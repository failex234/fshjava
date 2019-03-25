package me.felixnaumann.fsh.General;

import me.felixnaumann.fsh.FshMain;
import me.felixnaumann.fsh.Utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramLauncher {

	private static boolean isAbsolutePath(String str) {
	if (System.getProperty("file.separator").equals("/")) {
		//*NIX code
		if (str.charAt(0) == '/') return true;
		return false;
	} else {
		//Windows code
		if (str.charAt(0) == '/') return true;
		if (str.charAt(0) == '\\') return true;
		//This regex matches things like C:\ or D:\ or C:/ or D:/
		if (str.matches("^.{1}:[\\\\/]")) return true;
		return false;
	}
}

private static boolean hasExtension(String str) {
	return str.matches(".+\\.{1}.+$");
}

public static void findAndLaunch(String[] args) {
	if (args == null || args.length == 0) {
		System.err.println("fsh: error");
		return;
	}
	
	String programname = args[0];
	String[] programargs = new String[args.length - 1];
	boolean launched = false;
	
	for (int i = 1; i < args.length; i++) {
		programargs[i - 1] = args[i];
	}
	
	if (isAbsolutePath(programname)) {
		try {
			launchProgram(programname, programargs);
			launched = true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	} else {
		String[] allpaths = FshMain.vars.get("PATH").split(";");

		if (hasExtension(programname)) {
			for (String path : allpaths) {
				File temp = new File(path + System.getProperty("file.separator") + programname);
				if (temp.exists()) {
					try {
						launchProgram(temp.getAbsolutePath(), programargs);
						launched = true;
						break;
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			for (String path : allpaths) {
				for (String extension : FileUtils.windowsFileExtensions) {
					File temp = new File(path + System.getProperty("file.separator") + programname + "." + extension);

					if (temp.exists()) {
						try {
							launchProgram(temp.getAbsolutePath(), programargs);
							launched = true;
							break;
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	if (!launched) {
		System.err.println("fsh: command not found");
	}
	
}

private static void launchProgram(String programpath, String[] args) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(programpath);
		List<String> pbargs = new ArrayList<>(Arrays.asList(args));
		pbargs.add(0, programpath);
		pb.command(pbargs);
		pb.start();
}

}
