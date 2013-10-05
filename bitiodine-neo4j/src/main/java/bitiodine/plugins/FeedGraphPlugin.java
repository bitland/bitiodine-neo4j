/**
3 * Licensed to Neo Technology under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo Technology licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package bitiodine.plugins;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.server.plugins.Description;
import org.neo4j.server.plugins.Name;
import org.neo4j.server.plugins.Parameter;
import org.neo4j.server.plugins.PluginTarget;
import org.neo4j.server.plugins.ServerPlugin;
import org.neo4j.server.plugins.Source;

import bitiodine.domain.model.Address;
import bitiodine.domain.model.RelTypes;
import bitiodine.domain.service.AddressLocalService;
import bitiodine.domain.service.impl.AddressLocalServiceImpl;


@Description( "An extension to the Neo4j Server for feeding neo4j graph db" )
public class FeedGraphPlugin extends ServerPlugin
{
	AddressLocalService addressLocalService = null;
	
	public void initServices(GraphDatabaseService graphDb){
		if (addressLocalService == null)
			addressLocalService = new AddressLocalServiceImpl(graphDb);
	}
	
    @Name("addAddressNode")
    @Description("Adds an Address node to graphdb")
    @PluginTarget( GraphDatabaseService.class )
    public Node addVertexToClusterizer(	@Source GraphDatabaseService graphDb,
    							@Description( "The address of the node to add." )
    							@Parameter( name = "address" ) String address) {
    	initServices(graphDb);
    	Address a = addressLocalService.addAddress(new Address(address));
    	System.out.println("addVertexToClusterizer: "+address);
    	return a.getUnderlyingNode();
    }
    
    @Name("addEdgeToClusterizer")
    @Description("dsadsaddasda")
    @PluginTarget( GraphDatabaseService.class )
    public Node addEdgeToClusterizer( @Source GraphDatabaseService graphDb,
    							@Description( "The address of a vertex of the edge." )
								@Parameter( name = "address1" ) String address1,
								@Description( "The address of another vertex of the edge." )
								@Parameter( name = "address2" ) String address2) {
    	System.out.println("addEdgeToClusterizer: "+address1+" "+address2);
    	//Relationship r = null;
    	Node c1;
    	try ( Transaction tx = graphDb.beginTx() ) {
    		Node n1 = graphDb.index().getNodeAutoIndexer().getAutoIndex().get( "address", address1 ).getSingle();
    		//Node n2 = graphDb.index().getNodeAutoIndexer().getAutoIndex().get( "address", address2 ).getSingle();
    		c1 = n1.getRelationships(RelTypes.CLUSTER).iterator().next().getStartNode();
    		//r = n1.createRelationshipTo( n2, BitiodineRelTypes.SAMEOWNER );
    		tx.success();
        }
    	return c1;
    }

}
