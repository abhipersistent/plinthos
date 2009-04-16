package org.plinthos.core.bootstrap.environment;

public class PlinthosNodePropertyNames {
	
	private PlinthosNodePropertyNames() {
		// empty
	}
	
	public static final String DB_TYPE = "plinthos.node.db.type";
	public static final String HTTP_PORT = "plinthos.node.http.transport.port";
	public static final String JDBC_URL = "plinthos.node.jdbc.connection.url";
	public static final String TASK_CLASSPATH = "plinthos.node.task.classpath";
	public static final String USE_EMBEDDED_DB_SERVER = "plinthos.node.use.embedded.db.server";
	public static final String USE_EMBEDDED_HTTP_SERVER = "plinthos.node.use.embedded.http.server";
}
