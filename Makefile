CC=gcc
UNAME_S := $(shell uname)

.PHONY: win nix checkjava

all: win nix

win: checkjava
ifeq ($(OS), Windows_NT)
	@make fshutils.dll
endif

nix: checkjava
ifndef OS
ifeq ($(UNAME_S), Darwin)
	@echo Detected Mac OS
	@make fshutils.dylib
else
	@echo Detected *nix
	@make fshutils.so
endif
endif

fshutils.dll:
	$(CC) -o fshutils.dll -I ${JAVA_HOME}/include -I ${JAVA_HOME}/include/win32 -shared -W fshutils.c

fshutils.so:
	$(CC) -o fshutils.so -I ${JAVA_HOME}/include -I ${JAVA_HOME}/include/linux -shared -W -fPIC fshutils.c
	
fshutils.dylib:
	$(CC) -o fshutils.dylib -I ${JAVA_HOME}/include -I ${JAVA_HOME}/include/darwin -shared -W -fPIC fshutils.c

checkjava:
ifndef JAVA_HOME
	$(error JAVA_HOME is not set. Please set JAVA_HOME to compile!)
endif

clean:
	rm -f fshutils.dll fshutils.so
