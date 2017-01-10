package fer.projekt.examples;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 * 
 * Razred koji ostvaruje osnovnu HTTP komunikciju prema kameri i koji automatski
 * upravlja potrebnim procesima prijave.
 * 
 */
public class CameraConnector {

	public static final String DEFAULT_SERVER = "http://169.254.12.213/";
	public static final String DEFAULT_USERNAME = "root";
	public static final String DEFAULT_PASSWORD = "root";

	private String server = DEFAULT_SERVER;
	private String username = DEFAULT_USERNAME;
	private String password = DEFAULT_PASSWORD;

	public static int DEFAULT_CONNECT_TIMEOUT = 1000;
	public static int DEFAULT_READ_TIMEOUT = 1000;

	private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
	private int readTimeout = DEFAULT_READ_TIMEOUT;

	public CameraConnector() {
		initAuthenticator();
	}

	public CameraConnector(String server, String username, String password) {
		this();
		setServer(server);
		setUsername(username);
		setPassword(password);
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public InputStream getInputStream(String targetURL) {

		HttpURLConnection connection = getHttpUrlConnection(targetURL);
		connection.setConnectTimeout(connectTimeout);
		connection.setReadTimeout(readTimeout);

		InputStream is = null;
		try {
			is = connection.getInputStream();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		return is;
	}

	public HttpURLConnection getHttpUrlConnection(String targetURL) {

		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		return connection;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		if (server.length() > 0 && server.charAt(server.length() - 1) == '/')
			server = server.substring(0, server.length() - 1);
		this.server = server;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Metoda koja u slučaju upita podataka za prijavu šalje postavljene podatke
	 * i time osigurava nesmetanu daljnju komunikaciju HTTP protokolom.
	 */
	private void initAuthenticator() {
		Authenticator.setDefault(new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password.toCharArray());
			}
		});
	}

}

