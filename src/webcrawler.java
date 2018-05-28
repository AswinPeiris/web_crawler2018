//Name:Aswin Peiris
//Created 4/04/2018

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class webcrawler{
    @SuppressWarnings("Duplicates")
    static String largestPage  = "";
    static int largestPageLength = 0;
    static String recentPage   = "";
    static String PgDateTime   = "";
    static ArrayList<String> pages = new ArrayList<String>();
    static ArrayList<String> VisitedPgs = new ArrayList<String>();
    static ArrayList<String> InvldPgs = new ArrayList<String>();
    static ArrayList<String> reDirPgs = new ArrayList<String>();
    public static void main (String[] args) throws Exception{
      crawler();
    }

    private static void crawler() throws Exception{
      try{
        //get user input for the URL and Port
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the website address (don't include the 'http://' in front): ");
	//minor adjustments to url        
	String htt = "http://";
        String address = scan.next();
        address = htt+address;
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
        System.out.println("Webcrawler: socket connected to server's local port: " + sock.getLocalPort());
        System.out.println("Remote address: " + sock.getInetAddress());
        System.out.println("Port: " + sock.getPort());
        System.out.println();
        //get request for html page from server
        System.out.println("...");
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
          System.out.println("...");
          VisitedPgs.add(address);
        while((line = buff.readLine()) != null){
            if(line.contains("HTTP/1.1 4")){
                InvldPgs.add(address);
                VisitedPgs.add(address);
                break;
            }else if(line.contains("HTTP/1.1 3")){
                while((line = buff.readLine()) != null){
                    if(line.contains("<a href=")){
                        String redirect = line.substring(line.lastIndexOf("href=")+6,line.lastIndexOf('"'));
                        reDirPgs.add(address+" and the destination: "+redirect);
                        pages.add(redirect);
                        break;
                    }
                   break;
                }
                break;
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
                }
                if(line.contains("<a href=")){
                    String link = line.substring(line.lastIndexOf("href=")+6,line.lastIndexOf('"'));
                    if(urlVerification(link)){
                        continue;
                    }else{
                        pages.add(link);
                    }
                }
            }
        }
        System.out.println();
        //closing the socket and other imports
        printwriter.close();
        out.close();
        in.close();
        scan.close();
        sock.close();
        System.out.println("This may take a while...");
        //reads through the rest of the pages
        pageReader(pages.get(1), port);
        System.out.println("...");
        //return results
        System.out.println("------------------------------------------------------");
        System.out.println("------------------------REPORT------------------------");
        System.out.println("Total number of URLs visited: "+VisitedPgs.size()+" and the links :");
        for(int i = 0; i < VisitedPgs.size(); i++){
          System.out.println(VisitedPgs.get(i));
        }
	System.out.println();
        System.out.println("Total number of Invalid URLs: "+InvldPgs.size());
	System.out.println();
        System.out.println("The largest page: "+largestPage+" and size: "+largestPageLength);
	System.out.println();
        System.out.println("The most recently modifed page: "+ recentPage+" and its date/time: "+PgDateTime);
	System.out.println();
        System.out.println("List of redirected pages: ");
        for(int i = 0; i < reDirPgs.size();i++) {
            System.out.println(reDirPgs.get(i));
        }
        System.out.println("------------------------------------------------------");
	System.out.println("------------------------------------------------------");
      }catch (MalformedURLException e){
          System.out.println("ERROR MalformedURL: Something is wrong with your URL");
      }
      catch (IOException e){
          System.out.println("ERROR IO: cannot connect to the website");
      }
    }

    private static void pageReader(String address, int port) throws Exception{
        try{
            System.out.println("found URL!");
            TimeUnit.SECONDS.sleep(2);
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
            VisitedPgs.add(address);
            while((line = buff.readLine()) != null){
              if(line.contains("HTTP/1.1 4")){
                  //getting 4xx responses
                  InvldPgs.add(address);
                  pages.remove(address);
                  break;
              }else if(line.contains("HTTP/1.1 3")){
                 while((line = buff.readLine())!= null){
                   //getting 3xx responses
                   if(line.contains("<a href=")){
                     String redirect = line.substring(line.lastIndexOf("href=")+6, line.lastIndexOf('"'));
                     if(urlVerification(redirect) == false){
                       pages.add(redirect);
                     }
                     reDirPgs.add(address+" and the destination:"+redirect);
                     break;
                   }
                 }
                 break;
              }else {
                  if (line.contains("Content-Length: ")) {
                      //getting the size of page
                      String contentLength = line.substring(16);
                      int cl = Integer.parseInt(contentLength);
                      if (cl >= largestPageLength) {
                          largestPage = address;
                          largestPageLength = cl;
                      }
                  }
                  if (line.contains("Last-Modified: ")) {
                      //this section will get most recently modified date
                      String lastMod = line.substring(15);
                      //newest date
                      DateFormat lm = new SimpleDateFormat("E, dd MMM YYYY HH:mm:ss z", Locale.ENGLISH);
                      Date lmDate = lm.parse(lastMod);
                      //old date
                      DateFormat oldLm = new SimpleDateFormat("E, dd MMM YYYY HH:mm:ss z", Locale.ENGLISH);
                      Date oldLmDate = oldLm.parse(PgDateTime);
                      if(oldLmDate.compareTo(lmDate) <= 0){
                          recentPage = address;
                          PgDateTime = lastMod;
                      }
                  }
                  if(line.contains("<a href=")) {
                      //extracting the links
                      String gatheredLink = line.substring(line.lastIndexOf("href=")+6,line.lastIndexOf('"'));
                      if(urlVerification(gatheredLink) == true){
                        continue;
                    }else{
                        pages.add(gatheredLink);
                    }
                  }
              }
          }
          pages.remove(address);
          printwriter.close();
          out.close();
          in.close();
          sock.close();
          int j = 0;
          while (j < pages.size()){
              pageReader(pages.get(j), port);
              j++;
          }
        }catch (MalformedURLException e){
          System.out.println("ERROR: Something is wrong with your URL");
        }catch (IOException e){
          System.out.println("ERROR: cannot connect to the website");
          //adds a problem webpage to the invalids and visited to avoid visiting it again.
          VisitedPgs.add(address);
          InvldPgs.add(address);
        }
    }

    public static Boolean urlVerification(String link){
      //checks if we have visited the url previously
      if(VisitedPgs.contains(link)){
          return true;
      }else{
          return false;
      }
    }
}
