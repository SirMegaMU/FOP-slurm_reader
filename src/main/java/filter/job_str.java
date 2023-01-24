package filter;

import org.jetbrains.annotations.NotNull;
import tech.tablesaw.api.Table;

public class job_str {
    public static void job_get_by_string(@NotNull Table jobs_table, String filter, String content) {
        Table result = jobs_table.dropWhere(jobs_table.stringColumn(filter).isEqualTo(content));
        System.out.print(result);
    }
}
