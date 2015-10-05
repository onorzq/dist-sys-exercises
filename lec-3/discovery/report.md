Group member <br/>
-------------
protocol designer: Cong Sun, Te Lin <br/>
programmer: Xinchao Jiang, Zhisheng Liu, Lihao Li, Qi Ji <br/>
tester: Xinchao Jiang, Zhisheng Liu <br/>
________________________________________________________________________________________________________________________________
A brief summary of the capabilities of your system <br/>
-------------
The discovery server supports the ADD, DELETE and LOOKUP protocol discussed in class.<br/>
The ConvServer and Proxy Server will automatically register to the discovery Server.<br/>
If any one of the ConvServer which is used by the Proxy Server is down, the Proxy Server will return ERROR.<br/>

A protocol specification that clearly explains the protocol used by your system. <br/>
-------------
we can open a terminal and telnet 127.0.0.1 5555 to connect the discover server. The ip address and port number of discover server is hardcoded inside the code.<br/>
________________________________________________________________________________________________________________________________
On the client side,<br/>
(1) Use "lookup [unit1] [unit2] \n" to check the ip and port number for the convServer or ProxyServer<br/>
(2) Then use that ip and port number to send the actual request<br/>

On the ConvServer and Proxy Server Side<br/>
(1) Once the server is running, it will automatically register on the Discovery Server. You can also use "add [unit1] [unit2] 127.0.0.1 [port number]" on Discovery Server to add new server. <br/>
(2) If the server is shut down, it will send a REMOVE request to the discovery server to remove itself.<br/>

If you have extended the protocol in some way, explain how <br/>
-------------
To be Edited
A test plan explaining what commands the TA must execute in order to compile and test your program.
-------------
use "javac [fileName]" to compile all the code <br/>
(1)Launch Discovery server first using "java DisServer 5555" <br/>
(2)Launch 3 ConvServer "java ConvServer [port number]" <br/>
(3)Finally Launch ProxyServer (The proxy server will check all the connected ConvServer and register to the discovery server) using "java ProxyServer [port number]" <br/>
(4) use terminal to connect to the discovery server and test "telnet 127.0.0.1 5555"
