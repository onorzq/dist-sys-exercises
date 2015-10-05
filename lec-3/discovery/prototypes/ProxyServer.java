/******************************************************************************
 *
 *  CS 6421 - Simple Conversation
 *  Compilation:  javac ProxyServer.java
 *  Execution:    java ProxyServer port
 *                e.g. java ProxyServer 5554
 *
 *  % java ConvServer portnum
 ******************************************************************************/

import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Scanner;
import java.util.HashMap;

public class ProxyServer {
    private static String[] convServerIp = {
    "54.172.68.226","54.172.68.226","54.172.68.226"
    }; // ip address of conversion server
    private static int[] convServerPort = {5555, 5556, 5557}; // port number of conversion server
    private static HashMap<String, Integer> mapUnitPair2ConvServer = new HashMap<String, Integer>(){
    {
    put("inft",0);
    put("ftin",0);
    put("incm",1);
    put("cmin",1);
    put("cmm",2);
    put("mcm",2);
}
}; // mapping base unit conversion to cooresponding conversion server

public static void process (Socket clientSocket) throws IOException {
	// open up IO streams
	BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	
	/* Write a welcome message to the client */
	out.println("Welcome, you are connected to a Java-based proxy server\n" +
	"you can convert the following unit ft, in, cm or m into\n" +
	"each other with input ammount, e.g. ft m 100 or m cm 1 etc.");
	
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
	out.println(proxyConvert(userInput));
	// close IO streams, then socket
	out.close();
	in.close();
	clientSocket.close();
}

/** call different conversion server to do unit conversion
 *
 * @param userInput		        user input from command line
 * @return			         	``	   final result ofter conversion
 */
public static String proxyConvert(String userInput) throws IOException{
	String[] arrs = userInput.split(" ");
	String[] unitList = {"ft", "in", "cm", "m"}; // must be line in the order of ft->in->cm->m or m->cm->in->ft
	
	if(arrs.length != 3) {
		return "please input as following format, <input unit> <output unit> <input ammount> " +
	"for example, cm m 100 or ft m 100";
	}
	if(indexOfStringArray(unitList, arrs[0]) == -1) {
		return "please input the input unit as one of the following unit: ft, in, cm or m";
	}
	if(indexOfStringArray(unitList, arrs[1]) == -1) {
		return "please input the output unit as one of the following unit: ft, in, cm or m";
	}
	if(arrs[0].equalsIgnoreCase(arrs[1])) {
		return "the input unit and output unit cannot be the same";
	}
	try {
	if(Float.parseFloat(arrs[2]) < 0)
		return "please input a number larger than zero";
	} catch(NumberFormatException e) {
		return "please input a valid number";
	}
	
	int inputUnitIndex = indexOfStringArray(unitList, arrs[0]);
	int outputUnitIndex = indexOfStringArray(unitList, arrs[1]);
	
	return unitConversion(unitList, inputUnitIndex, outputUnitIndex, arrs[2]);
}

/** recurrsively calculate the result of unit conversion
 *
 * @param unitList		        array of units avalible for converting
 * @param inputUnitIndex		index number of inputUnit in unitList
 * @param outputUnitIndex		index number of outputUnit in unitList
 * @param inputAmount		    amount to be converted
 * @return			            final result ofter conversion
 */
public static String unitConversion(String[] unitList,
	int inputUnitIndex,
	int outputUnitIndex,
	String inputAmount)  throws IOException{
	String inputUnit = unitList[inputUnitIndex];
	String outputAmount = inputAmount;
	
	/* determine the direction of recursion chain */
	if(inputUnitIndex - outputUnitIndex > 1) { // from m -> cm -> in -> ft
		outputAmount = unitConversion(unitList, inputUnitIndex, (outputUnitIndex + 1), inputAmount);
		inputUnit = unitList[outputUnitIndex + 1];
	}
	if(outputUnitIndex - inputUnitIndex > 1) { // reverse, ft->m
		outputAmount = unitConversion(unitList, inputUnitIndex, (outputUnitIndex - 1), inputAmount);
		inputUnit = unitList[outputUnitIndex - 1];
	}

	return unitConversionCalculation(inputUnit, unitList[outputUnitIndex], outputAmount);
}

/** call the conversion server to do unit conversion
 *
 * @param inputUnit			inputUnit string e.g. in
 * @param outputUnit		outputUnit string e.g. ft
 * @return			        direct result ofter conversion
 */
public static String unitConversionCalculation(String inputUnit,
	String outputUnit,
	String inputAmount) throws IOException{
	String ip = "";
	int port = 0;
	int index = mapUnitPair2ConvServer.get(inputUnit + outputUnit);
	
	ip = convServerIp[index];
	port = convServerPort[index];
	try{
	Socket socket = new Socket(ip, port);
	PrintWriter write2Socket = new PrintWriter(socket.getOutputStream(), true);
	write2Socket.println(inputUnit + " " + outputUnit + " " + inputAmount);
	
	String result = convertStreamToString(socket.getInputStream());
	socket.close();

	return result;
//.split("\n")[1];
    
	}catch(Exception e){
    	e.printStackTrace();
    	remove(thisIp,thisPort);
    	return "";
	}
}

/** convert InputStream to String
 *
 * @param is		InputStream
 * @return			String
 */
public static String convertStreamToString(InputStream is) {
	Scanner s = new Scanner(is).useDelimiter("\\A");
	//System.out.println();
	return s.hasNext() ? s.next() : "";
}

/** find index number of an object in a String array
 *
 * @param array			target String array
 * @param target		target String object
 * @return			    index number of the object, return -1 if not found
 */
public static int indexOfStringArray(String[] array, String target) {
	for(int i = 0; i < array.length; i++) {
		if(array[i].equalsIgnoreCase(target))
		return i;
	}
	return -1;
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


public static boolean register(String ip,int port){
	try{
		Socket socket = new Socket("127.0.0.1", 5555);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		//out.println();
		out.println("lookup in ft");
		String s=in.readLine();
		System.out.println("s:"+s);
		boolean flag=true;
	if(!s.equalsIgnoreCase("failure")){
		String[] temp=s.split(" ");
		convServerIp[0]=temp[0];
		convServerPort[0]=Integer.parseInt(temp[1]);
	}else{flag=false;}
    socket.close();
	socket = new Socket("127.0.0.1", 5555);
	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	out = new PrintWriter(socket.getOutputStream(), true);
	out.println("lookup in cm");
	s=in.readLine();
	System.out.println("s:"+s);
	if(!s.equalsIgnoreCase("failure")){
		String[] temp=s.split(" ");
		convServerIp[1]=temp[0];
		convServerPort[1]=Integer.parseInt(temp[1]);
	}else{flag=false;}
    socket.close();
	socket = new Socket("127.0.0.1", 5555);
	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	out = new PrintWriter(socket.getOutputStream(), true);
	out.println("lookup cm m");
	s=in.readLine();
	System.out.println("s:"+s);
	if(!s.equalsIgnoreCase("failure")){
	String[] temp=s.split(" ");
	convServerIp[2]=temp[0];
	convServerPort[2]=Integer.parseInt(temp[1]);
	}else{flag=false;}
	socket.close();
	//next step: verify all server
	if(flag){
	try{
		socket = new Socket("127.0.0.1", 5555);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		out.println("add ft m "+ip+" "+port);}catch(Exception e){
		System.out.println("argument should be like:"
		+ " add { input unit } { output unit }  {My IP} {My Port}");
	
	}
	}else{return false;}
	}catch(Exception e){
	e.printStackTrace();
		return false;
	}
		return true;
	}
	static String thisIp="";
	static int thisPort=0;

	public static void main(String[] args) throws Exception {
	//check if argument length is invalid
	if(args.length != 1) {
	System.err.println("Usage: java ConvServer port");
	}
	// create socket
	 thisPort = Integer.parseInt(args[0]);
	InetAddress addr = InetAddress.getLocalHost();
	 thisIp = addr.getHostAddress();
	if(!register(thisIp,thisPort)){
	System.out.println("connection error");
	System.exit(0);}
	ServerSocket serverSocket = new ServerSocket(thisPort);
	System.err.println("Started server on port " + thisPort);
	
	// wait for connections, and process
	try {
    	while(true) {
	// a "blocking" call which waits until a connection is requested
	        Socket clientSocket = serverSocket.accept();
	        System.err.println("\nAccepted connection from client");
	        process(clientSocket);
    	}
	}catch (IOException e) {
	    e.printStackTrace();
	}finally{
	    System.out.println("remove proxy server");
		remove(thisIp,thisPort);
	}
	System.exit(0);
	}
}
