export NEO4J_HOME=target/neo4j-community-2.0.0-M05
cp target/*.jar $NEO4J_HOME/plugins/
$NEO4J_HOME/bin/neo4j restart
echo "Plugin deployed and neo4j restarted!"