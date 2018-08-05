package my.app.utilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class DownloadUtility {

	private DownloadUtility() {}

	//TODO: perhaps pass a URL instead, so that methods that rely on this can use a better suited
	//TODO: object, this will make incorrect usage harder (URL instead of String)
	//TODO: same for filePath (Paths aren't Strings)
	public static void downloadFile(String urlStr, String targetPath) {
		try {
			URL url = new URL(urlStr);
			File file = new File(targetPath);
			FileUtils.copyURLToFile(url, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
