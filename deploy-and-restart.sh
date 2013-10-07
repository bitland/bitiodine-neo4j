export NEO4J_HOME=/Users/giuseppe/Desktop/Bitcoin/neo4j-community-2.0.0-M05
cp target/*.jar $NEO4J_HOME/plugins/
/Users/giuseppe/Desktop/Bitcoin/neo4j-community-2.0.0-M05/bin/neo4j restart
echo "Plugin deployed and neo4j restarted!"