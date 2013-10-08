package bitiodine.domain.service;

import java.util.List;

import bitiodine.domain.model.Transaction;

public interface TransactionLocalService {
	Transaction getOrCreateTransaction(String txHash, List<String> txIns, 
			List<Integer> amountsIn, List<String> txPrevs, List<String> txOuts,
			List<Integer> amountsOut, String block_hash, Long timestamp);

	Transaction getOrCreateTransaction(String txHash);
	
	Transaction getFirst();
}
