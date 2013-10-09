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

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.server.plugins.Description;
import org.neo4j.server.plugins.Name;
import org.neo4j.server.plugins.Parameter;
import org.neo4j.server.plugins.PluginTarget;
import org.neo4j.server.plugins.ServerPlugin;
import org.neo4j.server.plugins.Source;

import bitiodine.domain.model.Address;
import bitiodine.domain.model.Cluster;
import bitiodine.domain.model.Transaction;
import bitiodine.domain.service.AddressLocalServiceUtil;
import bitiodine.domain.service.ClusterLocalServiceUtil;
import bitiodine.domain.service.TransactionLocalServiceUtil;

@Description( "An extension to the Neo4j Server for feeding neo4j graph db. All operations are idempotent." )
public class GraphPluginImpl extends ServerPlugin implements GraphPlugin
{	
	@Override
    @Name("addAddressNode")
    @Description("Adds an Address node to graph db")
    @PluginTarget( GraphDatabaseService.class )
    public Node addAddressNode(	@Source GraphDatabaseService graphDb, 
    		@Description( "The address of the node to add." )
    		@Parameter( name = "address" ) String address) {
    	Address a = AddressLocalServiceUtil.getOrCreateAddress(graphDb, address);
    	return a.getUnderlyingNode();
    }
    
	@Override
    @Name("addClusterNode")
    @Description("Adds a Cluster node to graph db")
    @PluginTarget( GraphDatabaseService.class )
	public Node addClusterNode( @Source GraphDatabaseService graphDb,
			@Description( "The id of the cluster to add." )
			@Parameter( name = "cluster-id" ) String clusterId) {
		Cluster c = ClusterLocalServiceUtil.getOrCreateCluster(graphDb, clusterId);
		return c.getUnderlyingNode();
	}

	@Override
    @Name("linkAddressToCluster")
    @Description("Links an Address node to a Cluster node")
    @PluginTarget( GraphDatabaseService.class )
	public Relationship linkAddressToCluster(@Source GraphDatabaseService graphDb, 
			@Description( "The address to link." )
			@Parameter( name = "address" ) String address, 
			@Description( "The id of the cluster to link to." )
			@Parameter( name = "cluster-id" ) String clusterId) {
		Address a = AddressLocalServiceUtil.linkAddressToCluster(graphDb, address, clusterId);
		return a.getClusterRelationship();
	}
	
	@Override
	@Name("addTransactionNode")
	@Description("Adds a Transaction node to graph db")
    @PluginTarget( GraphDatabaseService.class )
	public Node addTransactionNode( @Source GraphDatabaseService graphDb, 
			@Description( "Transaction hash" )
			@Parameter( name = "tx-hash" ) String txHash,
			@Description( "Input addresses" )
			@Parameter( name = "tx-ins" ) List<String> txIns, 
			@Description( "Input amounts in Satoshis" )
			@Parameter( name = "amounts-in" ) List<Long> amountsIn,
			@Description( "Input transactions hashes" )
			@Parameter( name = "tx-prevs") List<String> txPrevs,
			@Description( "Output addresses" )
			@Parameter( name = "tx-outs" ) List<String> txOuts,
			@Description( "Output amounts in Satoshis" )
			@Parameter( name = "amounts-out" ) List<Long> amountsOut,
			@Description( "Hash of the block where the transaction is timestamped" )
			@Parameter( name = "block-hash" ) String blockHash,
			@Description( "Timestamp of the transaction" )
			@Parameter( name = "timestamp" ) Long timestamp){
		
		//TODO Check input parameters
		Transaction t = TransactionLocalServiceUtil.getOrCreateTransaction(graphDb, 
				txHash, txIns, amountsIn, txPrevs, txOuts, amountsOut, 
				blockHash, timestamp);
		
		return t.getUnderlyingNode();
	}
    
}
