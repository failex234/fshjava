#include "fshutils.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <unistd.h>
#include <errno.h>

#ifdef __WIN32
#include <windows.h>
#endif

JNIEXPORT jboolean JNICALL Java_me_felixnaumann_reflection_Utils_Native_isAdministrator(JNIEnv *env, jclass clazz) {
    #ifdef __WIN32
		int isadmin = 0;
		HANDLE hToken = NULL;

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
		return getuid() == 0 ? 1 : 0;
    #endif
}

JNIEXPORT jint JNICALL Java_me_felixnaumann_reflection_Utils_Native_changeWorkingDirectory(JNIEnv *env, jclass clazz, jstring dirstring) {
	int result = chdir(dirstring);
	
	if (errno == EACCESS) {
		return -1;
	} else if (errno == ENOENT) {
		return -2;
	} else if (errno == ENOTDIR) {
		return -3;
	}
	return 1;
}