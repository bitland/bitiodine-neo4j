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
import org.neo4j.graphdb.Relationship;
import org.neo4j.server.plugins.Description;
import org.neo4j.server.plugins.Name;
import org.neo4j.server.plugins.Parameter;
import org.neo4j.server.plugins.PluginTarget;
import org.neo4j.server.plugins.ServerPlugin;
import org.neo4j.server.plugins.Source;

import bitiodine.domain.model.Address;
import bitiodine.domain.model.Cluster;
import bitiodine.domain.service.AddressLocalServiceUtil;
import bitiodine.domain.service.ClusterLocalServiceUtil;


@Description( "An extension to the Neo4j Server for feeding neo4j graph db" )
public class GraphPluginImpl extends ServerPlugin implements GraphPlugin
{	
	@Override
    @Name("addAddressNode")
    @Description("Adds an Address node to graph db")
    @PluginTarget( GraphDatabaseService.class )
    public Node addAddressNode(	@Source GraphDatabaseService graphDb, 
    		@Description( "The address of the node to add." )
    		@Parameter( name = "address" ) String address) {
    	Address a = AddressLocalServiceUtil.addAddress(graphDb, address);
    	return a.getUnderlyingNode();
    }
    
	@Override
    @Name("addClusterNode")
    @Description("Adds a Cluster node to graph db")
    @PluginTarget( GraphDatabaseService.class )
	public Node addClusterNode( @Source GraphDatabaseService graphDb,
			@Description( "The id of the cluster to add." )
			@Parameter( name = "cluster_id" ) String cluster_id) {
		Cluster c = ClusterLocalServiceUtil.addCluster(graphDb, cluster_id);
		return c.getUnderlyingNode();
	}

	@Override
    @Name("linkAddressToCluster")
    @Description("Adds a Cluster node to graph db")
	public Relationship linkAddressToCluster(GraphDatabaseService graphDb, String address, String cluster_id) {
		Address a = AddressLocalServiceUtil.linkAddressToCluster(graphDb, address, cluster_id);
		return a.getClusterRelationship();
	}
    
}
