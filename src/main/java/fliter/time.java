package fliter;

import org.jetbrains.annotations.NotNull;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class time {
    public static void get_by_time(@NotNull Table jobs_table, String start, String end) {
        // yyyy-MM-ddTHH:mm:ss.SSS
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:SSS");
        LocalDate start_t = LocalDate.parse(start, fmt);
        LocalDate end_t = LocalDate.parse(end, fmt);
        Table result_p = jobs_table.dropWhere(jobs_table.dateColumn("start").isAfter(start_t));
        Table result = result_p.dropWhere(result_p.dateColumn("end").isBefore(end_t));
        System.out.print(result);
    }
}
