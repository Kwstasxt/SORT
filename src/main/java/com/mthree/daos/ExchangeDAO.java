package com.mthree.daos;

public interface ExchangeDAO {
	
	void buyOrder();
	void sellOrder();
	void acceptOrder(); // TODO: should this be here?
	void sliceOrderSendToAlgo(); // TODO: should this be here?

}
