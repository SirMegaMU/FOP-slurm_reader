package UsrInterface;

import java.util.Objects;
import java.util.Scanner;

import static data_structure.data.errors;
import static data_structure.data.jobs;
import static filter.error_filter.err_get_by_string;
import static filter.error_filter.err_get_by_time;
import static filter.job_filter.job_get_by_string;
import static filter.job_filter.job_get_by_time;

public class UsrInterface {
    public static void main_page() {
        System.out.println("****************************************");
        System.out.println("* 1 : show data");
        System.out.println("* 2 : filter data by Column Name");
        System.out.println("* 3 : filter data by Time (yyyy-MM-dd) ");
        System.out.println("* q : quit");
        System.out.println("****************************************");
        System.out.print("* > : ");
        Scanner key = new Scanner(System.in);
        String choice = key.nextLine();
        if (Objects.equals(choice, "1")) {
            System.out.println("****************************************");
            System.out.print("choose the data sheet you want (jobs/errors) : ");
            String name = key.nextLine();
            show_data(name);
        } else if (Objects.equals(choice, "2")) {
            System.out.println("****************************************");
            System.out.print("choose the data sheet you want (jobs/errors) : ");
            String name = key.nextLine();
            System.out.print("type the filter you want  : ");
            String filter = key.nextLine();
            System.out.print("type the content you want : ");
            String content = key.nextLine();
            filter_by_str(name, filter, content);
        } else if (Objects.equals(choice, "3")) {
            System.out.println("****************************************");
            System.out.print("choose the data sheet you want (jobs/errors) : ");
            String name = key.nextLine();
            System.out.print("type the start time you want : ");
            String start = key.nextLine();
            System.out.print("type the end time you want   : ");
            String end = key.nextLine();
            filter_by_time(name, start, end);
        } else if (Objects.equals(choice, "q")) {
            return;
        } else {
            System.out.print("Unknown key");
            main_page();
        }
    }

    public static void show_data(String name) {
        if (Objects.equals(name, "jobs")) {
            System.out.println(jobs);
        } else if (Objects.equals(name, "errors")) {
            System.out.println(errors);
        } else {
            System.out.println("Unknown data name");
        }
        Scanner key = new Scanner(System.in);
        String input = key.nextLine();
        if (Objects.equals(input, "q")) {
            main_page();
        }
    }

    public static void filter_by_str(String name, String filter, String content) {
        if (Objects.equals(name, "jobs")) {
            job_get_by_string(jobs, filter, content);
        } else if (Objects.equals(name, "errors")) {
            err_get_by_string(errors, filter, content);
        } else {
            System.out.println("Unknown data name");
        }
        Scanner key = new Scanner(System.in);
        String input = key.nextLine();
        if (Objects.equals(input, "q")) {
            main_page();
        }
    }

    public static void filter_by_time(String name, String start, String end) {
        if (Objects.equals(name, "jobs")) {
            job_get_by_time(jobs, start, end);
        } else if (Objects.equals(name, "errors")) {
            err_get_by_time(errors, start, end);
        } else {
            System.out.println("Unknown data name");
        }
        Scanner key = new Scanner(System.in);
        String input = key.nextLine();
        if (Objects.equals(input, "q")) {
            main_page();
        }
    }

}
