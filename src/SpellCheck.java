import java.io.*;
import java.util.ArrayList;

import static java.lang.Character.isUpperCase;


public class SpellCheck {
	
	static ArrayList<String> list = new ArrayList<String>();
	
	public static void initialize() throws IOException {
		int counter = 0;
        BufferedReader br;
		File file = new File("src\\Vocab\\wordsEn.txt");
        if(file.exists() && !file.isDirectory()) {
             br = new BufferedReader(new FileReader(file));
        }
        else
        {
            InputStream in = SpellCheck.class.getResourceAsStream("/Vocab/wordsEn.txt");
            br = new BufferedReader(new InputStreamReader(in));
        }
        Debug.Log("initialized Mainframe");
		String line;
		while ((line = br.readLine()) != null) {
		   list.add(line);
		   counter++;
		}
		br.close();
	}

	public static boolean check(String f) {
        //System.out.println("checking word spelling: " + f);
        for (String aList : list) {
            if (aList.equals(f)){
                //System.out.println("found " +f);
                return true;
            }
            if (aList.equalsIgnoreCase(f)) {
                return isUpperCase(f.charAt(0));
            }
        }
		return false;
	}
	
	public static ArrayList<String>fill(String f) {
		boolean found = false;
		ArrayList<String> recommendation = new ArrayList<String>();
		recommendation.clear();
		recommendation.add("speculation");
		
		/// quick search of list for match ///

        for (String aList : list) {
            if (aList.equals(f)) {
                recommendation.clear();
                recommendation.add(aList);
                found = true;
                break;
            }
            if (aList.equalsIgnoreCase(f)) {
                if (isUpperCase(f.charAt(0))) {
                    recommendation.clear();
                    recommendation.add(aList);
                    found = true;
                    break;
                } else {
                    recommendation.add(aList);
                    found = true;
                    break;
                }
            }
        }
		
		/// mistake search of list for match ///
		
		for(int i = 0; i<list.size(); i++)
		{
			
		}
		
		/// not found protocol ///
		
		if(!found)
		{
			System.out.println("not found");
			recommendation.set(0,"not found");
		}
        else
        {
            System.out.println("found");
        }
		
		return recommendation;
	}

}
