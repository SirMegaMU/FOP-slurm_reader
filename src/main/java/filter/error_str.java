package filter;

import org.jetbrains.annotations.NotNull;
import tech.tablesaw.api.Table;

public class error_str {
    public static void err_get_by_string(@NotNull Table errors_table, String filter, String content) {
        Table result = errors_table.dropWhere(errors_table.stringColumn(filter).isEqualTo(content));
        System.out.print(result);
    }
}
