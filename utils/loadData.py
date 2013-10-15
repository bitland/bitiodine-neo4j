#!/usr/local/opt/python/bin/python

# SQLAlchemy imports
import urllib
import urllib2

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from bitiodine_mapping import Tx

import signal
import sys

t = 0

# Create SQLAlchemy session
engine = create_engine('sqlite:///blockchain/blockchain.sqlite', echo=False)
engine.execute("select 1").scalar()
Session = sessionmaker(bind=engine)
session = Session()

# Open file and intercept Ctrl+C 
f=open('./latest_added_transaction.txt', 'w+')
def signal_handler(signal, frame):
        print 'You pressed Ctrl+C!'
        print t.tx_id
        print >> f, t.tx_id
        sys.exit(0)
        
signal.signal(signal.SIGINT, signal_handler)
signal.signal(signal.SIGTERM, signal_handler)

print "Start filling neo4j db"
for t in session.query(Tx).order_by(Tx.tx_id).slice(0,100000):
    assert isinstance(t,Tx)
    txIns=t.txIns 
    txOuts=t.txOuts
    
    txHash = t.tx_hash
    inAddresses = map(lambda x: x.txout.address.encode('ascii') ,t.txIns)
    outAddresses = map(lambda x: x.address.encode('ascii'), t.txOuts)
    inAmounts = map(lambda x: x.txout.txout_value, t.txIns)
    outAmounts = map(lambda x: x.txout_value, t.txOuts)
    txPrevs = map(lambda x: x.txout.tx.tx_hash.encode('ascii') ,t.txIns)
    blockHash = t.block.block_hash
    timestamp = t.block.time
    

    params = "{\n 'tx-hash' : '"+txHash+"',\n 'tx-ins' : "+str(inAddresses)+",\n "+\
    "'tx-prevs' : "+str(txPrevs)+",\n 'amounts-in' : "+str(inAmounts)+",\n "+\
    "'tx-outs' : "+str(outAddresses)+",\n 'amounts-out' : "+str(outAmounts)+",\n "+\
    "'block-hash' : '"+blockHash+"',\n 'timestamp' : "+str(timestamp)+"\n}"
    params = params.replace("'","\"")

    req = urllib2.Request('http://bitcoin:7474/db/data/ext/GraphPluginImpl/graphdb/addTransactionNode',params,{'Content-Type': 'application/json'})
    response = urllib2.urlopen(req).read()
    print t.tx_id

    


