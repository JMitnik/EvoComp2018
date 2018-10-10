#!/bin/sh
clear
# each ppl has different PATH
# export LD_LIBRARY_PATH=~/EvoComp2018/
# javac  com/Individual.java
javac -cp . com/auxComparator.java
javac -cp contest.jar:./ejml/ejml-simple-0.36.jar:./ejml/ejml-core-0.36.jar:. com/Population.java
javac -cp contest.jar:./ejml/ejml-simple-0.36.jar:./ejml/ejml-core-0.36.jar:. com/MUtils.java
# javac -cp . com/Utils.java
javac -cp contest.jar:./ejml/ejml-simple-0.36.jar:./ejml/ejml-core-0.36.jar:. com/EvoAlgorithm.java
javac -cp contest.jar:. player14.java
jar cmf MainClass.txt submission.jar player14.class com/MUtils.class com/Population.class com/auxComparator.class com/EvoAlgorithm.class ./org/ 
echo "evaluation=BentCigarFunction:"
java -jar testrun.jar -submission=player14 -evaluation=BentCigarFunction -seed=1
echo "evaluation=Katsuura:"
java -jar testrun.jar -submission=player14 -evaluation=KatsuuraEvaluation -seed=1
echo "evaluation=Schaffers:"
java -jar testrun.jar -submission=player14 -evaluation=SchaffersEvaluation -seed=1
rm player14.class ./com/EvoAlgorithm.class ./com/MUtils.class ./com/Population.class com/auxComparator.class
rm -r tmp