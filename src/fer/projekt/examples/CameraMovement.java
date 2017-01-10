package fer.projekt.examples;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CameraMovement {

	private CameraConnector connector;
	private static final String CGI = "axis-cgi/com/ptz.cgi";

	public CameraMovement(CameraConnector connector) {
		this.connector = connector;
	}

	private String getBaseURL() {
		return connector.getServer() + "/" + CGI;
	}

	/**
	 * Pomiče kameru za jedan korak prema gore.
	 */
	public void moveUp() {
		connector.getInputStream(getBaseURL() + "?move=up");

	}

	/**
	 * Pomiče kameru za jedan korak prema dolje.
	 */
	public void moveDown() {
		connector.getInputStream(getBaseURL() + "?move=down");
	}

	/**
	 * Pomiče kameru za jedan korak prema desno.
	 */
	public void moveRight() {
		connector.getInputStream(getBaseURL() + "?move=right");
	}

	/**
	 * Pomiče kameru za jedan korak prema lijevo.
	 */
	public void moveLeft() {
		connector.getInputStream(getBaseURL() + "?move=left");
	}

	/**
	 * Apsolutno postavlja iris kamere na zadani.
	 * 
	 * @param irisSteps
	 *            [1, 9999]
	 */
	public void changeIris(int irisSteps) {
		connector.getInputStream(getBaseURL() + "?iris=" + irisSteps);
	}

	/**
	 * Vertikalna rotacija kamere na zadani kut.
	 * 
	 * @param degrees
	 *            [-180, 180]
	 */
	public void panTo(float degrees) {
		connector.getInputStream(getBaseURL() + "?pan=" + degrees);
	}

	/**
	 * Horizontalna rotacija kamere na zadani kut.
	 * 
	 * @param degrees
	 *            [-180, 180]
	 */
	public void tiltTo(float degrees) {
		connector.getInputStream(getBaseURL() + "?tilt=" + degrees);
	}

	/**
	 * Apsolutno postavlja korak povećanja.
	 * 
	 * @param zoom
	 *            [1, 9999]
	 */
	public void setZoom(int zoom) {
		connector.getInputStream(getBaseURL() + "?zoom=" + zoom);
	}

	/**
	 * Apsolutno postavlja korak svjetlosti.
	 * 
	 * @param brightness
	 *            [1, 9999]
	 */
	public void setBrightness(int brightness) {
		connector.getInputStream(getBaseURL() + "?brightness=" + brightness);
	}

	/**
	 * Apsolutno postavlja korak fokusa.
	 * 
	 * @param focus
	 *            [1, 9999]
	 */
	public void setFocus(int focus) {
		connector.getInputStream(getBaseURL() + "?focus=" + focus);
	}

	/**
	 * Relativna promjena irisa.
	 * 
	 * @param iris
	 *            [-9999, 9999]
	 */
	public void changeIrisRelatively(int iris) {
		connector.getInputStream(getBaseURL() + "?riris=" + iris);
	}

	/**
	 * Relativno zoomiranje.
	 * 
	 * @param zoom
	 *            [-9999, 9999]
	 */
	public void changeZoomRelatively(int zoom) {
		connector.getInputStream(getBaseURL() + "?rzoom=" + zoom);
	}

	/**
	 * Relativno fokusiranje.
	 * 
	 * @param focus
	 *            [-9999, 9999]
	 */
	public void changeFocusRelatively(int focus) {
		connector.getInputStream(getBaseURL() + "?rfocus=" + focus);
	}

	/**
	 * Relativna vertikalna rotacija.
	 * 
	 * @param degrees
	 *            [-360.0, 360.0]
	 */
	public void changeTiltRelatively(float degrees) {
		connector.getInputStream(getBaseURL() + "?rtilt=" + degrees);
	}

	/**
	 * Relativna horizontalna rotacija.
	 * 
	 * @param degrees
	 *            [-360.0, 360.0]
	 */
	public void changePanRelatively(float degrees) {
		connector.getInputStream(getBaseURL() + "?rpan=" + degrees);
	}

	/**
	 * Relativna promjena osvjetljenja.
	 * 
	 * @param steps
	 *            [-9999, 9999]
	 */
	public void changeBrightnessRelatively(int steps) {
		connector.getInputStream(getBaseURL() + "?rbrightness=" + steps);
	}

	/**
	 * Usmjerava kameru na točku (x, y) na slici koju kamera trenutno prikazuje.
	 * 
	 * @param x
	 * @param y
	 */
	public void moveToCoordinates(int x, int y) {
		connector.getInputStream(getBaseURL() + "?center=" + x + "," + y);
	}

	/**
	 * Usmjerava kameru na točku (x, y) na slici koju kamera trenutno prikazuje
	 * ako se pretpostavi da je slika širine width i visina height.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void moveToCoordinates(int x, int y, int width, int height) {
		connector.getInputStream(
				getBaseURL() + "?center=" + x + "," + y + "&imagewidth=" + width + "&imageheight=" + height);
	}

	/**
	 * Usmjerava kameru na točku (x, y) na slici koju kamera trenutno prikazuje
	 * te određuje koliki postotak od trenutnog povećanja da se postavi (100 ne
	 * mijenja ništa, <100 je smanjivanje, >100 je povećavanje).
	 * 
	 * @param x
	 * @param y
	 * @param viewPercentage
	 */
	public void moveToCoordinatesAndZoom(int x, int y, int viewPercentage) {
		connector.getInputStream(getBaseURL() + "?areazoom=" + x + "," + y + "," + viewPercentage);
	}

	/**
	 * Usmjerava kameru na točku (x, y) na slici koju kamera trenutno prikazuje
	 * ako se pretpostavi da je slika širine width i visine height te određuje
	 * koliki postotak od trenutnog povećanja da se postavi (100 ne mijenja
	 * ništa, <100 je smanjivanje, >100 je povećavanje).
	 * 
	 * @param x
	 * @param y
	 * @param viewPercentage
	 * @param width
	 * @param height
	 */
	public void moveToCoordinatesAndZoom(int x, int y, int viewPercentage, int width, int height) {
		connector.getInputStream(getBaseURL() + "?areazoom=" + x + "," + y + "," + viewPercentage + "&imagewidth="
				+ width + "&imageheight=" + height);
	}

	/**
	 * Uključivanje/isključivanje automatske prilagodbe irisa.
	 * 
	 * @param autoiris
	 *            true za uključenje, false za isključenje.
	 */
	public void setAutoiris(boolean autoiris) {
		String onOrOff;
		if (autoiris == false) {
			onOrOff = "off";
		} else {
			onOrOff = "on";
		}
		connector.getInputStream(getBaseURL() + "?autoiris=" + onOrOff);
	}

	/**
	 * Uključivanje/isključivanje autofokusiranja.
	 * 
	 * @param autoFocus
	 *            true za uključenje, false za isključenje.
	 */
	public void setAutofocus(boolean autoFocus) {
		String onOrOff;
		if (autoFocus == false) {
			onOrOff = "off";
		} else {
			onOrOff = "on";
		}
		connector.getInputStream(getBaseURL() + "?autofocus=" + onOrOff);
	}

	/**
	 * Metoda koja postavlja brzinu kamere.
	 * 
	 * @param speed
	 *            [1, 100]
	 */
	public void setSpeed(int speed) {
		connector.getInputStream(getBaseURL() + "?speed=" + speed);
	}

	/**
	 * Metoda koja dohvaća trentnu brzinu kretanja kamere.
	 * 
	 * @return Trenutna brzina kamere.
	 */
	public int getSpeed() {
		InputStream is = connector.getInputStream(getBaseURL() + "?query=speed");

		BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));

		String line = null;
		try {
			line = br.readLine();
		} catch (IOException e) {
			return -1;
		}

		String speed = line.substring(line.indexOf("=") + 1);

		return Integer.parseInt(speed);
	}

	/**
	 * Metoda koja postavlja kontinuirano fokusiranje danom brzinom.
	 * 
	 * @param focusSpeed
	 *            [-100, 100]
	 */
	public void setContinuousFocusMove(int focusSpeed) {
		connector.getInputStream(getBaseURL() + "?continuousfocusmove=" + focusSpeed);
	}

	/**
	 * Metoda koja postavlja kontinuiranu promjenu svjetlosti danom brzinom.
	 * 
	 * @param brightnessSpeed
	 *            [-100, 100]
	 */
	public void setContinuousBrightnessMove(int brightnessSpeed) {
		connector.getInputStream(getBaseURL() + "?continuousbrightnessmove=" + brightnessSpeed);
	}

	/**
	 * Metoda koja postavlja kontinuirano povećavanje danom brzinom.
	 * 
	 * @param zoomSpeed
	 *            [-100, 100]
	 */
	public void setContinuousZoomMove(int zoomSpeed) {
		connector.getInputStream(getBaseURL() + "?continuouszoommove=" + zoomSpeed);
	}

	/**
	 * Metoda koja postavlja kontinuiranu promjenu irisa danom brzinom.
	 * 
	 * @param irisSpeed
	 *            [-100, 100]
	 */
	public void setContinuousIrisMove(int irisSpeed) {
		connector.getInputStream(getBaseURL() + "?continuousirismove=" + irisSpeed);
	}

	/**
	 * Metoda koja uključuje kontinuiranu horizontalnu i vertikalnu rotaciju
	 * danim brzinama.
	 * 
	 * @param panSpeed
	 *            [-100, 100]
	 * @param tiltSpeed
	 *            [-100, 100]
	 */
	public void setContinuousPanTiltMove(int panSpeed, int tiltSpeed) {
		connector.getInputStream(getBaseURL() + "?continuouspantiltmove=" + panSpeed + "," + tiltSpeed);
	}

}
