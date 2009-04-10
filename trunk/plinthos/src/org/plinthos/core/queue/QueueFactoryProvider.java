package org.plinthos.core.queue;

import org.plinthos.core.framework.Constants;

public class QueueFactoryProvider {

	public QueueFactoryProvider() {
		
	}
	
	public QueueFactory getQueueFactory() {
		QueueFactory queueFactory = null;
		try {
			Class<?> queueFactoryClass = Class.forName(Constants.QUEUE_FACTORY);
			queueFactory = (QueueFactory)queueFactoryClass.newInstance();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to craete queue factory: ", e);
		}
		return queueFactory;
		
	}
}
