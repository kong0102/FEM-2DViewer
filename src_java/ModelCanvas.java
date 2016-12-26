/* 
Canvas
Kong
*/
import java.awt.*;
import java.io.*;

import java.awt.image.*;
import javax.imageio.*;


class ModelCanvas extends Canvas 
	{
	
	BufferedImage img;
	
	
	Graphics gb;
	
	
	public void paint(Graphics g) 
		{
	
		Dimension d = getSize();

		BufferedImage img = new BufferedImage(d.width, d.height,BufferedImage.TYPE_INT_RGB);

		gb = img.getGraphics();
						
		Main.model.draw(gb, d.width, d.height);
		
		g.drawImage(img, 0, 0, this);
		}
		
	public void update(Graphics g) 
		{
		paint(g);
		}
	}




