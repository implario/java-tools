package implario;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@UtilityClass
public class FileUtils {

	public static List<File> listFiles(File dir, String regexMask) {
		List<File> result = new ArrayList<>();
		listFiles0(dir, Pattern.compile(regexMask), result);
		return result;
	}

	private static void listFiles0(File dir, Pattern regex, List<File> buffer) {
		File[] files = dir.listFiles();
		if (files == null) return;
		for (File file : files) {
			if (file.isDirectory()) listFiles0(file, regex, buffer);
			else if (regex.matcher(file.getName()).matches()) buffer.add(file);
		}
	}



}
