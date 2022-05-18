package diffihellmen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	
	public static final int PORT = 8080;
	
	public static void main(String[] args) throws Exception{
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("--------------------- Server Side ---------------------");
		
		ServerSocket server = new ServerSocket(PORT);
		System.out.println("Waiting for the Client to Accept Connection on PORT:" + server.getLocalPort() + ".....");
		
		Socket socket = (Socket)server.accept();
		System.out.println("Client to Accepted Connection on PORT:" + server.getLocalPort());
		
		// data input and output stream over socket
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		
		// reading values send by client
		
		int g = dis.readInt();
		int p = dis.readInt();
		int A = dis.readInt();
		
		System.out.println("Client Sended Values of g, p, A : " + g +", " + p +", " + A);
		
		System.out.print("Enter the Secret No. b : ");
		int b = Integer.parseInt(scan.nextLine());
		
		// calulating the A to send to client
		
		int B = computeSharableValue(g, p, b);
		
		// sending the value of B to client
		dos.writeInt(B);
		dos.flush();
		
		// calculating the shared key

		int sharedKey = computeSharableValue(A, p, b);
		System.out.println("Shared key : " + sharedKey);
				
		
		// closing the all streams
		dis.close();
		dos.close();
		socket.close();
		server.close();
		scan.close();
		System.out.println("Closed the Connection...");
	}
	
	// method to calculate the transferable data
	public static int computeSharableValue(int g, int p, int secret) {
		
		BigInteger value = BigInteger.valueOf(g).pow(secret)
				.mod(BigInteger.valueOf(p));
		
		return value.intValue();
	}


}
