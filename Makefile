all:
	mv src/massdriver .
	find -name "*.java" > sources.tmp
	javac @sources.tmp
	rm sources.tmp
	jar cf massdriver.jar massdriver
	mv massdriver src
	fromdos massdriver_listen.sh
	fromdos massdriver_run.sh
	chmod +x massdriver_listen.sh
	chmod +x massdriver_run.sh
clean:
	rm -rf bin
	rm -f massdriver.jar