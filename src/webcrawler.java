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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class webcrawler{
    static String largestPage  = "";
    static int largestPageLength = 0;
    static String recentPage   = "";
    static String PgDateTime   = "";
    static ArrayList<String> pages = new ArrayList<String>();
    static ArrayList<String> VisitedPgs = new ArrayList<String>();
    static ArrayList<String> InvldPgs = new ArrayList<String>();
    static ArrayList<String> reDirPgs = new ArrayList<String>();

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
        }
        int port = scan.nextInt();
        //open socket connection and states details about the connection
        Socket sock = new Socket (url.getHost(), port);
        System.out.println();
        System.out.println("Webcrawler: socket connected to server's local port: " + sock.getLocalPort());
        System.out.println("Remote address: " + sock.getInetAddress());
        System.out.println("Port: " + sock.getPort());
        System.out.println();
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
            if(line.contains("HTTP/1.1 4")){
                InvldPgs.add(address);
                VisitedPgs.add(address);
            }else if(line.contains("HTTP/1.1 3")){
                reDirPgs.add(address);
                VisitedPgs.add(address);
            }else{
                if (line.contains("Content-Length: ")) {
                    String contentLength = line.substring(16);
                    int cl = Integer.parseInt(contentLength);
                    largestPage = address;
                    largestPageLength = cl;
                }
                if (line.contains("Last-Modified: ")) {
                    String lastMod = line.substring(15);
                    PgDateTime = lastMod;
                    recentPage = address;
                    System.out.println("Last-Modified: "+lastMod);
                }
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
        }
          System.out.println();
          printwriter.close();
          out.close();
          in.close();
          scan.close();
          sock.close();
          int i = 0;
          while(i < pages.size()){
              //pageReader(pages.get(i), port);
              i++;
          }
          //System.out.println(pages);
          //System.out.println(VisitedPgs);
          //read through pages
          //return results
          //close socket
        /*
        * System.out.println("------------------------------------------------------";
        * System.out.println("------------------------REPORT------------------------";
        * System.out.println("Total number of URLs visited: "+VisitedPgs.size());
        * System.out.println("Total number of Invalid URLs: "+InvldPgs.size());
        * System.out.println("The largest page: "+largestPage+" and size: "+largestPageLength);
        * System.out.println("The most recently modifed page date/time: "+ recentPage);
        * System.out.println("List of redirected pages: "+reDirPgs);
        * System.out.println("------------------------------------------------------";
        * */
      }catch (MalformedURLException e){
          System.out.println("ERROR MalformedURL: Something is wrong with your URL");
      }
      catch (IOException e){
          System.out.println("ERROR IO: cannot connect to the website");
      }
    }

    public static void pageReader(String address, int port) throws Exception{
        try{
          URL url = new URL (address);
          String path = url.getPath();
          if(path.isEmpty()){
              path = "/index.html";
          }
          //initiate socket connection
          Socket sock = new Socket(url.getHost(),port);
          //begins procedure to send get request to server
          OutputStream out = sock.getOutputStream();
          PrintWriter printwriter = new PrintWriter(out, false);
          printwriter.print("GET "+path+" HTTP/1.0\r\n");
          printwriter.print("\r\n");
          printwriter.flush();
          //starts parsing received data from server
          InputStream in = sock.getInputStream();
          InputStreamReader InSR = new InputStreamReader(in);
          BufferedReader buff = new BufferedReader(InSR);
          String line;
          while((line = buff.readLine()) != null){
              if(line.contains("HTTP/1.1 4")){
                  InvldPgs.add(address);
                  VisitedPgs.add(address);
              }else if(line.contains("HTTP/1.1 3")){
                  reDirPgs.add(address);
                  VisitedPgs.add(address);
              }else {
                  if (line.contains("Content-Length: ")) {
                      String contentLength = line.substring(16);
                      int cl = Integer.parseInt(contentLength);
                      if (cl >= largestPageLength) {
                          largestPage = address;
                          largestPageLength = cl;
                      } else {
                          System.out.println("Smaller Content Length : " + contentLength);
                      }
                  }
                  if (line.contains("Last-Modified: ")) {
                      String lastMod = line.substring(15);
                      //newest date
                      DateFormat lm = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy", Locale.ENGLISH);
                      Date lmDate = lm.parse(lastMod);
                      //old date
                      DateFormat oldlm = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy", Locale.ENGLISH);
                      Date oldLmDate = oldlm.parse(PgDateTime);
                      if(lmDate.compareTo(oldLmDate) >= 0){
                       recentPage = address;
                       PgDateTime = lastMod;
                      }
                  }
              }
          }
          printwriter.close();
          out.close();
          in.close();
          sock.close();       
        }catch (MalformedURLException e){
          System.out.println("ERROR: Something is wrong with your URL");
        }catch (IOException e){
          System.out.println("ERROR: cannot connect to the website");
        }
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
