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

public class job {
    public void main(String[] args) throws ParseException {
        Table jobs = Table.create("Jobs table").addColumns(
                IntColumn.create("JobId"),
                StringColumn.create("job"),
                IntColumn.create("InitPrio"),
                IntColumn.create("usec"),
                IntColumn.create("uid"),
                StringColumn.create("sched"),
                StringColumn.create("NodeList"),
                StringColumn.create("CPUs"),
                StringColumn.create("Partition"),
                StringColumn.create("starus"),
                DateColumn.create("start"),
                DateColumn.create("end")
        );
    }

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
    }


}
