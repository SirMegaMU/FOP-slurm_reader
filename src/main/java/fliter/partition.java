package fliter;

import org.jetbrains.annotations.NotNull;
import tech.tablesaw.api.Table;

public class partition {
    public static void get_by_partition(@NotNull Table jobs_table, String partition) {
        Table result = jobs_table.dropWhere(jobs_table.stringColumn("Partition").isEqualTo(partition));
        System.out.print(result);
    }
}
