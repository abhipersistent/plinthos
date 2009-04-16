set PLINTHOS_DIR=.

set PLINTHOS_NODE_DIR=%PLINTHOS_DIR%/nodes/%1

set CLASSPATH=

set CLASSPATH=%PLINTHOS_DIR%\PlinthOS.jar;%CLASSPATH%

set CLASSPATH=%PLINTHOS_DIR%\lib\antlr-2.7.6.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\commons-beanutils-1.8.0.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\commons-collections-3.1.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\commons-logging-api.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\commons-logging.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\dom4j-1.6.1.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\hibernate-annotations.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\hibernate3.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\hsqldb.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\javassist-3.4.GA.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\jetty-6.1.15.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\jetty-util-6.1.15.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\jta-1.1.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\log4j.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\mysql-connector-java-5.1.7-bin.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\servlet-api-2.5-20081211.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\slf4j-api-1.5.6.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\slf4j-log4j12-1.5.6.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\xpp3_min-1.1.4c.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\xstream-1.3.1.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\commons-io-1.4.jar;%CLASSPATH%

java -cp %CLASSPATH% -Dplinthos.dir=%PLINTHOS_DIR% -Dlog4j.configuration=%PLINTHOS_NODE_DIR%/log4j.xml -Dnode.properties=%PLINTHOS_NODE_DIR%/node.properties org.plinthos.application.PlinthOSApp
