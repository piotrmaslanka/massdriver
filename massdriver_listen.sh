#!/bin/bash
export CLASSPATH=`pwd`/massdriver.jar
rmiregistry $2 -Djava.rmi.server.codebase=massdriver.jar -Djava.security.policy=java.policy &
java -Djava.security.policy=java.policy massdriver.worker.Run $1:$2