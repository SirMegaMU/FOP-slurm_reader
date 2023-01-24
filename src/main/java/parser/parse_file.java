package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static parser.slurm_error.add_error;

public class parse_file {
    public static int[] readfile(String fileloc, HashMap<Integer, slurm_job> job_map) {
        int parse_fail = 0, parse_success = 0;
        try {
            Scanner file_scanner = new Scanner(new File(fileloc));
            while (file_scanner.hasNextLine()) {
                String line = file_scanner.nextLine() + " ";
                //System.out.println(line);
                int JobId = 0, InitPrio = 0, usec = 0, uid = 0, CPUs = 0, association = 0;
                String NodeList = "", Partition = "", status = "", account = "", user = "";
                String time = "";

                int marker = 0;

                // [2022-06-01T01:02:35.148] _slurm_rpc_submit_batch_job: JobId=42802 InitPrio=19758 usec=589
                String submit_p = "\\[(.*T.*)\\] _slurm_rpc_submit_batch_job: JobId=([0-9]*) InitPrio=([0-9]*) usec=([0-9]*)";
                Pattern submit = Pattern.compile(submit_p);
                // [2022-06-01T01:02:36.012] sched: Allocate JobId=42802 NodeList=gpu05 #CPUs=32 Partition=gpu-v100s
                String sched_p = "\\[(.*T.*)\\] sched: Allocate JobId=([0-9]*) NodeList=(.*?) #CPUs=([0-9]*) Partition=(.*)";
                Pattern sched = Pattern.compile(sched_p);
                //[2022-06-01T04:05:04.581] _job_complete: JobId=42801 WEXITSTATUS 2
                String complete_p = "\\[(.*T.*)\\] _job_complete: JobId=([0-9]*) (.*)";
                Pattern complete = Pattern.compile(complete_p);
                // [2022-06-01T10:39:24.178] _slurm_rpc_kill_job: REQUEST_KILL_JOB JobId=42819 uid 548200029
                String kill_p = "\\[(.*T.*)\\] _slurm_rpc_kill_job: REQUEST_KILL_JOB JobId=([0-9]*) uid ([0-9]*)";
                Pattern kill = Pattern.compile(kill_p);

                // [2022-06-01T15:12:23.290] error_str: This association 187(account='free', user='lobbeytan', job_str='(null)') does not have access to qos long
                String error_p = "\\[(.*T.*)\\] error: This association ([0-9]*)\\(account='(.*)', user='(.*)', partition='(.*)'\\) (.*?)";
                Pattern error = Pattern.compile(error_p);

                Matcher m1 = submit.matcher(line);
                Matcher m2 = sched.matcher(line);
                Matcher m3 = complete.matcher(line);
                Matcher m4 = kill.matcher(line);
                Matcher m5 = error.matcher(line);

                try {
                    if (m1.find()) {
                        time = m1.group(1);
                        JobId = Integer.parseInt(m1.group(2));
                        InitPrio = Integer.parseInt(m1.group(3));
                        usec = Integer.parseInt(m1.group(4));
                        marker = 1;
                        System.out.println("* submit    \t\t match");
                        parse_success++;
                    } else if (m2.find()) {
                        time = m2.group(1);
                        JobId = Integer.parseInt(m2.group(2));
                        NodeList = m2.group(3);
                        CPUs = Integer.parseInt(m2.group(4));
                        Partition = m2.group(5);
                        marker = 2;
                        System.out.println("* sched     \t\t match");
                        parse_success++;
                    } else if (m3.find()) {
                        time = m3.group(1);
                        JobId = Integer.parseInt(m3.group(2));
                        status = m3.group(3);
                        marker = 3;
                        System.out.println("* complete  \t\t match");
                        parse_success++;
                    } else if (m4.find()) {
                        time = m4.group(1);
                        JobId = Integer.parseInt(m4.group(2));
                        uid = Integer.parseInt(m4.group(3));
                        System.out.println("* kill      \t\t match");
                        parse_success++;
                    } else if (m5.find()) {
                        time = m5.group(0);
                        association = Integer.parseInt(m5.group(1));
                        account = m5.group(2);
                        user = m5.group(3);
                        Partition = m5.group(4);
                        add_error(time, association, account, user, Partition);
                        System.out.println("* error     \t\t match");
                        parse_success++;
                    } else {
                        System.out.println("* matcher   \t\t failed");
                        parse_fail++;
                    }
                } catch (IllegalStateException e) {
                    continue;
                }

                if (JobId != 0) {
                    if (job_map.containsKey(JobId)) {
                        if (marker == 1) {
                            job_map.get(JobId).InitPrio = InitPrio;
                            job_map.get(JobId).usec = usec;
                            job_map.get(JobId).add_time(time);
                        } else if (marker == 2) {
                            job_map.get(JobId).NodeList = NodeList;
                            job_map.get(JobId).CPUs = CPUs;
                            job_map.get(JobId).Partition = Partition;
                        } else if (marker == 3) {
                            job_map.get(JobId).status = status;
                            job_map.get(JobId).add_time(time);
                        }
                    } else {
                        slurm_job job = new slurm_job(JobId);
                        job_map.put(JobId, job);
                        if (marker == 1) {
                            job_map.get(JobId).InitPrio = InitPrio;
                            job_map.get(JobId).usec = usec;
                            job_map.get(JobId).add_time(time);
                        } else if (marker == 2) {
                            job_map.get(JobId).NodeList = NodeList;
                            job_map.get(JobId).CPUs = CPUs;
                            job_map.get(JobId).Partition = Partition;
                        } else if (marker == 3) {
                            job_map.get(JobId).add_time(time);
                            job_map.get(JobId).end(status);
                        }
                    }
                }
            }
            file_scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new int[]{parse_success, parse_fail};
    }
}
