import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by 의현 on 2015-02-16.
 */
public class Debug {

    static File logFile = new File("logReport.txt");

    public static void initialize()
    {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(logFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Debug.Log("initialized Debugger");
    }

    public static void Log(String report)
    {
        try {
            BufferedWriter printer = new BufferedWriter(new FileWriter(logFile,true));
            printer.write("\n"+report);
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println("logging");
    }

}
