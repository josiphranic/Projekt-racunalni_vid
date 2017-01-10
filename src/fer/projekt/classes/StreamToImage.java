package fer.projekt.classes;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public class StreamToImage {

	private JPEGImageDecoder decoder;
	
	public StreamToImage(DataInputStream dis){
		decoder = JPEGCodec.createJPEGDecoder(dis);
	}
	
	public BufferedImage getImage() throws ImageFormatException, IOException{
		return decoder.decodeAsBufferedImage();
	}
}
