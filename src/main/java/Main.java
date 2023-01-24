import java.io.*;
import java.text.ParseException;
import java.util.Scanner;

import parser.*;

import static UsrInterface.UsrInterface.*;
import static data_structure.data.*;

public class Main {
    public static void main(String[] args) throws ParseException, FileNotFoundException {
        String file_loc;
        Scanner key = new Scanner(System.in);
        System.out.println("****************************************");
        System.out.println("*****  type the path of log file   *****");
        System.out.print("* > :");
        file_loc = key.next();
        file_loc = "/home/megamu/Documents/FOP-Assignment/src/main/resources/extracted_log";
        System.out.println("****************************************");
        System.out.println("**********   loading files    **********");
        System.out.println("****************************************\n");
        int[] res = parse_file.readfile(file_loc, job_map);
        System.out.println("****************************************");
        System.out.println("**********      complete      **********");
        System.out.println("****************************************");
        System.out.println("* success : "+res[0]);
        System.out.println("* fail    : "+res[1]);
        main_page();
    }
}
