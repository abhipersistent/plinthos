____________________________________________________________________________________________

  Build and run the PlinthOS server and a sample service
____________________________________________________________________________________________

PREREQUISITES:

   JDK (1.5 or later) installed
   
   Apache Ant (1.7.1 or later) installed  

   -----------------------------------------------------------------------------   
   (OPTIONAL) MySQL or any other relational DB for which you have a JDBC driver
   -----------------------------------------------------------------------------
____________________________________________________________________________________________

1. Open ./build/build-common.properties file and modify the values of the 'plinthos.home' 
   and 'plinthos.deploy' properties, so that they refer to the location where you installed
   the source code and the location that you want to install the plinthos server and the 
   sample application, respectively.
____________________________________________________________________________________________

2. Copy ./nodes/node1/basic-node.properties to ./nodes/node1/node.properties
   and modify the property values as needed. For example, the type of the database that the
   node should use, the port that it should listen to for the HTTP requests, etc.
____________________________________________________________________________________________

3. In order to build the PlinthOS server, invoke the 'ant' build script (default target is 
   'deploy') from the following directory: 

   ./plinthos/build/
____________________________________________________________________________________________

4. In order to build the sample Statistics service, invoke the 'ant' build script (default 
   target is 'deploy') from the following directory:

   ./plinthos-samples/StatisticsService/build/
____________________________________________________________________________________________

5. Start the PlinthOS server 

   a. Go to %plinthos.deploy%/server/ directory
   
   b. Execute the following:
    
         'run.bat node1'
         
      to start the PlinthOS server; an instance of the PlinthOS server to be precise.
____________________________________________________________________________________________      

6. Call the sample service

   a. Go to %plinthos.deploy%/samples/StatisticsService directory 
    
   b. Execute the following:
    
       'run-client.bat http 5'
       
      This command submits 5 requests to the StatisticsService for calculating some basic
      statistical properties of ten random numbers.  


