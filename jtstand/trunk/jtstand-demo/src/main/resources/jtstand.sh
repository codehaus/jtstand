cd `dirname $0`
java -Xmx256m -Xms256m -Xmn64m -Dorg.jboss.logging.provider=log4j -jar ${project.artifactId}-${project.version}.jar  -x jtstand-1.0.xsd