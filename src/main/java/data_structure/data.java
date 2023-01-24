package data_structure;

import parser.slurm_job;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.util.HashMap;

public class data {
    public static HashMap<Integer, slurm_job> job_map = new HashMap<Integer, slurm_job>();
    public static Table jobs = Table.create("Jobs table").addColumns(
            IntColumn.create("JobId"),
            StringColumn.create("Main"),
            IntColumn.create("InitPrio"),
            IntColumn.create("usec"),
            IntColumn.create("uid"),
            StringColumn.create("NodeList"),
            IntColumn.create("CPUs"),
            StringColumn.create("Partition"),
            StringColumn.create("status"),
            DateColumn.create("start"),
            DateColumn.create("end")
    );
}
