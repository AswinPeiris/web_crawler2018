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
import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.*;

public class webcrawler{
    String largestPage  = "";
    String recentPage   = "";
    String PgDateTime   = "";
    static ArrayList pages = new ArrayList();
    static ArrayList VisitedPgs = new ArrayList();
    static ArrayList InvldPgs = new ArrayList();

    public static void main (String[] args) throws IOException{
      crawler();
    }

    public static void crawler(){
      try{
        //get user input for the URL and Port
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the website address (don't include the 'http://' in front): ");
        String address = "http://"+scan.next();
        VisitedPgs.add(address);
        URL url = new URL(address);
        String path = url.getPath();
        System.out.println("Please enter the port: ");
        //to give empty paths something to do
        if(path.isEmpty()){
            path = "/index.html";
        };
        int port = scan.nextInt();
        //open socket connection and states details about the connection
        Socket sock = new Socket (url.getHost(), port);
        System.out.println("Webcrawler: socket connected to server's local port: " + sock.getLocalPort());
        System.out.println("Remote address: " + sock.getInetAddress());
        System.out.println("Port: " + sock.getPort());
        System.out.println("URL: "+ url.toString());
        //System.out.println("Path: "+ path);
        //get request for html page from server
        OutputStream out = sock.getOutputStream();
        PrintWriter printwriter = new PrintWriter(out, false);
        printwriter.print("GET "+path+" HTTP/1.0\r\n");
        printwriter.print("\r\n");
        printwriter.flush();
        //receive html page from server
        InputStream in = sock.getInputStream();
        InputStreamReader InSR = new InputStreamReader(in);
        BufferedReader buff = new BufferedReader(InSR);
          String line;
        while((line = buff.readLine()) != null){
            if(line.contains("<a href=")){
                String link = line.substring(line.lastIndexOf("href=")+6,line.lastIndexOf('"'));
                System.out.println(link);
                if(urlVerification(link)){
                    continue;
                }else{
                    pages.add(link);
                }
            }
        }
        System.out.println();
        System.out.println(pages);
        System.out.println(VisitedPgs);
        //read through pages
        //return results
        //close socket
        printwriter.close();
        out.close();
        in.close();
        scan.close();
        sock.close();
      }catch (MalformedURLException e){
          System.out.println("ERROR: Something is wrong with your URL");
      }
      catch (IOException e){
        System.out.println("ERROR: cannot connect to the website");
      }
    }

    public static String pageReader(String address, String port){
        return null;
    }

    public static Boolean urlVerification(String link){
      //checks if we have visited the webpage previously
      if(VisitedPgs.contains(link)){
          return true;
      }else{
          return false;
      }
    }
}
