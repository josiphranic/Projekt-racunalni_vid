package fer.projekt.classes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import sun.misc.BASE64Encoder;

public class CameraController {

	private static String userName = "root";
	private static String password = "root";
	private static String targetURL = "http://169.254.12.213/axis-cgi/com/ptz.cgi";

	public CameraController() {

	}

	public InputStream cameraCommand(String cameraCommand) throws IOException {

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
		return connection.getInputStream();

	}

	public void switchAutoFocus() throws IOException {

		String mode = "off";
		InputStream str = cameraCommand("query=autofocus");
		BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(str)));

		String line = null;
		try {
			line = br.readLine();
		} catch (IOException e) {
		}

		String autoFocus = line.substring(line.indexOf("=") + 1);
		if (autoFocus.equals("off")) {
			mode = "on";
		}

		cameraCommand("autofocus=" + mode);

	}

	public void switchAutoIris() throws IOException {

		String mode = "off";
		InputStream str = cameraCommand("query=autoiris");
		BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(str)));

		String line = null;
		try {
			line = br.readLine();
		} catch (IOException e) {
		}

		String autoIris = line.substring(line.indexOf("=") + 1);
		if (autoIris.equals("off")) {
			mode = "on";
		}

		cameraCommand("autoiris=" + mode);

	}

	public void moveToCoordinates(int x, int y, int width, int height) throws IOException {
		cameraCommand("center=" + x + "," + y + "&imagewidth=" + width + "&imageheight=" + height);
	}

	/**
	 * Postavlja kontinuiranje kretanje kamere. Pan je horizontalna, a tilt
	 * vertikalna rotacija.
	 * 
	 * @param panSpeed
	 *            Brzina horizontalne rotacije.
	 * @param tiltSpeed
	 *            Brzina vertikalne rotacije.
	 * @throws IOException
	 */
	public void setContinuousPanTiltMove(int panSpeed, int tiltSpeed) throws IOException {
		cameraCommand("continuouspantiltmove=" + panSpeed + "," + tiltSpeed);
	}

	/**
	 * Postavlja povećanje kamere na zadano.
	 * 
	 * @param zoom
	 *            [1, 9999]
	 * @throws IOException
	 */
	public void setZoom(int zoom) throws IOException {
		cameraCommand("zoom=" + zoom);
	}

	/**
	 * Postavlja iris na zadani.
	 * 
	 * @param iris
	 *            [1, 9999]
	 * @throws IOException
	 */
	public void setIris(int iris) throws IOException {
		cameraCommand("iris=" + iris);
	}

	/**
	 * Postavlja brzinu na zadanu.
	 * 
	 * @param speed
	 *            [1, 100]
	 * @throws IOException
	 */
	public void setSpeed(int speed) throws IOException {
		cameraCommand("speed=" + speed);
	}

	public void moveUp() throws IOException {
		cameraCommand("move=up");
	}

	public void moveDown() throws IOException {
		cameraCommand("move=down");
	}

	public void moveLeft() throws IOException {
		cameraCommand("move=left");
	}

	public void moveRight() throws IOException {
		cameraCommand("move=right");
	}

	/**
	 * Mijenja zoom relativno s obzirom na trenutni zoom.
	 * 
	 * @param zoom
	 *            Povećanje/smanjenje zooma s obzirom na trenutni. [-9999, 9999]
	 * @throws IOException
	 */
	public void changeZoomRelatively(int zoom) throws IOException {
		cameraCommand("rzoom=" + zoom);
	}

}
