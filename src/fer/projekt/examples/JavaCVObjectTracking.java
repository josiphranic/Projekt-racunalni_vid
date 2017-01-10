package fer.projekt.examples;

import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;

import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_calib3d.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;

@SuppressWarnings("javadoc")
public class JavaCVObjectTracking {

	private static int FRAME_HEIGHT = 640;
	private static int FRAME_WIDTH = 480;

	private int objectPositionX;
	private int objectPositionY;

	public static void main(String[] args) {

		CvCapture capture = cvCreateCameraCapture(CV_CAP_ANY);
		
		IplImage frame;
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

		while (true) {
			frame = cvQueryFrame(capture);
			
			if (frame == null) {
				break;
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
					cvDrawContours(binimage, contour2, CV_RGB(0, 0, 0), CV_RGB(0, 0, 0), 0, CV_FILLED, 8,
							cvPoint(0, 0));
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

			char c = (char) cvWaitKey(50);

			if (c == 27) {
				break;
			}

		}

		cvReleaseCapture(capture);
		cvReleaseMemStorage(storage);

	}

}
