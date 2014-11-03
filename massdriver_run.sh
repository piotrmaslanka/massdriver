#!/bin/bash
export CLASSPATH=`pwd`/massdriver.jar
java -Djava.security.policy=java.policy massdriver.coordinator.Run $@