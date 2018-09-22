player14.class: contest.jar player14.java
	javac -cp $^

.PHONY: submit
submit: MainClass.txt submission.jar *.class
	jar cmf $^

.PHONY: test_dummy
test_dummy: submit
	java -jar testrun.jar -submission=player14 -evaluation=SphereEvaluation -seed=1

.PHONY: test_bcf
test_bcf: submit
	java -jar testrun.jar -submission=player14 -evaluation=BentCigarFunction -seed=1

clean:
	rm player14.class