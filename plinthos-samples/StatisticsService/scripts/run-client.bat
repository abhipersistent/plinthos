set CLIENT_HOME_DIR=.

set CLASSPATH=

set CLASSPATH=%CLIENT_HOME_DIR%\statistics-service-client.jar;%CLASSPATH%
set CLASSPATH=%CLIENT_HOME_DIR%\lib\commons-math-1.2.jar;%CLASSPATH%
set CLASSPATH=%CLIENT_HOME_DIR%\lib\log4j.jar;%CLASSPATH%
set CLASSPATH=%CLIENT_HOME_DIR%\lib\mysql-connector-java-5.1.7-bin.jar;%CLASSPATH%
set CLASSPATH=%CLIENT_HOME_DIR%\lib\plinthos-shared.jar;%CLASSPATH%
set CLASSPATH=%CLIENT_HOME_DIR%\lib\xercesImpl.jar;%CLASSPATH%
set CLASSPATH=%CLIENT_HOME_DIR%\lib\xpp3_min-1.1.4c.jar;%CLASSPATH%
set CLASSPATH=%CLIENT_HOME_DIR%\lib\xstream-1.4.5.jar;%CLASSPATH%

java -cp %CLASSPATH% org.plinthos.sample.statistics.client.BasicStatisticsClient %1 %2
