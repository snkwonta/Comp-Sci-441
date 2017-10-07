import java.io.*;
import java.net.*;
import java.util.*;


public class TCPClient {

	public static void main(String[] args){

		String s, tmp;
		Scanner inputStream; 
		PrintWriter outputStream; 
		Scanner userinput;

		try {
			
			/* 
			Connects to a server application at port 8888. The server in this case is
			the same machine as the client.
			Each socket is associated to a port number, which helps Transport layer to identify which
			application to forward the message to.
			When selecting port number, you should note that port numbers between 0 and 1023 are preserved 
			for priviledge users. 
			Portnumber is an unsigned 16-bit. The maximum port number 2^16 - 1 = 65535.
			*/
			Socket socket = new Socket("localhost", 8888); 



			/* 
			   Q: What is a stream ?
			   A: stream is a sequence of data. Output stream is used for writing data to a destination. InputStream is used to
			   read data from a source.
			*/

			/* PrintWriter prints formatted representations of objects to a text-output stream. */
			/* DataOutputStream creates a new data output stream to write data to the specified underlying output stream. */
			outputStream = new PrintWriter(new DataOutputStream(socket.getOutputStream()));


			/* Scanner breaks input into tokens using white spaces. */
			/* InputStreamReader reads byte stream and decode them into character stream.*/
			inputStream = new Scanner(new InputStreamReader(socket.getInputStream()));


			/* System.in: Standard system input stream. */
			/* Scanner breaks user input into tokens. */
			userinput = new Scanner(System.in);

			/* Constantly checking the user input and send it to the server. */
			while (true) {
				System.out.println("Enter Text Message: ");

				/* Gets user input. */
				tmp = userinput.nextLine();

				/* Send the user input out the server. */
				outputStream.println(tmp);
				outputStream.flush();

				/* Receives the string sent from the server by reading socket input stream. */
				s = inputStream.nextLine();
				System.out.println(s);


				/* If server sends back a bye, escape the loop. */
				if (s.equalsIgnoreCase("bye")) {
					break;
				}
			}


			/* Close input and output stream. */
			inputStream.close();
			outputStream.close();
		}

		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	/* 
		Q: Why do you need to call flush on the output stream ? 
		When you write to an output stream, the data is not written 
		immediately to the stream but it gets buffered. Once the buffer is full, 
		it empties its content to the stream. This is done to reduce the number of 
		writes needed, therefore improving the I/O performance.
		By calling flush, all of the buffered data are forced to be written out. 
	*/


}