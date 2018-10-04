#!/bin/sh
clear
# each ppl has different PATH
# export LD_LIBRARY_PATH=~/EvoComp2018/
javac  com/Individual.java
javac -cp . com/Population.java
javac -cp . com/Utils.java
javac -cp contest.jar:. com/EvoAlgorithm.java
javac -cp contest.jar:. player14.java
jar cmf MainClass.txt submission.jar player14.class com/Individual.class com/Population.class com/EvoAlgorithm.class com/Utils.class
echo "evaluation=BentCigarFunction:"
java -jar testrun.jar -submission=player14 -evaluation=BentCigarFunction -seed=1
echo "evaluation=Katsuura:"
java -jar testrun.jar -submission=player14 -evaluation=KatsuuraEvaluation -seed=1
echo "evaluation=Schaffers:"
java -jar testrun.jar -submission=player14 -evaluation=SchaffersEvaluation -seed=1
rm player14.class ./com/EvoAlgorithm.class ./com/Individual.class ./com/Population.class ./com/Utils.class
rm -r tmp