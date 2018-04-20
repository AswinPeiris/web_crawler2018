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

public class webcrawler{
    static final int pages = 11;
    int visitedPages    =  0;
    String largestPage  = "";
    String recentPage   = "";
    String PgDateTime   = "";
    ArrayList VstDPgs = new ArrayList();
    ArrayList InvldPgs = new ArrayList();

    public static void main (String[] args) throws IOException{
      crawler();
    }

    public static void crawler(){
      try{
        //get user input
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the website address: ");
        String address = scan.next();
        System.out.println("Please enter the port: ");
        int port = scan.nextInt();
        //open socket connection
        Socket sock = new Socket (address, port);
        System.out.println("Webcrawler: created socket connected to local port: " + sock.getLocalPort());
        System.out.println("Remote address: " + sock.getInetAddress());
        System.out.println("Port: " + sock.getPort());
        //read through pages
        //return results
        //close socket
        scan.close();
        sock.close();
      }
      catch (IOException e){
        System.out.println("ERROR: cannot connect to the website");
      }
    }

    public static String pageReader(String address){
        return null;
    }

    public String urlVerification(String address){
      String verification = "yes";
      //checks if we have visited the webpage previously

      //checks if it was listed as invalid
      return verification;
    }
}
