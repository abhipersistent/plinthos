Steps to build and run PlinthOS server

1 Open ./build/build-common.properties file and modify 'deploy.home' property 
to point to location where you want plinthos server and samples installed.

2 Run 'ant deploy' build from ./plinthos/build/

3 Run 'ant deploy' build from ./plinthos-samples/StatisticsService/build/

4. Start PlinthOS server 

Go to %deploy.home%/server/ directory and start server with 'run.bat'

5 Run example

Go to %deploy.home%/samples/StatisticsService directory and 
submit request to PlinthOS server with 'run-client.bat http 1' 

