package formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class time_formatter {
    public static LocalDateTime toLocalDateTime(String dateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime ldt = LocalDateTime.parse(dateTime, df);
        return ldt;
    }
}
