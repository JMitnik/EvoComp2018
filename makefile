player0.class: contest.jar player0.java
	javac -cp $^

.PHONY: submit
submit: MainClass.txt submission.jar player0.class
	jar cmf $^

.PHONY: test_dummy
test_dummy: submit
	java -jar testrun.jar -submission=player0 -evaluation=SphereEvaluation -seed=1

.PHONY: test_bcf
test_bcf: submit
	java -jar testrun.jar -submission=player0 -evaluation=BentCigarFunction -seed=1

clean:
	rm player0.class