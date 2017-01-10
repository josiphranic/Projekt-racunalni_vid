package fer.projekt.classes;

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

import java.awt.image.BufferedImage;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvReleaseFunc;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.CvMoments;

public class BlueColorDetect implements Detectable {

	private BufferedImage image;
	private int imageWidth;
	private int imageHeight;
	private IplImage hsvimage;
	private IplImage binimage;
	private CvMemStorage storage;
	private CvSeq contour1;
	private CvSeq contour2;
	private int maxArea = 0;
	private int area = 0;
	private CvMoments moments;
	private double spatialMomentX;
	private double spatialMomentY;
	private double spatialMomentArea;
	private int objectPositionX;
	private int objectPositionY;
	private IplImage frame;
	private char pressedKey;

	public BlueColorDetect(BufferedImage image) {
		this.image = image;
		initImageProcessing();
		getResult();
	}

	private void initImageProcessing() {
		frame = IplImage.createFrom(image);
		hsvimage = cvCreateImage(cvSize(image.getWidth(), image.getHeight()), IPL_DEPTH_8U, 3);
		binimage = cvCreateImage(cvSize(image.getWidth(), image.getHeight()), IPL_DEPTH_8U, 1);

		storage = new CvMemStorage().create();

		moments = new CvMoments(Loader.sizeof(CvMoments.class));
	}

	@Override
	public PositionVector getPositionVector() {
		if (objectPositionX == 0 && objectPositionY == 0) {
			return new PositionVector(0.0, 0.0);
		}

		return new PositionVector(((double) objectPositionX / (double) image.getWidth() - 0.5) * 2.0,
				((double) objectPositionY / (double) image.getHeight() - 0.5) * 2.0);
	}

	@Override
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public void getResult() {
		if (frame == null) {
			throw new RuntimeException("Frame is null.");
		}
		frame = IplImage.createFrom(image);
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

		pressedKey = (char) cvWaitKey(50);

	}

	@Override
	public char getPressedKey() {
		return pressedKey;
	}

	@Override
	public void dispose() {
		cvReleaseMemStorage(storage);
	}
}
