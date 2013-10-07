package bitiodine.domain.service.impl;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import bitiodine.domain.model.Address;
import bitiodine.domain.model.Cluster;
import bitiodine.domain.model.NodeTypes;
import bitiodine.domain.model.RelTypes;
import bitiodine.domain.service.AddressLocalService;
import bitiodine.domain.service.AddressLocalServiceUtil;
import bitiodine.domain.service.ClusterLocalServiceUtil;

public class AddressLocalServiceImpl implements AddressLocalService {
	
	public AddressLocalServiceImpl(GraphDatabaseService graphDb) {
		this.graphDb=graphDb;
	}

	@Override
	public Address updateAddress(Address address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address deleteAddress(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address addAddress(String address) {  	
		Address a = null;
		try ( Transaction tx = graphDb.beginTx() ) {
    		// Create node
			//TODO Check if the address is already present
    		Node n = graphDb.createNode();
    		n.addLabel(NodeTypes.ADDRESS);
    		n.setProperty("address", address);
    		a = new Address(n);
    		tx.success();
        }
		return a;
	}

	@Override
	public Address linkAddressToCluster(String address, String cluster_id) {
		Address a = null;
		try ( Transaction tx = graphDb.beginTx() ) {
			a = AddressLocalServiceUtil.findAddressByAddress(this.graphDb, address);
			Cluster c = ClusterLocalServiceUtil.findClusterById(this.graphDb, cluster_id);
			Relationship clusterRelationship = a.getUnderlyingNode().
					createRelationshipTo(c.getUnderlyingNode(), RelTypes.CLUSTER);
			a.setClusterRelationship(clusterRelationship);
			tx.success();
		}
		return a;
	}

	@Override
	public Address findAddressByAddress(String address) {
		Address a = null;
		try ( Transaction tx = graphDb.beginTx() ) {
			a = new Address (graphDb
					.findNodesByLabelAndProperty(NodeTypes.ADDRESS, "address", address)
					.iterator().next());
			tx.success();
		}
		return a;
	}
	
	private GraphDatabaseService graphDb = null;
}

