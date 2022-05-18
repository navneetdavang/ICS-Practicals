package rsa;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	public static final String SERVER = "127.0.0.1";
	public static final int PORT = 8080;
	
	public static void main(String[] args) throws Exception {
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("-------------------- Client Side --------------------");
		
		Socket socket = new Socket(SERVER, PORT);
		
		// creating the input & output streams for socket
		
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		
		// take p and q from user		
		
		System.out.print("Enter the Prime No. P : ");
		int p = Integer.parseInt(scan.nextLine());
		
		System.out.print("Enter the Prime No. Q : ");
		int q = Integer.parseInt(scan.nextLine());
		
		// calculating the n value
		int n = p * q;
		
		// calculating the phi value
		int phi = (p-1) * (q-1);
		
		System.out.println("PHI : " + phi);
		
		
		// getting the encryption key {n, e}
		
		int e = getPublicKeyComponent(phi);
		
		// Clients Public key is given as : {n, e}
		System.out.println("Public key : {" + n + ", " + e + "}");
		
		
		// sending the public key to the server
		dos.writeInt(n);
		dos.writeInt(e);
		dos.flush();
		
		
		// getting the cihper text msg from the serve
		double cipher = dis.readInt();
		
		System.out.println("Cipher Message from the Server : " + cipher);
		
		// generating the private key : {n, d}
		double d = getPrivateKeyComponent(e, phi);
		
		// Clients Private key is given as : {n, d}
		System.out.println("Private key : {" + n + ", " + d + "}");
		
		
		// decrypting the cipher
		int message = decryptCipher(cipher, n, d);
		
		System.out.println("Decrypted Message : " + message);
		
		
		// closing all the streams
		dos.close();
		dis.close();
		socket.close();
		
	
	}
	
	// function to calculate the GCD of given two no
	public static int GCD(int a, int b) {
		if(b == 0) 
			return a;
		return GCD(b, a % b);
	}
	
	
	// Method to calculate the public keys e component
	public static int getPublicKeyComponent(int phi) {
		
		int e = 2;
		
		while(e < phi) {
			
//			System.out.println("GCD of e and phi : "+ e + " <-> " + phi + " : " + GCD(e, phi));
			
			if(GCD(e, phi) == 1) 
				break;
			
			e++;
		}
		
		return e;
	}
	
	
	/*
	 * e can calculate the value of d.

		The value of d should be such that (d * e) mod Ø should be equal to 1.
		
		i.e., (d * e) mod ø = 1
		
		hence, here we have:
		
		
		(d * 13) mod 60 = 1
		
		d = (k * Ø + 1)/e, for some integer k.
		
		For k = 8, value of d,
		
		d = (8 * 60 + 1)/13
		
		d = 37
	 * 
	 */
	// Method to calculate the private keys d component
	public static int getPrivateKeyComponent(int e, int phi) {
		
		int k = 1;
		double d = ((double)(k * phi) / (double)e);
		
		while(Math.floor(d) != d) {
			k++;
			d = (double)(1 + (k * phi)) / (double)e;
		}
		
		return (int)d;
	}
	
	// Method to decrypt the given cipher 
	public static int decryptCipher(double cipher, int n, double d) {
		
		BigInteger msg = BigInteger.valueOf((long) cipher).pow((int)d);
		msg = msg.mod(BigInteger.valueOf(n));
		
		return msg.intValue();
	}
}
