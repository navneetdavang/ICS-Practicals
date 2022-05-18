package rsa;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	
	
	public static void main(String[] args) throws Exception {
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("-------------------- Server Side --------------------");
		
		ServerSocket serverSocket = new ServerSocket(8080);
		System.out.println("Waiting for the client to accept the conncetion on PORT:" + serverSocket.getLocalPort() + "....");
		Socket socket = serverSocket.accept();
		
		System.out.println("Client Accepted Connection on SERVER:" + serverSocket.getInetAddress() + " PORT:" + serverSocket.getLocalPort());

		System.out.println("-----------------------------------------------------");
		
		// creating the input & output streams for socket
		
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		
		// getting the public key from the client
		int n = dis.readInt();
		int e = dis.readInt();
		
		
		// take a integer to encrypt
		System.out.print("Enter the Message (Integer) to send : ");
		int msg = Integer.parseInt(scan.nextLine());
		
		int cihper = (int)encryptMsg(msg, n, e);
		
		// sending the encrypted msg to the client
		dos.writeInt(cihper);
		dos.flush();
		
		// closing all the streams
		dos.close();
		dis.close();
		socket.close();
		serverSocket.close();

	}
	
	public static double encryptMsg(int msg, int n, int e) {
		
		double cipher = 0;
		
		cipher = Math.pow(msg, e) % n;
		
		return cipher;
	}

}
