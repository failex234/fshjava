CC=gcc

win:
	$(CC) -o fshutils.dll -I /cygdrive/c/Users/fnauman1/JDK8/include -I /cygdrive/c/Users/fnauman1/JDK8/include/win32 -shared -W fshutils.c
	
nix:
	@echo "Need to implement :("