CC=gcc

.PHONY: win nix checkjava

all: win nix

win: checkjava
ifeq ($(OS), Windows_NT)
	@make fshutils.dll
endif
	
nix: checkjava
ifndef OS
	@make fshutils.so
endif

fshutils.dll:
	$(CC) -o fshutils.dll -I ${JAVA_HOME}/include -I ${JAVA_HOME}/include/win32 -shared -W fshutils.c
	
fshutils.so:
	$(CC) -o fshutils.so -I ${JAVA_HOME}/include -I ${JAVA_HOME}/include/linux -shared -W fshutils.c
	
checkjava:
ifndef JAVA_HOME
	$(error JAVA_HOME is not set. Please set JAVA_HOME to compile!)
endif

clean:
	rm -f fshutils.dll fshutils.so