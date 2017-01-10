package fer.projekt.examples;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JFrame;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public class StreamCatcher {


    private static final long serialVersionUID = 1L;
    public boolean useMJPGStream = true;
    public String jpgURL = "http://169.254.12.213/video.cgi/jpg/image.cgi?resolution=640×480";
    public String mjpgURL = "http://169.254.12.213/video.cgi/mjpg/video.cgi?resolution=640×480";
    DataInputStream dis;
   
    private BufferedImage image = null;
    public Dimension imageSize = null;
    public boolean connected = false;
    private boolean initCompleted = false;
    HttpURLConnection huc = null;
    static Component parent;
   
    public StreamCatcher(JFrame parent_) {
        parent = parent_;
    }
       
   
 
    public void connect() {
        try {
            URL u = new URL(useMJPGStream ? mjpgURL : jpgURL);
            huc = (HttpURLConnection) u.openConnection();
 
            connected = true;
            dis = new DataInputStream(new BufferedInputStream(huc.getInputStream()));
            if (!initCompleted)
                initDisplay();
        } catch (IOException e) {
           
            try {
                huc.disconnect();
                Thread.sleep(60);
            } catch (InterruptedException ie) {
                huc.disconnect();
                connect();
            }
            connect();
        } catch (Exception e) {
            ;
        }
    }
 
    public void initDisplay() { // setup the display
        if (useMJPGStream)
            readMJPGStream();
        else {
            readJPG();
            disconnect();
        }
        imageSize = new Dimension(image.getWidth((ImageObserver) this), image.getHeight((ImageObserver) this));
        parent.setPreferredSize(imageSize);
        parent.setSize(imageSize);
        parent.validate();
        initCompleted = true;
    }
 
    public void disconnect() {
        try {
            if (connected) {
                dis.close();
                connected = false;
            }
        } catch (Exception e) {
            ;
        }
    }
 
    public void paint(Graphics g) {
        if (image != null)
            g.drawImage(image, 0, 0, (ImageObserver) this);
    }
 
    public void readStream() {
       
        try {
            if (useMJPGStream) {
                while (true) {
 
                    readMJPGStream();
                    parent.repaint();
                }
            } else {
                while (true) {
                    connect();
                    readJPG();
                    parent.repaint();
                    disconnect();
 
                }
            }
 
        } catch (Exception e) {
            
        }
    }
 
    public void readMJPGStream() { // preprocess the mjpg stream to remove the
                                    // mjpg encapsulation
        readLine(4, dis); // discard the first 4 lines
        readJPG();
        readLine(2, dis); // discard the last two lines
    }
 
    public void readJPG() { // read the embedded jpeg image
        try {
 
 
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(dis);
            image = decoder.decodeAsBufferedImage();
 
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
        }
 
    }
 
    public void readLine(int n, DataInputStream dis) {
       
        for (int i = 0; i < n; i++) {
            readLine(dis);
        }
    }
 
    public void readLine(DataInputStream dis) {
        try {
            boolean end = false;
            String lineEnd = "\n";
           
            byte[] lineEndBytes = lineEnd.getBytes();
            byte[] byteBuf = new byte[lineEndBytes.length];
 
            while (!end) {
                dis.read(byteBuf, 0, lineEndBytes.length);
                String t = new String(byteBuf);
                System.out.print(t);
               
                if (t.equals(lineEnd))
                    end = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
    public void run() {
        System.out.println("in Run...................");
        connect();
        readStream();
    }
   
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
 
        JFrame jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StreamCatcher axPanel = new StreamCatcher(jframe);
        //new Thread().start();
        axPanel.run();
        //* axPanel.readJPG(); reads the jpg image..
        //* jframe.getContentPane().add(axPanel);
        jframe.pack();
        jframe.setVisible(true);
    }
}
