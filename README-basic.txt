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

1. Open ./build/build-common.properties file and modify the value of the
   'plinthos.deploy' property, so that it refers to the location where you want 
   to install the plinthos server and the sample application.
____________________________________________________________________________________________

2. Copy ./nodes/node1/basic-node.properties to ./nodes/node1/node.properties
   and modify the property values as needed.
____________________________________________________________________________________________

3. Invoke the 'ant' build script (default target is 'deploy') from 

   ./plinthos/build/
____________________________________________________________________________________________

4. Invoke the 'ant' build script (default target is 'deploy') from 

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


