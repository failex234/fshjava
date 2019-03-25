#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <sys/stat.h>
#include "fshutils.h"

#ifdef __WIN32

#include <windows.h>
#else
	
#include <unistd.h>
#include <sys/types.h>

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

JNIEXPORT jboolean JNICALL Java_me_felixnaumann_reflection_Utils_Native_isAdministrator(JNIEnv *env, jclass clazz) {
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

JNIEXPORT jint JNICALL Java_me_felixnaumann_reflection_Utils_Native_changeWorkingDirectory(JNIEnv *env, jclass clazz, jstring dest) {
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

JNIEXPORT jstring JNICALL Java_me_felixnaumann_reflection_Utils_Native_getWorkingDirectory(JNIEnv *env, jclass clazz) {
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