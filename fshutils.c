#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <sys/stat.h>
#include <string.h>
#include "fshutils.h"

#ifdef __WIN32

#include <windows.h>

#else

#include <unistd.h>
#include <sys/types.h>
#include <sys/ioctl.h>
#include <sys/wait.h>

#endif

int isDirectory(const char *path) {
    struct stat filestats;
    if (stat(path, &filestats) != 0) {
        return 0;
    }
    return S_ISDIR(filestats.st_mode);
}

int fileExists(const char *path) {
    FILE *file = fopen(path, "r");

    if (file == NULL) {
        return 0;
    } else {
        fclose(file);
        return 1;
    }
}

JNIEXPORT jboolean JNICALL Java_me_felixnaumann_fsh_Utils_Native_isAdministrator(JNIEnv *env, jclass clazz) {
#ifdef __WIN32
	HANDLE hToken = NULL;
	int isadmin = -99;

		if (OpenProcessToken(GetCurrentProcess(), TOKEN_QUERY, &hToken)) {
			TOKEN_ELEVATION Elevation;
			DWORD cbSize = sizeof(TOKEN_ELEVATION);

			if (GetTokenInformation(hToken, TokenElevation, &Elevation, sizeof(Elevation), &cbSize)) {
				isadmin = Elevation.TokenIsElevated;
			}
		}

		if (isadmin) {
			CloseHandle(hToken);
		}

		return isadmin;
#else
	return !getuid();
#endif
}

JNIEXPORT jint JNICALL Java_me_felixnaumann_fsh_Utils_Native_changeWorkingDirectory(JNIEnv *env, jclass clazz, jstring dest) {
	char *destination = (*env)->GetStringUTFChars(env, dest, 0);

    if (destination[0] != '.') {
	    if (!isDirectory(destination)) {
	        return -3;
    	} else if (!fileExists(destination) && !isDirectory(destination)) {
	        return -2;
	    }
	}

    errno = 0;
	chdir(destination);

	if (errno == EACCES) {
		return -1;
	} else if (errno == ENOENT) {
		return -2;
	} else if (errno == ENOTDIR) {
		return -3;
	}


	return 1;
}

JNIEXPORT jstring JNICALL Java_me_felixnaumann_fsh_Utils_Native_getWorkingDirectory(JNIEnv *env, jclass clazz) {
	char *buf = malloc(sizeof(char) * 512);

	errno = 0;
	if (getcwd(buf, 512) == NULL) {
		if (errno == EACCES) {
			strcpy(buf, "Access denied!");
		} else {
			sprintf(buf, "Unknown error with err code %d occured!", errno);
		}

		jstring ret = (*env)->NewStringUTF(env, buf);

		return ret;
	}

	jstring ret = (*env)->NewStringUTF(env, buf);

	return ret;
}

JNIEXPORT jstring JNICALL Java_me_felixnaumann_fsh_Utils_Native_getOS(JNIEnv *env, jclass clazz) {
	#ifdef __WIN32
		char *os = "win";
	#else
		char *os = "nix";
	#endif

	return (*env)->NewStringUTF(env, os);
}

JNIEXPORT void JNICALL Java_me_felixnaumann_fsh_Utils_Native_clearConsole(JNIEnv *env, jclass clazz) {
	#ifdef __WIN32
	//From https://docs.microsoft.com/en-us/windows/console/clearing-the-screen
	
	HANDLE hStdout = GetStdHandle(STD_OUTPUT_HANDLE);

	COORD coordScreen = { 0, 0 };    // home for the cursor 
   	DWORD cCharsWritten;
   	CONSOLE_SCREEN_BUFFER_INFO csbi; 
   	DWORD dwConSize;

	// Get the number of character cells in the current buffer. 
   	if( !GetConsoleScreenBufferInfo( hConsole, &csbi )) {
    	return;
   	}

   	dwConSize = csbi.dwSize.X * csbi.dwSize.Y;

   	// Fill the entire screen with blanks.
   	if( !FillConsoleOutputCharacter( hConsole,        // Handle to console screen buffer 
                                    (TCHAR) ' ',     // Character to write to the buffer
                                    dwConSize,       // Number of cells to write 
                                    coordScreen,     // Coordinates of first cell 
                                    &cCharsWritten ))// Receive number of characters written 
									{
    	return;
   	}

   	// Get the current text attribute.
   	if( !GetConsoleScreenBufferInfo( hConsole, &csbi )) {
      	return;
   	}

   	// Set the buffer's attributes accordingly.
   	if( !FillConsoleOutputAttribute( hConsole,         // Handle to console screen buffer 
                                    csbi.wAttributes, // Character attributes to use
                                    dwConSize,        // Number of cells to set attribute 
                                    coordScreen,      // Coordinates of first cell 
                                    &cCharsWritten )) // Receive number of characters written
									{
      	return;
   	}

   	// Put the cursor at its home coordinates.
   	SetConsoleCursorPosition( hConsole, coordScreen );

	#else
		puts("\033[H\033[2J");
		fflush(stdout);
	#endif
}

int getCursorPosition() {
	int rows, cols;
	char buf[32];
	uint i = 0;

	if (write(STDOUT_FILENO, "\x1b[6n", 4) != 4) return -1;

	while (i < sizeof(buf) - 1) {
        if (read(STDIN_FILENO, &buf[i], 1) != 1) {
                break;
        }
        if (buf[i] == 'R') {
                break;
        }
        i++;
    }

	buf[i] = '\0';

	if (buf[0] != '\x1b' || buf[1] != '[') {
        return -1;
    }

	if (sscanf(&buf[2], "%d;%d", &rows, &cols) != 2) {
        return -1;
    }

    return cols;

}

JNIEXPORT jint JNICALL Java_me_felixnaumann_fsh_Utils_Native_getConsoleWidth(JNIEnv *env, jclass clazz) {
	#ifdef __WIN32
		CONSOLE_SCREEN_BUFFER_INFO csbi;
		GetConsoleScreenBufferInfo(GetStdHandle(STD_OUTPUT_HANDLE), &csbi);

		return csbi.srWindow.Right - csbi.srWindow.Left + 1;
	#else
		struct winsize ws;

		if (ioctl(STDOUT_FILENO, TIOCGWINSZ, &ws) == -1 || ws.ws_col == 0) {
			if (write(STDOUT_FILENO, "\x1b[999C\x1b[999B", 12) != 12) {
				return -1;
			}
			return getCursorPosition();
		} else {
			return ws.ws_col;
		}
	#endif
}

JNIEXPORT jint JNICALL Java_me_felixnaumann_fsh_Utils_Native_runApplication(JNIEnv *env, jclass clazz, jstring application, jobjectArray argsptr, jobjectArray envptr) {
	char *application_ptr = (*env)->GetStringUTFChars(env, application, 0);
	#ifdef __WIN32

	#else
		pid_t pid = fork();

		if (pid == 0) {
			int argcount = (*env)->GetArrayLength(env, argsptr);
			char *args[argcount + 1];

			for (int i = 0; i < argcount; i++) {
				jstring tempstr = (jstring) ((*env)->GetObjectArrayElement(env, argsptr, i));
				args[i] = (*env)->GetStringUTFChars(env,tempstr, 0);
			}
			args[argcount] = NULL;

			if (envptr) {
				int envlen = (*env)->GetArrayLength(env, envptr);
				char *envptr_arr[envlen + 1];

				for (int i = 0; i < envlen; i++) {
					jstring tempstr = (jstring) ((*env)->GetObjectArrayElement(env, envptr, i));
					envptr_arr[i] = (*env)->GetStringUTFChars(env, tempstr, 0);
				}

				execve(application_ptr, args, envptr_arr);
			} else {
				execve(application_ptr, args, NULL);
			}
		} else {
			int status;
			waitpid(pid, &status, 0);

			return status;
		}
	#endif
}