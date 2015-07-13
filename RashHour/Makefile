TARGET=Example
SRCS=$(TARGET).java RashModel.java RashView.java RashController.java Constants.java

all:
	javac *.java
test: all
	java $(TARGET)
vim:
	vim $(SRCS)
clean:
	rm *.class
wc:
	wc -l *.java
