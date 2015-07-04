TARGET=Example
SRCS=$(TARGET).java PC.java KademliaModel.java KademliaView.java
LIB_DIR=lib
LIBS=\
	 $(LIB_DIR)/gson-2.2.2.jar:$(LIB_DIR)/guice-3.0-no_aop.jar:$(LIB_DIR)/guice-3.0.jar:$(LIB_DIR)/guice-throwingproviders-3.0.jar:$(LIB_DIR)/hamcrest-all-1.2.jar:$(LIB_DIR)/javax-inject.jar:$(LIB_DIR)/json-lib-2.4-jdk15.jar:$(LIB_DIR)/lambdaj-2.3.3-with-dependencies.jar:$(LIB_DIR)/mockito-all-1.9.0.jar:$(LIB_DIR)/openkad-r27.jar:$(LIB_DIR)/commons-codec-1.4.jar

JFLAG = -cp .:$(LIBS)

all:
	@javac $(JFLAG) $(SRCS)
test: all
	java $(JFLAG) $(TARGET)
vim:
	vim $(SRCS)
clean:
	rm *.class
