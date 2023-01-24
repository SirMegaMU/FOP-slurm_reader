import java.io.*;
import java.text.ParseException;

import parser.*;

import static UsrInterface.UsrInterface.*;
import static data_structure.data.*;

public class Main {
    public static void main(String[] args) throws ParseException, FileNotFoundException {
        System.out.println("****************************************");
        System.out.println("**********   loading files   **********");
        System.out.println("****************************************\n");
        parse_file.readfile(job_map);
        System.out.println("\n****************************************");
        System.out.println("**********      complete      **********");
        System.out.println("****************************************");
        main_page();
    }
}
