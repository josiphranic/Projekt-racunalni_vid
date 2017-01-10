package fer.projekt.classes;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sun.misc.BASE64Encoder;

public class StreamGetter {

	private HttpURLConnection huc;

	public StreamGetter(String jpgURL, String userName, String password) throws IOException {
		setConnection(jpgURL, userName, password);
	}

	private void setConnection(String jpgURL, String userName, String password) throws IOException {
		URL url = new URL(jpgURL);
		huc = (HttpURLConnection) url.openConnection();
		huc.setConnectTimeout(1000);
		huc.setReadTimeout(1000);

		BASE64Encoder enc = new sun.misc.BASE64Encoder();
		String userpassword = userName + ":" + password;
		String encodedAuthorization = enc.encode(userpassword.getBytes());
		huc.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
	}

	//TODO obrisi
	public InputStream getInputStreamForURL(String targetURL) throws IOException{
		URL url = new URL(targetURL);
		huc = (HttpURLConnection) url.openConnection();
		huc.setConnectTimeout(1000);
		huc.setReadTimeout(1000);
		return huc.getInputStream();
	}
	
	public InputStream getInputStream() throws IOException {
		return huc.getInputStream();
	}
	
	public InputStream getInputStream(String targetURL) throws IOException {
		
		return null;
	}

	public void resetConnection() throws IOException{
		huc.disconnect();
		huc.connect();
	}
	
	public void disconnect(){
		huc.disconnect();
	}
}
