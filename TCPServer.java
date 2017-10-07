import java.io.*;
import java.net.*;
import java.util.*;

/**
 * A Simple Echo Server
 */

public class TCPServer {

	public static void main(String[] args) {

		/* Sets up input/output stream for the serverSocket. */
		String s;
		Scanner inputStream;
		PrintWriter outputStream;
		ServerSocket serverSocket;


		try {
			/* 
				Server accepts connection from port 8888. 
			*/
			serverSocket = new ServerSocket(8888);
			
			Socket socket = serverSocket.accept();
			
			/* 
				Socket stream ==> byte stream ==> text stream.
				inputStream: sequence of data (in form of text) coming from the client.
				outputStream: sequence of data (in form of text) from the server to the client. 
			*/
			outputStream = new PrintWriter(new DataOutputStream(socket.getOutputStream()));
			inputStream = new Scanner(new InputStreamReader(socket.getInputStream()));

			/*
				Constantly checking the input stream for input text from the client.  
			*/
			while (true) {

				/* Gets the next line of the input and print it out. */
				s = inputStream.nextLine();
				System.out.println(s);

				/* If the input text is bye, the loop terminates and the server is down. */
				if (s.equalsIgnoreCase("bye")) {
					outputStream.println("bye");
					outputStream.flush();
					break;
				}

				/* Sends the reply back to the client. */
				outputStream.println(s);


				/* 
					Q: Why do you need to call flush on the output stream ? 
					When you write to an output stream, the data is not written 
					immediately to the stream but it gets buffered. Once the buffer is full, 
					it empties its content to the stream. This is done to improve the I/O performance.
					By calling flush, all of the buffered data are forced to be written out. 
				*/
				outputStream.flush();
			}

			/* Cloese the server input and output stream. */
			inputStream.close();
			outputStream.close();
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Error: " + e.getMessage());
		}
	}
}

