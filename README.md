bitiodine-neo4j
===============

Clone this repo (e.g. in bitiodine-neo4j)

Download and untar neo4j-community-2.0.0-M05 in bitiodine-neo4j/neo4j-community-2.0.0-M05

Run "mvn post-clean install" in bitiodine-neo4j folder
The plugin will be recompiled and copied to bitiodine-neo4j/neo4j-community-2.0.0-M05/plugins 
Then the server will restart. A web interface will be listening on port 7474

Copy a blockchain.sqlite into utils/blockchain/

Modify the following line into utils/loadData.py to select the slice of transactions to load
"for t in session.query(Tx).order_by(Tx.tx_id).slice(0,100000):"

Launch utils/loadData.py

Wait...

