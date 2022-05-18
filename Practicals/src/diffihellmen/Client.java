package diffihellmen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static final String SERVER = "127.0.0.1";
	public static final int PORT = 8080;
	
	public static void main(String[] args) throws Exception{
	
		Scanner scan = new Scanner(System.in);
		
		System.out.println("--------------------- Client Side ---------------------");
		System.out.println("Connection Established.......");
		
		Socket socket = new Socket(SERVER, PORT);
		
		// creating the streams for data to send
		
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		
		// choosing the two prime no
		System.out.print("Enter the Prime No. g : ");
		int g = Integer.parseInt(scan.nextLine());
		
		System.out.print("Enter the Prime No. p : ");
		int p = Integer.parseInt(scan.nextLine());	
		
		System.out.print("Enter the Secret No. a : ");
		int a = Integer.parseInt(scan.nextLine());
		
		// calulating the A to send to server
		
		int A = computeSharableValue(g, p, a);
		
		System.out.println("Sending values g, p, A : " + g +", " + p +", " + A);
		
		// sending the value of g and p to server	
		dos.writeInt(g);
		dos.writeInt(p);
		dos.writeInt(A);
		dos.flush();
		
		// getting the value from  the server
		int B = dis.readInt();
		System.out.println("Server Sended : " + B);

		// calculating the shared key
		
		int sharedKey = computeSharableValue(B, p, a);
		System.out.println("Shared key : " + sharedKey);
		
		// closing the all streams
		dis.close();
		dos.close();
		socket.close();
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
