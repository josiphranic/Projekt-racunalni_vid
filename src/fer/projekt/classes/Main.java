package fer.projekt.classes;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.ImageFormatException;

public class Main {

	private static String jpgURL = "http://169.254.12.213/axis-cgi/jpg/image.cgi?resolution=4CIF";
	private static String someJpgURL = "http://201.166.63.44/axis-cgi/jpg/image.cgi?resolution=SIF";
	private static String userName = "root";
	private static String password = "root";

	public static void main(String[] args) {
		StreamGetter streamGetter = null;
		StreamToImage sti = null;
		BufferedImage image = null;
		Detectable blueColorDetect;
		CameraController cameraController = new CameraController();

		try {
			streamGetter = new StreamGetter(someJpgURL, userName, password);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			sti = new StreamToImage(new DataInputStream(new BufferedInputStream(streamGetter.getInputStream())));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			image = sti.getImage();
		} catch (ImageFormatException | IOException e) {
			e.printStackTrace();
		}

		blueColorDetect = new BlueColorDetect(image);
		streamGetter.disconnect();

		// TODO napraviti osvjezavanje slike bez ponovnog uspostavljanja http
		// veze

		int x;
		int y;
		while (true) {
			try {
				streamGetter = new StreamGetter(jpgURL, userName, password);
				sti = new StreamToImage(new DataInputStream(new BufferedInputStream(streamGetter.getInputStream())));
				image = sti.getImage();
				blueColorDetect = new BlueColorDetect(image);
				streamGetter.disconnect();
				if (blueColorDetect.getPressedKey() == 27) {
					break;
				}

				System.out.println("iw="+ image.getWidth()+" ih="+image.getHeight());
				
				x = (int) (blueColorDetect.getPositionVector().getX() / 4.0 * (double) image.getWidth())
						+ image.getWidth() / 2;
				y = (int) (blueColorDetect.getPositionVector().getY() / 4.0 * (double) image.getHeight())
						+ image.getHeight()/2;

				System.out.println("x="+x+" y="+y);
				
				cameraController.cameraCommand("center=" + x + "," + y + "&imagewidth="+image.getWidth()+"&mageheight="+image.getHeight());

			} catch (ImageFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		streamGetter.disconnect();
		blueColorDetect.dispose();
	}
}
