export MAVEN_OPTS="-server -Xmx3g -XX:+UseConcMarkSweepGC"
mvn exec:java -DjvmArgs="-Dcom.sun.management.jmxremote" -Dexec.mainClass="bitiodine.utils.Neo4jBatchImporter"