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
set CLASSPATH=%PLINTHOS_DIR%\lib\hibernate3.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\c3p0-0.9.1.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\hsqldb.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\javassist-3.4.GA.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\jetty-http-9.0.6.v20130930.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\jetty-io-9.0.6.v20130930.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\jetty-server-9.0.6.v20130930.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\jetty-servlet-9.0.6.v20130930.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\jetty-servlets-9.0.6.v20130930.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\jetty-util-9.0.6.v20130930.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\jta-1.1.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\log4j.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\mysql-connector-java-5.1.7-bin.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\servlet-api-3.0.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\slf4j-api-1.7.5.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\slf4j-log4j12-1.7.5.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\xmlpull-1.1.3.1.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\xpp3_min-1.1.4c.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\xstream-1.4.5.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\commons-io-1.4.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\commons-codec-1.5.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\commons-lang3-3.1.jar;%CLASSPATH%
set CLASSPATH=%PLINTHOS_DIR%\lib\hibernate-jpa-2.0-api-1.0.1.Final.jar;%CLASSPATH%

set JPDA=
@rem set JPDA=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8788,server=y,suspend=n

java -cp %CLASSPATH% %JPDA% -Dplinthos.dir=%PLINTHOS_DIR% -Dlog4j.configuration=%PLINTHOS_NODE_DIR%/log4j.xml -Dnode.properties=%PLINTHOS_NODE_DIR%/node.properties org.plinthos.application.PlinthOSApp
