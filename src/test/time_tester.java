import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Scanner;

class tmp {
    public static void main(String[] args) {
        Scanner key = new Scanner(System.in);
        String time_s = key.next();
        System.out.print(toLocalDateTime(time_s));
    }

    public static LocalDateTime toLocalDateTime(String dateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime ldt = LocalDateTime.parse(dateTime, df);
        return ldt;
    }
}