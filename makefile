webcrawler.class : webcrawler.java
	javac webcrawler.java

run : webcrawler.class
	java webcrawler

clean :
	rm webcrawler.java
