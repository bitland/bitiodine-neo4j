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
package virex4bitcoin.neo4j.plugin;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.server.plugins.Description;
import org.neo4j.server.plugins.Name;
import org.neo4j.server.plugins.Parameter;
import org.neo4j.server.plugins.PluginTarget;
import org.neo4j.server.plugins.ServerPlugin;
import org.neo4j.server.plugins.Source;

import virex4bitcoin.neo4j.service.VirexBitcoinLocalServiceUtil;

@Description( "An extension to the Neo4j Server for feeding neo4j graph db. All operations are idempotent." )
public class VirexBitcoinNeo4jPluginImpl extends ServerPlugin
{
	public Long flow(GraphDatabaseService graphDb, String payer, String payee) {
		// TODO Auto-generated method stub
		return VirexBitcoinLocalServiceUtil.flow(graphDb, payer, payee);
	}
	
	@Name("flow")
	@Description("Gets the exchanged amount between two bitcoin addresses in a given period"
			+ "of time")
	@PluginTarget( GraphDatabaseService.class )
	public Long flow(@Source GraphDatabaseService graphDb,
			@Description( "Payer address" )
			@Parameter( name = "payer" ) String payer, 
			@Description( "Payee address" )
			@Parameter( name = "payee" ) String payee, 
			@Description( "From date (unix timestamp)" )
			@Parameter( name = "fromdate" ) Long fromdate,
			@Description( "To date (unix timestamp)" )
			@Parameter( name = "todate" ) Long todate) 
	{
		// TODO check input parameters
		return VirexBitcoinLocalServiceUtil.flow(graphDb, payer, payee, fromdate, todate);
	}

	public Long balance(GraphDatabaseService graphDb, String address) {
		return VirexBitcoinLocalServiceUtil.balance(graphDb, address);
	}

	public Long balance(GraphDatabaseService graphDb, String address,
			Long attime) {
		return VirexBitcoinLocalServiceUtil.balance(graphDb, address, attime);
	}

	
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
		/*
		Transaction t = TransactionLocalServiceUtil.getOrCreateTransaction(graphDb,
				txHash, txIns, amountsIn, txPrevs, txOuts, amountsOut, 
				blockHash, timestamp);
		return t.getUnderlyingNode();
		*/
		
		return null; 
	}
    
}
