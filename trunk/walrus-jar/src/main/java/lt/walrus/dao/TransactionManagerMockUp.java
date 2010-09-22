package lt.walrus.dao;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;

public class TransactionManagerMockUp implements ResourceTransactionManager {

	public Object getResourceFactory() {
		//EMPTY
		return null;
	}

	public void commit(TransactionStatus arg0) throws TransactionException {
		//EMPTY
	}

	public TransactionStatus getTransaction(TransactionDefinition arg0)
			throws TransactionException {
		//EMPTY
		return null;
	}

	public void rollback(TransactionStatus arg0) throws TransactionException {
		//EMPTY
	}

	

}
