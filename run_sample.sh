#!/bin/sh
javac -cp ./ejml/ejml-fdense-0.36.jar:./ejml/ejml-simple-0.36.jar:./ejml/ejml-core-0.36.jar:./ejml/ejml-ddense-0.36.jar:./ejml/ejml-zdense-0.36.jar:. ./com/MUtils.java
java -cp ./ejml/ejml-fdense-0.36.jar:./ejml/ejml-simple-0.36.jar:./ejml/ejml-core-0.36.jar:./ejml/ejml-ddense-0.36.jar:./ejml/ejml-zdense-0.36.jar:. com.MUtils

# javac -cp contest.jar:./ejml/ejml-simple-0.36.jar:./ejml/ejml-core-0.36.jar:. com/Population.java
# java -cp contest.jar:./ejml/ejml-simple-0.36.jar:./ejml/ejml-core-0.36.jar:. com.Population

rm ./com/*.class