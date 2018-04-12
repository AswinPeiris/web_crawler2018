//Username: u6177012    Name:Aswin Peiris
//Assignment: 2         Course: COMP3310
//Created 4/04/2018

/*needs to record:
-number of pages
-the largest page (and size)
-the most-recently modified page (and its date/time)
-A list of invalid pages (not) found (404)
-A list of redirected pages found (30x) and where they redirect to
*/
import java.io.*;
import java.net.*;
import java.util.*;

public static int pages = 11;
public static int port  = 3310;

public class webcrawler{
    int visitedPages    =  0;
    String largestPage  = "";
    String recentPage   = "";
    String PgDateTime   = "";
    ArrayList invalidPgs = new ArrayList();
    String address = "http://3310exp.hopto.org:9780/";
    Scanner scan = new Scanner(System.in);

    public static void main (String[] args) throws IOException{
      crawler();
    }

    public static void crawler(){
      try{
        URL site =  new URL (address);
        //URLConnection c = oracle.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader (site.openStream()));

        String inputLine;
        while((inputLine = in.readLine()) != null){
          System.out.println(inputLine);
        }
        in.close();
      }
      catch (IOException e){
        System.out.println("ERROR: cannot connect to website");
      }
    }
}
