package my.app.updatedatabase;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class DownloadUtility {

	private DownloadUtility() {}
	
	public static void downloadFile(String urlStr, String filePath) {
		try {
			URL url = new URL(urlStr);
			File file = new File(filePath);
			FileUtils.copyURLToFile(url, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
