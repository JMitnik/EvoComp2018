#!/bin/sh
export LD_LIBRARY_PATH=~/EvoComp2018/
javac  com/Individual.java
javac -cp . com/Population.java
javac -cp . com/Utils.java
javac -cp contest.jar:. com/EvoAlgorithm.java
javac -cp contest.jar:. player14.java
jar cmf MainClass.txt submission.jar player14.class com/Individual.class com/Population.class com/EvoAlgorithm.class com/Utils.class
echo "evaluation=SphereEvaluation:(hasStructure,isSeparable)"
java -jar testrun.jar -submission=player14 -evaluation=SphereEvaluation -seed=1
echo "evaluation=BentCigarFunction:"
java -jar testrun.jar -submission=player14 -evaluation=BentCigarFunction -seed=1
echo "evaluation=SchaffersEvaluation:(isMultimodal,hasStructure)"
java -jar testrun.jar -submission=player14 -evaluation=SchaffersEvaluation -seed=1
echo "evaluation=KatsuuraEvaluation:(isMultimodal)"
# java -jar testrun.jar -submission=player14 -evaluation=KatsuuraEvaluation -seed=1
rm player14.class ./com/EvoAlgorithm.class ./com/Individual.class ./com/Population.class ./com/Utils.class
