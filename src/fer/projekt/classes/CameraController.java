package fer.projekt.classes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sun.misc.BASE64Encoder;

public class CameraController {
	
	private static String userName = "root";
	private static String password = "root";
	private static String targetURL = "http://169.254.12.213/axis-cgi/com/ptz.cgi";
	
	public CameraController() {

	}

	public void cameraCommand( String cameraCommand) throws IOException {

		URL url = null;
		HttpURLConnection connection = null;
		url = new URL(targetURL + "?" + cameraCommand);
		connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(1000);
		connection.setReadTimeout(1000);
		BASE64Encoder enc = new sun.misc.BASE64Encoder();
		String userpassword = userName + ":" + password;
		String encodedAuthorization = enc.encode(userpassword.getBytes());
		connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
		connection.getInputStream();
		
	}
	
	
}
