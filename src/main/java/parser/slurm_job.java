package parser;

import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static data_structure.data.*;

public class slurm_job {
    public int JobId, InitPrio, usec, uid, CPUs;
    public String job, NodeList, Partition, status;
    public LocalDateTime start, end;

    public slurm_job(int JobId) {
        this.JobId = JobId;
    }

    public static LocalDateTime toLocalDateTime(String dateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime ldt = LocalDateTime.parse(dateTime, df);
        return ldt;
    }

    public void add_time(String s_time) {
        //2022-06-01T09:10:34.641
        if (this.start == null) {
            this.start = toLocalDateTime(s_time);
        } else {
            this.end = toLocalDateTime(s_time);
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
                DateColumn.create("start", new LocalDate[]{this.start.toLocalDate()}),
                DateColumn.create("end", new LocalDate[]{this.end.toLocalDate()})
        );
        jobs.append(job);
        job_map.remove(this.JobId);
    }
}