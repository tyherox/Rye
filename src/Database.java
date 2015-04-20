import java.io.*;
import java.util.ArrayList;

/**
 * Created by JohnBae on 4/13/15.
 */
public class Database {


    public static void intialize() {
        try {
            final File parentDir = new File("1");
            parentDir.mkdir();
            parentDir.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
