import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public void main(String[] args) throws ParseException, FileNotFoundException {
        readfile();
    }

    public void readfile() {
        try {
            Scanner file_scanner = new Scanner(new File("src/main/resources/extracted_log"));
            while (file_scanner.hasNextLine()) {
                String line = file_scanner.nextLine();
                int JobId = 0, InitPrio = 0, usec = 0, uid = 0, CPUs = 0;
                String NodeList = "", Partition = "", status = "";
                Date start, end;
                Matcher m;

                int marker = 0;

                // [2022-06-01T01:02:35.148] _slurm_rpc_submit_batch_job: JobId=42802 InitPrio=19758 usec=589
                String submit_p = "\\[(.*T.*)\\] _slurm_rpc_submit_batch_job: JobId=([0-9]*) InitPrio=([0-9]*) usec=([0-9]*)\s";
                Pattern submit = Pattern.compile(submit_p);
                // [2022-06-01T01:02:36.012] sched: Allocate JobId=42802 NodeList=gpu05 #CPUs=32 Partition=gpu-v100s
                String sched_p = "\\[(.*T.*)\\] sched: Allocate JobId=([0-9]*) NodeList=(.*?) #CPUs=([0-9]*) Partition=(.*)\s";
                Pattern sched = Pattern.compile(sched_p);
                //[2022-06-01T04:05:04.581] _job_complete: JobId=42801 WEXITSTATUS 2
                String complete_p = "\\[(.*T.*)\\] _job_complete: JobId=[0-9]* (.*)\s";
                Pattern complete = Pattern.compile(complete_p);
                // [2022-06-01T15:12:23.290] error: This association 187(account='free', user='lobbeytan', partition='(null)') does not have access to qos long
                String error_p = "\\[(.*T.*)\\] error: This association ([0-9]*)\\(account='(.*)', user='(.*)', partition='(.*)'\\) (.*?)\s";
                Pattern error = Pattern.compile(error_p);
                // [2022-06-01T10:39:24.178] _slurm_rpc_kill_job: REQUEST_KILL_JOB JobId=42819 uid 548200029
                String kill_p = "\\[(.*T.*)\\] _slurm_rpc_kill_job: REQUEST_KILL_JOB JobId=([0-9]*) uid ([0-9]*)]\s";
                Pattern kill = Pattern.compile(kill_p);

                if (Pattern.matches(submit_p, line)) {
                    m = submit.matcher(line);
                    JobId = Integer.parseInt(m.group(0));
                    InitPrio = Integer.parseInt(m.group(1));
                    usec = Integer.parseInt(m.group(2));
                    marker = 1;
                } else if (Pattern.matches(sched_p, line)) {
                    m = sched.matcher(line);
                    JobId = Integer.parseInt(m.group(0));
                    NodeList = m.group(1);
                    CPUs = Integer.parseInt(m.group(2));
                    Partition = m.group(3);
                    marker = 2;
                } else if (Pattern.matches(complete_p, line)) {
                    m = complete.matcher(line);
                    JobId = Integer.parseInt(m.group(0));
                    status = m.group(1);
                    marker = 3;
                } else if (Pattern.matches(error_p, line)) {
                    m = kill.matcher(line);
                    JobId = Integer.parseInt(m.group(0));
                    uid = Integer.parseInt(m.group(1));
                    marker = 4;
                } else if (Pattern.matches(kill_p, line)) {
                }
                if (JobId != 0) {
                    if (job_map.containsKey(JobId)) {
                        if (marker == 1) {
                            job_map.get(JobId).InitPrio = InitPrio;
                            job_map.get(JobId).usec = usec;
                        } else if (marker == 2) {
                            job_map.get(JobId).NodeList = NodeList;
                            job_map.get(JobId).CPUs = CPUs;
                            job_map.get(JobId).Partition = Partition;
                        } else if (marker == 3) {
                            job_map.get(JobId).status = status;
                        } else if (marker == 4) {
                            job_map.get(JobId).uid = uid;
                        }
                    } else {
                        slurm_job job = new slurm_job(JobId);
                        job_map.put(JobId, job);
                        if (marker == 1) {
                            job_map.get(JobId).InitPrio = InitPrio;
                            job_map.get(JobId).usec = usec;
                        } else if (marker == 2) {
                            job_map.get(JobId).NodeList = NodeList;
                            job_map.get(JobId).CPUs = CPUs;
                            job_map.get(JobId).Partition = Partition;
                        } else if (marker == 3) {
                            job_map.get(JobId).status = status;
                        } else if (marker == 4) {
                            job_map.get(JobId).uid = uid;
                        }
                    }
                }
            }
            file_scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<Integer, slurm_job> job_map = new HashMap<Integer, slurm_job>();
    public Table jobs = Table.create("Jobs table").addColumns(
            IntColumn.create("JobId"),
            StringColumn.create("Main"),
            IntColumn.create("InitPrio"),
            IntColumn.create("usec"),
            IntColumn.create("uid"),
            StringColumn.create("NodeList"),
            IntColumn.create("CPUs"),
            StringColumn.create("Partition"),
            StringColumn.create("status"),
            StringColumn.create("start"),
            StringColumn.create("end")
    );

    public class slurm_job {
        int JobId, InitPrio, usec, uid, CPUs;
        String job, NodeList, Partition, status;
        Date start, end;

        public slurm_job(int JobId) {
            this.JobId = JobId;
        }

        public void add_time(String s_time) throws ParseException {
            //2022-06-01T09:10:34.641
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSS");
            if (this.start == null) {
                this.start = ft.parse(s_time);
            } else {
                this.end = ft.parse(s_time);
            }
        }

        public void end(String s_status) {
            this.status = s_status;
            Table job = Table.create("Jobs table").addColumns(
                    IntColumn.create("JobId", new int[]{this.JobId}),
                    StringColumn.create("Main", new String[]{this.job}),
                    IntColumn.create("InitPrio", new int[]{this.InitPrio}),
                    IntColumn.create("usec", new int[]{this.usec}),
                    IntColumn.create("uid", new int[]{this.uid}),
                    StringColumn.create("NodeList", new String[]{this.NodeList}),
                    IntColumn.create("CPUs", new int[]{this.CPUs}),
                    StringColumn.create("Partition", new String[]{this.Partition}),
                    StringColumn.create("status", new String[]{this.status}),
                    StringColumn.create("start", new String[]{this.start.toString()}),
                    StringColumn.create("end", new String[]{this.end.toString()})
            );
            jobs.appendRow();
            job_map.remove(this.JobId);
        }
    }


}
