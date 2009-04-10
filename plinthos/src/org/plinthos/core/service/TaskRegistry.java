package org.plinthos.core.service;

import java.util.List;

import org.plinthos.core.model.RegisteredTask;

public interface TaskRegistry {

	List<RegisteredTask> findAll();
	
	RegisteredTask findTask(String taskType);
	
	RegisteredTask registerTask(RegisteredTask task);
	
	void initTasks(List<RegisteredTask> tasks);
}
