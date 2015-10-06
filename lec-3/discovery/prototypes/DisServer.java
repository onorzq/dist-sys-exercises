import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class DisServer {
	ArrayList<Record> recordArray=new ArrayList<Record>();
	int portNum=0;
	ServerSocket serverSocket;
	public void start(){
		ini();
		try {
            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("\nAccepted connection from client");
                process(clientSocket);
                clientSocket.close();
            }

        }catch (IOException e) {
            System.err.println("Connection Error");
        }
        System.exit(0);
		
	}
	
	public void ini(){
		try {
			serverSocket = new ServerSocket(portNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.err.println("Started server on port " + portNum);
        }
	
	public void process(Socket client) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        String instruction=in.readLine();
        System.out.println(instruction);
        String[] temp=instruction.split(" ");
    	if(temp.length!=3&&temp.length!=5){
    		out.println("input format should be: add { input unit } { output unit }  {My IP} {My Port}"
    				+ "\r\nor remove { My IP } { My Port}"
    				+ "\r\nor lookup {input unit} {output unit} ");
    		}
        if(temp[0].equalsIgnoreCase("add")&&temp.length==5){//create a new record and add it into array
        	Record r=new Record();
        	r.from=temp[1];
        	r.to=temp[2];
        	r.ip=temp[3];
        	r.port=temp[4];
        	recordArray.add(r);
        }
        if(temp[0].equalsIgnoreCase("remove")){
        	for(int i=0;i<recordArray.size();i++){
        	    Record r=recordArray.get(i);
        		if(r.ip.equalsIgnoreCase(temp[1])&&r.port.equalsIgnoreCase(temp[2])){
        			recordArray.remove(r);
        			i--;
        		}
        	}
        }
        if(temp[0].equalsIgnoreCase("lookup")){
        	ArrayList<Record> result=new ArrayList<Record>();
        	String res="";
        	for(int ii=0;ii<recordArray.size();ii++){
        	    Record r=recordArray.get(ii);
        		if((r.from.equalsIgnoreCase(temp[1])&&r.to.equalsIgnoreCase(temp[2]))||
        				(r.to.equalsIgnoreCase(temp[1])&&r.from.equalsIgnoreCase(temp[2]))){
        		Socket sc = null;
                        try {
                              	sc = new Socket(r.ip, Integer.parseInt(r.port));
                              	PrintWriter o = new PrintWriter(sc.getOutputStream(), true);
                                o.println("test");
                              	sc.close();
                              	o.close();
                        	result.add(r);            
                           } catch(Exception e) {
                        	e.printStackTrace();
                            	recordArray.remove(r);
                            	ii--;
                               System.out.println(r.to + "have shuted down");
                            }
        		}
        	}
        	if(result.size()!=0){
        	  //  Random rand = new Random();
            //    int  rn = rand.nextInt(result.size());
              //  return result.get(rn).ip+" "+result.get(rn).port;
        		for(Record r:result){//only return first one, need update next week
        		res+=r.ip+" "+r.port;
        			break;
        		}
        	}else{res="failure";}
        	out.println(res);
        }
        
        if(temp[0].equalsIgnoreCase("remove")){
        	ArrayList<Record> result=new ArrayList<Record>();
        	for(Record r:recordArray){
        		if((r.ip.equalsIgnoreCase(temp[1])&&r.port.equalsIgnoreCase(temp[2]))){
        			result.remove(r);
        			break;
        		}
        	}
        }
        
        
	}
	
	
	public DisServer(String[] args){
		if(args.length != 1) {
            System.err.println("Usage: java ConvServer port");
        }
		portNum = Integer.parseInt(args[0]);
        

	}
	
	class Record{
		String from,to,ip,port;
	}
	
	public static void main(String[] args){
		new DisServer(args).start();;
	}
}
