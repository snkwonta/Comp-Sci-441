
/**
 * UrlCache Class
 * 
 *
 */

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class UrlCache {

	HashMap	<String, String> catalog;
	
    /**
     * Default constructor to initialize data structures used for caching/etc
	 * If the cache already exists then load it. If any errors then throw runtime exception.
	 *
     * @throws IOException if encounters any errors/exceptions
     */
	public UrlCache() throws IOException {
		String test = (System.getProperty("user.dir"));
//		File header = new File(test, "catalog.ser");
		
		
		// check if file exists. If so, initialize catalog with info from saved catalog
			try{
				FileInputStream fileIn = new FileInputStream("catalog.ser");
				ObjectInputStream objIn = new ObjectInputStream(fileIn);
				catalog = (HashMap<String, String>)objIn.readObject();
				fileIn.close();
				objIn.close();
				
			} catch(FileNotFoundException e){
				File fOut = new File("catalog.ser");
				catalog = new HashMap<String, String>();

			} catch(Exception e){
				System.out.println("Error " + e.getMessage());
			} 
	}
	
    /**
     * Downloads the object specified by the parameter url if the local copy is out of date.
	 *
     * @param url	URL of the object to be downloaded. It is a fully qualified URL.
     * @throws IOException if encounters any errors/exceptions
     */
	public void getObject(String url) throws IOException {
//		FileOutputStream out = new FileOutputStream(header);
		int portNum = 80;
		String hostname;
		String path;
		

		
		hostname = url.substring(0, url.indexOf("/"));
		path = url.substring(url.indexOf("/"));
		int stop = url.indexOf("/");
		int hostStop = url.indexOf(":");
		
		if(url.indexOf(":") != -1){
			hostname = url.substring(0, hostStop);
			portNum = Integer.parseInt(url.substring(url.indexOf(":") + 1, stop));
		}
		
		String lastModified = "";
		
//		if(catalog.containsKey(url)){
//			lastModified = catalog.get(url);
//		}
		
		try{
			Socket connect = new Socket(hostname, portNum);
			// HTTP GET request
			
			PrintWriter request = new PrintWriter(new DataOutputStream(connect.getOutputStream()));
		
			
			request.print("GET " + path + " HTTP/1.1\r\n"); 
			request.print("If-modified-since: "+ lastModified + "\r\n");
			request.print("Host: " + hostname + ":" + portNum + "\r\n");                       
			request.print("\r\n");
			request.flush(); 
			
			InputStream inputstream = connect.getInputStream( ); 
			
			// Initial byte array to hold data
			byte[] buffering = new byte[2048];
			byte[] buffering1 = new byte[2048];
			int readBytes = 0;
			String line = "";
			int count = 0;
			int oLength = 0;
			int counter1 = 0;
//			int numBytesRead = 0;
		try{
			while(readBytes != -1){
				inputstream.read(buffering, count, 1);
				count++;
				line = new String(buffering, 0, count, "US-ASCII");
				if(line.contains("\r\n\r\n")){
//					out.write(buffering);
					break;
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}

			Scanner scan = new Scanner(line);
			String lastMod = "";
			// remove last-modified value from header
			while(scan.hasNextLine()){
				lastMod = scan.nextLine();
				if(lastMod.contains("Last-Modified")){
					lastModified = lastMod.substring(lastMod.indexOf(":") + 2);
				} else if(lastMod.contains("Content-Length")){
					oLength = Integer.parseInt(lastMod.substring(lastMod.indexOf(":") + 2));
				}
			}
			
			scan.close();
			// conditional GET to know whether or not to download files from Internet
			
			if(line.contains("304 Not Modified")){
				System.out.println("File already here");
				
			} else if (line.contains("200 OK")){
				System.out.println("Downloading from URL: " + url);
				
				File download = new File(hostname + path);
				download.getParentFile().mkdirs();
				FileOutputStream fOut = new FileOutputStream(download);

				while(readBytes!= -1){
					if(counter1 == oLength){
						break;
					}
					readBytes = connect.getInputStream().read(buffering1);
					fOut.write(buffering1);
					fOut.flush();
					fOut.getFD().sync();	
					
					counter1 = counter1 + readBytes;
				}
				//write to catalog
//				catalog.put(url, lastModified);
				FileOutputStream WTC = new FileOutputStream("catalog.ser");
				ObjectOutputStream WTC1 = new ObjectOutputStream(WTC);
				WTC1.writeObject(catalog);
				WTC1.flush();
				WTC1.close();
			}
			
			inputstream.close();
			request.close();

			connect.close();
			
		} catch(IOException e){
			System.out.print("Error: " + e.getMessage());
		}
			
	}
	
    /**
     * Returns the Last-Modified time associated with the object specified by the parameter url.
	 *
     * @param url 	URL of the object 
	 * @return the Last-Modified time in millisecond as in Date.getTime()
     */
	public long getLastModified(String url) throws RuntimeException {
		
//		if(catalog.containsKey(url)){ //if object is in catalog
			String lastModified1 = "Thu, 01 Jan 1970 1:00:00 GMT"; // url goes here
			SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz");
			Date date = format.parse(lastModified1, new ParsePosition(0));
			long millis = date.getTime();
			return millis;
			
//		} else {
//			throw new RuntimeException();
//		}
	}

}
