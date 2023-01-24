package filter;

import org.jetbrains.annotations.NotNull;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class job_filter {
    public static void job_get_by_string(@NotNull Table jobs_table, String filter, String content) {
        if (jobs_table.containsColumn(filter)) {
            Table result = jobs_table.where(jobs_table.stringColumn(filter).isEqualTo(content));
            System.out.println(result);
        } else {
            System.out.println("No such Column");
        }

    }

    public static void job_get_by_time(@NotNull Table jobs_table, String start, String end) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start_t = LocalDate.parse(start, fmt);
            LocalDate end_t = LocalDate.parse(end, fmt);
            Table result_p = jobs_table.where(jobs_table.dateColumn("start").isAfter(start_t));
            Table result = result_p.where(result_p.dateColumn("end").isBefore(end_t));
            System.out.println(result);
    }
}
