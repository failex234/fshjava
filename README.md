# fshjava
fsh is a simple shell written in java (and small parts in C)

# Before Compiling
Your System should have a C Compiler (gcc or clang) and make installed.

On Windows you need an environment like Cygwin, MinGW or WSL (Distro doesn't matter. I recommend Ubuntu though)

On GNU/Linux you just need to install the **gcc** and  **make** package with your local package manager. (You may need more than that and install **build-essential** or **base-devel**)

On Mac OS you need to install the clang compiler. Just type **cc** into the terminal and confirm to download the developer tools also containing make

# Compiling
First make sure that your JAVA_HOME variable is set!

Run **./gradlew build** on *nix

Run **gradlew.bat build** on Windows

After a successful build you should have a functioning jar in build/libs/fshjava-complete.jar (Currently the jar is missing the library. Make sure to have to library in your current working directory when you run the jar!)

# Just Compiling the Library
First make sure that your JAVA_HOME variable is set!

For **Windows**, **GNU/Linux** and **Mac OS** just type `make` to compile the shared library for your OS.

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
