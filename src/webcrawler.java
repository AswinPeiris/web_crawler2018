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
    //static final int port  = 3310;
    int visitedPages    =  0;
    String largestPage  = "";
    String recentPage   = "";
    String PgDateTime   = "";
    ArrayList VstDPgs = new ArrayList();
    ArrayList InvldPgs = new ArrayList();
    //static final String address = "http://3310exp.hopto.org:9780/";

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

        //open socket connection to server
          //this code was mostly adapted from the second tutorial client/server code
        Socket sock =  new Socket(address,port);
        System.out.println("Webcrawler: created socket connected to local port" +
                sock.getLocalPort() + "and to remote address" +
                sock.getInetAddress() + "and port" +
                sock.getPort());
        //read through pages

        //return results

        //close socket
        sock.close();
        //URL site =  new URL (address)
        //URLConnection c = oracle.openConnection();
      }
      catch (IOException e){
        System.out.println("ERROR: cannot connect to the website");
      }
    }

    public String pageReader(String address){
      return null;
    }

    public String urlVerification(String address){
      String verification = "yes";
      //checks if we have visited the webpage previously
      //checks if it was listed as invalid
      return verification;
    }
}
