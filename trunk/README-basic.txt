Steps to build and run PlinthOS server

1 Open ./build/build-common.properties file and modify 'deploy.home' property 
to point to location where you want plinthos server and samples installed.

2. Copy ./nodes/node1/basic-node.properties  to ./nodes/node1/node.properties

3 Run 'ant deploy' build from ./plinthos/build/

4 Run 'ant deploy' build from ./plinthos-samples/StatisticsService/build/

5. Start PlinthOS server 

Go to %deploy.home%/server/ directory and start server with 'run.bat node1'

6 Run example

Go to %deploy.home%/samples/StatisticsService directory and 
submit request to PlinthOS server with 'run-client.bat http 1' 

