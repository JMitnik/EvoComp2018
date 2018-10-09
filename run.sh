#!/bin/sh
export LD_LIBRARY_PATH=~/EvoComp2018/
javac -cp contest.jar:. EvoAlgorithm.java
javac -cp contest.jar:. player14.java
jar cmf MainClass.txt submission.jar player14.class ./org/ EvoAlgorithm.class
echo "evaluation=SphereEvaluation:"
java -jar testrun.jar -submission=player14 -evaluation=SphereEvaluation -seed=1
echo "evaluation=BentCigarFunction:"
java -jar testrun.jar -submission=player14 -evaluation=BentCigarFunction -seed=1
echo "evaluation=Schaffers:"
java -jar testrun.jar -submission=player14 -evaluation=SchaffersEvaluation -seed=1
echo "evaluation=Katsuura:"
java -jar testrun.jar -submission=player14 -evaluation=KatsuuraEvaluation -seed=1
rm player14.class EvoAlgorithm.class
rm -r tmp
