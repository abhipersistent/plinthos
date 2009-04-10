package org.plinthos.shared.plugin;

public interface PlinthosTaskContext {

	boolean isTaskCancelled();
	
	void setProgressMessage(String msg);
	
	void setResults(String data);
	
	void setStatusMessage(String msg);
	
	String getStatusMessage();	
}
