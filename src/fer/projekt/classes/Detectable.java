package fer.projekt.classes;

import java.awt.image.BufferedImage;

public interface Detectable {
	
	public PositionVector getPositionVector();

	public void setImage(BufferedImage image);
	
	public void dispose();
	
	public void getResult();
	
	public char getPressedKey();
}
