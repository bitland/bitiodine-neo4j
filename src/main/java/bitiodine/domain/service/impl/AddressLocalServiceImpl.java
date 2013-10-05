package bitiodine.domain.service.impl;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import bitiodine.domain.model.Address;
import bitiodine.domain.model.NodeTypes;
import bitiodine.domain.service.AddressLocalService;

public class AddressLocalServiceImpl implements AddressLocalService {
	
	public AddressLocalServiceImpl(GraphDatabaseService graphDb) {
		this.graphDb=graphDb;
	}

	@Override
	public Address getAddress(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address updateAddress(Address address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address deleteAddress(Address address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address addAddress(Address address) {  	
		Address a = null;
		try ( Transaction tx = graphDb.beginTx() ) {
    		// Create node
			//TODO Check if the address is already present
    		Node n = graphDb.createNode();
    		n.addLabel(NodeTypes.ADDRESS);
    		n.setProperty("address", address.getAddress());
    		a = new Address(n);
    		tx.success();
        }
		catch(Exception e){
			e.printStackTrace();
		}
		return a;
	}
	
	private GraphDatabaseService graphDb = null;


}
