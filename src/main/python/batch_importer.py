#!/usr/local/bin/python3
from pymongo import MongoClient
import sqlite3

# Mongo configuration
client = MongoClient()
client = MongoClient('localhost', 27017)
db = client.virex4bitcoin

# Sqlite configuration
connection = sqlite3.connect("/Users/giuseppe/git/blockchain/blockchain.sqlite")
connection.row_factory = sqlite3.Row
cursor = connection.cursor()  


# Start transaction, set to 1 to start from the beginning
start=db.transactions.find().count()
if (start <= 1):
    db.drop_collection('transactions')
    start=1
collection = db.transactions

# Ensure indexes
collection.ensure_index("tx_id")
collection.ensure_index("payers.address")
collection.ensure_index("payees.address")
collection.ensure_index("time")

# Transactions
def txrow2transaction(row):
    cursor2 = connection.cursor()
    txid=row['tx_id']
    transaction = {"tx_id": row['tx_id'],
                   "hash" : row['tx_hash'],
                   "time": row['time'],
                   "payees": list(map(inoutrow2inouts, cursor2.execute("SELECT * FROM TXOUT WHERE tx_id=?", (txid,) ).fetchall())),
                   "payers" : list(map(inoutrow2inouts, cursor2.execute("SELECT * FROM TXIN LEFT JOIN TXOUT ON TXIN.txout_id=TXOUT.txout_id WHERE TXIN.tx_id=?", (txid,) ).fetchall()))
                   }
    return transaction

def inoutrow2inouts(row):
    out={"address" : row['address'],
         "value" : row['txout_value']
         }
    return out


pagesize = 100000
rowcount = 25529367-(start-1) #cursor.execute("SELECT COUNT(*) AS COUNT FROM TX JOIN BLOCKS ON TX.block_id=BLOCKS.block_id").fetchone()['COUNT']-(start-1)
inserted = 0
print("Importing ", rowcount ," transactions")

rows= cursor.execute("SELECT * FROM TX JOIN BLOCKS ON TX.block_id=BLOCKS.block_id LIMIT -1 OFFSET ?",(start-1,)).fetchmany(pagesize)
while rows:
    transactions=list(map(txrow2transaction,rows)) 
    collection.insert(transactions);
    inserted+=pagesize
    print("%.2f " % min(inserted/rowcount*100,100),"% completed")
    rows = cursor.fetchmany(pagesize)

    