package src.main;

import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public void main(String[] args) throws ParseException {
        System.out.print(jobs);

    }

    public Table jobs = Table.create("Jobs table").addColumns(
            IntColumn.create("JobId"),
            StringColumn.create("Main"),
            IntColumn.create("InitPrio"),
            IntColumn.create("usec"),
            IntColumn.create("uid"),
            StringColumn.create("sched"),
            StringColumn.create("NodeList"),
            StringColumn.create("CPUs"),
            StringColumn.create("Partition"),
            StringColumn.create("status"),
            DateColumn.create("start"),
            DateColumn.create("end")
    );

    public class slurm_job {
        int JobId, InitPrio, usec, uid;
        String job, sched, NodeList, CPUs, Partition, status;
        List<Date> dates = new ArrayList();

        public slurm_job(int JobId) {
            this.JobId = JobId;
        }

        public Date add_time(String s_time) throws ParseException {
            //2022-06-01T09:10:34.641
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSS");
            try {
                this.dates.add(ft.parse(s_time));
                return ft.parse(s_time);
            } catch (ParseException e) {
                throw new RuntimeException(e);
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
                    StringColumn.create("sched", new String[]{this.sched}),
                    StringColumn.create("NodeList", new String[]{this.NodeList}),
                    StringColumn.create("CPUs", new String[]{this.CPUs}),
                    StringColumn.create("Partition", new String[]{this.Partition}),
                    StringColumn.create("status", new String[]{this.status}),
                    DateColumn.create("start"),
                    DateColumn.create("end")
            );

            jobs.appendRow();
        }
    }


}
