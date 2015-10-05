Group member <br/>
protocol designer: Cong Sun, Te Lin <br/>
programmer: Xinchao Jiang, Zhisheng Liu, Lihao Li, Qi Ji <br/>
tester: Xinchao Jiang, Zhisheng Liu <br/>
A brief summary of the capabilities of your system <br/>
The discovery server supports the ADD, DELETE and LOOKUP protocol discussed in class.
The ConvServer and Proxy Server will automatically register to the discovery
If any one of the ConvServer which is used by the Proxy Server is down, the Proxy Server will return ERROR

A protocol specification that clearly explains the protocol used by your system. <br/>

On the client side,<br/>
(1) Use "lookup <unit1> <unit2> \n" to check the ip and port number for the convServer or ProxyServer<br/>
(2) Then use that ip and port number to send the actual request<br/>

On the ConvServer and Proxy Server Side<br/>
(1) Once the server is running, it will automatically register on the Discovery Server using "add <unit1> <unit2> <ip> <port number>"<br/>


If you have extended the protocol in some way, explain how <br/>
Even if you have not changed the protocol at all, you should fully explain how the protocol works. Assume that the reader of the document does not know anything about the discovery protocol discussed in class.
A test plan explaining what commands the TA must execute in order to compile and test your program.
