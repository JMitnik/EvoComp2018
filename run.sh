#!/bin/sh
export LD_LIBRARY_PATH=~/EvoComp2018/
# bash compile.sh
if [ $# -eq 3 ]
then
   java -Dpopsize=$2 -Dtsize=$3 -jar testrun.jar -submission=player14 -evaluation=$1 -seed=1
else
  if [ $1 = 'compile' ]
  then
     javac  com/Individual.java
     javac -cp . com/Population.java
     javac -cp . com/Utils.java
     javac -cp contest.jar:. com/EvoAlgorithm.java
     javac -cp contest.jar:. player14.java
     jar cmf MainClass.txt submission.jar player14.class com/Individual.class com/Population.class com/EvoAlgorithm.class com/Utils.class
  fi
  if [ $1 = 'clean' ]
  then
     rm player14.class ./com/EvoAlgorithm.class ./com/Individual.class ./com/Population.class ./com/Utils.class
  fi
fi
