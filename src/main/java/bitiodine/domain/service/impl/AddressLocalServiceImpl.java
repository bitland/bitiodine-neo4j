package bitiodine.domain.service.impl;

import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.UniqueFactory;

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
		try ( Transaction tx = graphDb.beginTx() ) {
		    this.uniqueAddressFactory = new 
		    		UniqueFactory.UniqueNodeFactory( graphDb, "addresses" ) {
						@Override
						protected void initialize(Node created,
								Map<String, Object> properties) {
							created.setProperty( "address", properties.get( "address" ) );	
						}
		    };
		    tx.success();
		}
	}

	@Override
	public Address updateAddress(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address deleteAddress(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address getOrCreateAddress(String address) {  	
		Address a = null;
		try ( Transaction tx = graphDb.beginTx() ) {
    		// get-or-create node
			Node n = uniqueAddressFactory.getOrCreate( "address", address );
    		n.addLabel(NodeTypes.ADDRESS);
    		a = new Address(n);
			tx.success();
		}
		return a;
	}

	@Override
	public Address linkAddressToCluster(String address, String cluster_id) {
		Address a = null;
		try ( Transaction tx = graphDb.beginTx() ) {
			a = AddressLocalServiceUtil.getOrCreateAddress(this.graphDb, address);
			Cluster c = ClusterLocalServiceUtil.getOrCreateCluster(this.graphDb, cluster_id);
			
			Relationship clusterRelationship = a.getUnderlyingNode()
					.getSingleRelationship(RelTypes.CLUSTER,Direction.OUTGOING);
			
			if ( clusterRelationship == null)
				clusterRelationship = a.getUnderlyingNode()
					.createRelationshipTo(c.getUnderlyingNode(), RelTypes.CLUSTER);
			
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
	private UniqueFactory<Node> uniqueAddressFactory = null;
}

