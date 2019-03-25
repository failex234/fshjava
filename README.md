# fshjava
fsh is a simple shell written in java

# Compiling
First make sure that your JAVA_HOME variable is set!

For **Windows** and **GNU/Linux** just type `make` to compile the shared library for your OS.

For ***NIX** and **other OS's** there is no make recipe so you need to issue the command:
`cc -o fshutils.so -I ${JAVA_HOME}/include -I ${JAVA_HOME}/include/YOURSYSTEMINCLUDE -shared -W fshutils.c`

Where you **replace** the *.so* extension with the library extension of your OS and *YOURSYSTEMINCLUDE* to the
subfolder in your java include folder that contains system specific JNI Headers

# Why Java Native Interface?
There is a small problem with Java and that is you can't reliably change your current working directory. 
You can set the `user.dir` system property but the problem is that this is not really changing the working directory because some classes still use the the "real" working directory and you can't change it with Java. 

With JNI I'm able to reliably set the working directory to ensure that all classes that interface with the file system work as intended.

# License
This source code is licensed under the MIT License
