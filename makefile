JC = javac
JFLAGS = -sourcepath src -Xlint:all
SRC = ./src/
SP = com/twicestruck/metalpunk/
RM = rm -f
.SUFFIXES: .java .class
.PHONY: clean

CLIENTCLASSES = $(patsubst $(SRC)$(SP)client/%.java,$(SRC)$(SP)client/%.class,$(wildcard $(SRC)$(SP)client/*.java))

SERVERCLASSES = $(patsubst $(SRC)$(SP)server/%.java,$(SRC)$(SP)server/%.class,$(wildcard $(SRC)$(SP)server/*.java))

COMMONCLASSES = $(patsubst $(SRC)$(SP)common/%.java,$(SRC)$(SP)common/%.class,$(wildcard $(SRC)$(SP)common/*.java))

%.class: %.java
	$(JC) $(JFLAGS) $<

default: server client
	
client: $(CLIENTCLASSES) $(COMMONCLASSES)
	cd src && jar -cfe ../MetalpunkClient.jar com.twicestruck.metalpunk.client.Client $(SP)client/*.class $(SP)common/*.class
	
server: $(SERVERCLASSES) $(COMMONCLASSES)
	cd src && jar -cfe ../MetalpunkServer.jar com.twicestruck.metalpunk.server.Server $(SP)server/*.class $(SP)common/*.class
	
lines:
	bc <<< "$(shell cat ./src/com/twicestruck/metalpunk/common/*.java | sed '/^\s*$$/d' | wc -l) + $(shell cat ./src/com/twicestruck/metalpunk/client/*.java | sed '/^\s*$$/d' | wc -l) + $(shell cat ./src/com/twicestruck/metalpunk/server/*.java | sed '/^\s*$$/d' | wc -l)"
	
clean:
	$(RM) src/com/twicestruck/metalpunk/common/*.class
	$(RM) src/com/twicestruck/metalpunk/client/*.class
	$(RM) src/com/twicestruck/metalpunk/server/*.class
