package bitiodine.domain.service;

import bitiodine.domain.model.Block;

public interface BlockLocalService {

	Block getOrCreateBlock(String blockHash);

	Block getFirst();

	Block getOrCreateBlock(String blockHash, Long timestamp);

}
