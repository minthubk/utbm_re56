all: 
	javac RE56/*.java
	jar cef RE56.Main RE56.Main RE56/*.class

clean:
	rm -rf RE56/*.class RE56/*\$1.class RE56/*.java~ Makefile~