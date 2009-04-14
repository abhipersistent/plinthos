package org.plinthos.core.persistence.txn;

public interface TxAction<T> {

	public T run();
	
}
