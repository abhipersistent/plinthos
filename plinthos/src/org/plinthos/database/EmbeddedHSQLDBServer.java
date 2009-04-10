package org.plinthos.database;

import java.io.PrintWriter;

import org.hsqldb.Server;

public class EmbeddedHSQLDBServer {

	//TODO: get data from PlinthosConfiguration
	
    String  serverProps;
    String  url;
    String  user     = "sa";
    String  password = "";
    Server  server;
    boolean isNetwork = true;

    private static EmbeddedHSQLDBServer dbServer = new EmbeddedHSQLDBServer();
    
    public static EmbeddedHSQLDBServer getInstance() {
    	return dbServer;
    }
    
    private EmbeddedHSQLDBServer() {
    	
    }
    
    private EmbeddedHSQLDBServer(String name, String connectionUrl, boolean network) {
        this.isNetwork = network;
        this.url       = connectionUrl;
    }

    public void start() {

        if (url == null) {
            url = "jdbc:hsqldb:hsql://localhost/test";
        }

        server = new Server();

        server.setDatabaseName(0, "test");
        server.setDatabasePath(0, "mem:test;sql.enforce_strict_size=true");
        server.setLogWriter(new PrintWriter(System.out));
        server.setErrWriter(new PrintWriter(System.out));
        server.setTrace(false);
        server.setSilent(true);
        
        server.start();
        System.out.println("started db server");
//        } else {
//            if (url == null) {
//                url = "jdbc:hsqldb:file:test;sql.enforce_strict_size=true";
//            }
//        }
    }
    
    public void stop() {
        server.stop();
        server = null;
    }

	
}
