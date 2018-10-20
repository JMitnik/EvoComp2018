#!/bin/sh
export LD_LIBRARY_PATH=~/EvoComp2018/
# bash compile.sh
if [ $# -eq 4 ]
then
   java -Dpopsize=$2 -Dsigma=$3 -Dmomentum=$4 -jar testrun.jar -submission=player14 -evaluation=$1 -seed=1
else
  if [ $1 = 'compile' ]
  then
     javac -cp contest.jar:. EvoAlgorithm.java
     javac -cp contest.jar:. player14.java
     jar cmf MainClass.txt submission.jar player14.class ./org/ EvoAlgorithm.class
  fi
  if [ $1 = 'clean' ]
  then
     rm player14.class EvoAlgorithm.class
     rm -r tmp
  fi
fi
