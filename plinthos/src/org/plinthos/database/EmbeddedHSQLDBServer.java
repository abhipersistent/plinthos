/*
 * PlinthOS, Open Source Multi-Core and Distributed Computing.
 * Copyright 2003-2009, Emptoris Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
