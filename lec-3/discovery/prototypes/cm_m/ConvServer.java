/******************************************************************************
 *
 *  CS 6421 - Simple Conversation
 *  Compilation:  javac ConvServer.java
 *  Execution:    java ConvServer port
 *
 *  % java ConvServer portnum
 ******************************************************************************/

import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
//import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConvServer {
	
    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        /* Write a welcome message to the client */
        out.println("Welcome, you are connected to a Java-based server");

        /* read and print the client's request */
        // readLine() blocks until the server receives a new line from client
        String userInput;
        if ((userInput = in.readLine()) == null) {
            System.out.println("Error reading message");
            out.close();
            in.close();
            clientSocket.close();
        }

        System.out.println("Received message: " + userInput);
        //--TODO: add your converting functions here, msg = func(userInput);
        if(userInput.equalsIgnoreCase("test")){
            return;
        }
        out.println(inchToCm(userInput));
        // close IO streams, then socket
        out.close();
        in.close();
        clientSocket.close();
    }
    public static String inchToCm(String userInput){
    	String res="";
    	String[] arrs=userInput.split(" ");
    	if(arrs.length!=3){return res;}
    	if(arrs[0].equalsIgnoreCase("m")&&arrs[1].equalsIgnoreCase("cm")){
    		res=""+Double.parseDouble(arrs[2])/100;
    	}
    	if(arrs[1].equalsIgnoreCase("m")&&arrs[0].equalsIgnoreCase("cm")){
    		res=""+Double.parseDouble(arrs[2])*100;
    	}
    	return res;
    }
    public static void remove(String ip,int port)throws IOException{
    	Socket socket=null;
		PrintWriter out=null;
    	try{
            socket = new Socket("127.0.0.1", 5555);
            out = new PrintWriter(socket.getOutputStream(), true);
            //out.println();
            out.println("remove "+ip+" "+port);}catch(Exception e){
                System.out.println("argument should be like:"
                		+ " remove  {My IP} {My Port}");
            }finally{
            	out.close();
            	socket.close();
            	}
    }
	public static void register(String ip,int port) throws IOException{
		Socket socket=null;
		PrintWriter out=null;
    	try{
            socket = new Socket("127.0.0.1", 5555);
            out = new PrintWriter(socket.getOutputStream(), true);
            //out.println();
            out.println("add m cm "+ip+" "+port);}catch(Exception e){
                System.out.println("argument should be like:"
                		+ " add { input unit } { output unit }  {My IP} {My Port}");
            }finally{
            	out.close();
            	socket.close();
            	}
    }
    
    
    public static void main(String[] args) throws Exception {

        //check if argument length is invalid
        if(args.length != 1) {
            System.err.println("Usage: java ConvServer port");
        }
        // create socket
        int port = Integer.parseInt(args[0]);
        InetAddress addr = InetAddress.getLocalHost();
        String ip = addr.getHostAddress();
        register(ip,port);
        
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Started server on port " + port);

        // wait for connections, and process
        try {
            while(true) {
                // a "blocking" call which waits until a connection is requested
                Socket clientSocket = serverSocket.accept();
                System.err.println("\nAccepted connection from client");
                process(clientSocket);
            }

        }catch (IOException e) {
            System.err.println("Connection Error");
        }finally{
        	remove(ip,port);
        }
        System.exit(0);
    }
}
