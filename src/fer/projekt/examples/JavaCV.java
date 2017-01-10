package fer.projekt.examples;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

public class JavaCV {

	public static void main(String[] args) {

			IplImage image = cvLoadImage("alvin_and_the_chipmunks.jpg");

			IplImage hsvimage = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 3);
			IplImage grayimage = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);

			cvCvtColor(image, hsvimage, CV_BGR2HSV);
			cvCvtColor(image, grayimage, CV_BGR2GRAY);

			cvShowImage("Original", image);
			cvShowImage("HSV", hsvimage);
			cvShowImage("GRAY", grayimage);

			cvWaitKey();

			cvReleaseImage(image);
			cvReleaseImage(hsvimage);
			cvReleaseImage(grayimage);

	}
}
