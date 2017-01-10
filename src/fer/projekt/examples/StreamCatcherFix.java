package fer.projekt.examples;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.CV_RGB;
import static com.googlecode.javacv.cpp.opencv_core.CV_WHOLE_SEQ;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCircle;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvDrawContours;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_highgui.cvQueryFrame;
import static com.googlecode.javacv.cpp.opencv_highgui.cvReleaseCapture;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_LINK_RUNS;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RETR_LIST;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvContourArea;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvFindContours;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvGetCentralMoment;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvGetSpatialMoment;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMoments;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.CvMoments;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

import sun.misc.BASE64Encoder;

@SuppressWarnings("javadoc")
public class StreamCatcherFix {

	private String jpgURL = "http://169.254.12.213/axis-cgi/jpg/image.cgi?resolution=CIF";
	private String mjpgURL = "http://169.254.12.213/mjpg/video.mjpg";
	private String userName = "root";
	private static String password = "root";
	private static int FRAME_HEIGHT = 352;
	private static int FRAME_WIDTH = 288;

	public void initStream() {

		DataInputStream dis = null;
		try {
			URL url = new URL(jpgURL);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setConnectTimeout(1000);
			huc.setReadTimeout(1000);

			BASE64Encoder enc = new sun.misc.BASE64Encoder();
			String userpassword = userName + ":" + password;
			String encodedAuthorization = enc.encode(userpassword.getBytes());
			huc.setRequestProperty("Authorization", "Basic " + encodedAuthorization);

			dis = new DataInputStream(new BufferedInputStream(huc.getInputStream()));

		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}

		JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(dis);
		BufferedImage image = null;

		try {
			image = decoder.decodeAsBufferedImage();
		} catch (ImageFormatException | IOException e) {
			e.printStackTrace();
		}

		IplImage frame = IplImage.createFrom(image);

		render(frame);

		// Path jpegImage =
		// Paths.get("C:/Users/Korisnik/Desktop/Cam/image.jpg");
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// try {
		// ImageIO.write(image, "jpg", baos);
		// baos.flush();
		// Files.write(jpegImage, baos.toByteArray());
		// baos.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	private void render(IplImage frame) {
		IplImage hsvimage = cvCreateImage(cvSize(FRAME_HEIGHT, FRAME_WIDTH), IPL_DEPTH_8U, 3);
		IplImage binimage = cvCreateImage(cvSize(FRAME_HEIGHT, FRAME_WIDTH), IPL_DEPTH_8U, 1);

		CvMemStorage storage = new CvMemStorage().create();
		CvSeq contour1;
		CvSeq contour2;
		int maxArea = 0;
		int area = 0;

		CvMoments moments = new CvMoments(Loader.sizeof(CvMoments.class));
		double spatialMomentX;
		double spatialMomentY;
		double spatialMomentArea;
		int objectPositionX;
		int objectPositionY;

		if (frame == null) {
			System.err.println("frame is null");
			System.exit(0);
		}

		cvCvtColor(frame, hsvimage, CV_BGR2HSV);
		cvShowImage("HSV", hsvimage);

		// filtering by color
		CvScalar minc = cvScalar(75, 90, 100, 0);
		CvScalar maxc = cvScalar(145, 255, 245, 0);
		cvInRangeS(hsvimage, minc, maxc, binimage);

		// finding all objects
		contour1 = new CvSeq();
		maxArea = 100;
		cvFindContours(binimage, storage, contour1, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_LINK_RUNS,
				cvPoint(0, 0));
		contour2 = contour1;

		// finding area of biggest object
		while (contour1 != null && !contour1.isNull()) {
			area = (int) cvContourArea(contour1, CV_WHOLE_SEQ, 1);
			if (area > maxArea) {
				maxArea = area;
			}

			contour1 = contour1.h_next();
		}

		// filling all object except biggest one
		while (contour2 != null && !contour2.isNull()) {
			area = (int) cvContourArea(contour2, CV_WHOLE_SEQ, 1);
			if (area < maxArea) {
				cvDrawContours(binimage, contour2, CV_RGB(0, 0, 0), CV_RGB(0, 0, 0), 0, CV_FILLED, 8, cvPoint(0, 0));
			}
			contour2 = contour2.h_next();
		}

		cvShowImage("Filtered bin-image", binimage);

		// calculating moments
		cvMoments(binimage, moments, 1);
		spatialMomentX = cvGetSpatialMoment(moments, 1, 0);
		spatialMomentY = cvGetSpatialMoment(moments, 0, 1);
		spatialMomentArea = cvGetCentralMoment(moments, 0, 0);

		// calculating (x,y) of object
		objectPositionX = (int) (spatialMomentX / spatialMomentArea);
		objectPositionY = (int) (spatialMomentY / spatialMomentArea);

		// drawing circle in center of object
		if (objectPositionX != 0 && objectPositionY != 0) {

			cvCircle(frame, cvPoint(objectPositionX, objectPositionY), 5, cvScalar(0, 255, 0, 0), 9, 0, 0);
		}
		cvShowImage("Camera", frame);

		char c = (char) cvWaitKey();

		if (c == 27) {
			System.exit(0);
		}
		
		cvReleaseMemStorage(storage);
	}

	public static void main(String[] args) {
		StreamCatcherFix scf = new StreamCatcherFix();
		scf.initStream();
	}
}
