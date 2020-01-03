package Algorithm;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.awt.Color;
public class ImageBright {

	private BufferedImage img;
	private int height,width;
	private int scale;
	//private String name;

	public ImageBright(){}

	public void loadImage(String name) throws IOException{
	
			img = ImageIO.read(new File(name));
			height = img.getHeight();
			width = img.getWidth();
			
	}

	public String brightens(String path) throws IOException{
		int i,j;
		 scale=4;
	
			for(i=0;i<height;i++){
				for(j=0;j<width;j++){
					Color c = new Color(img.getRGB(j,i));
					int r = c.getRed();
					int b = c.getBlue();
					int g = c.getGreen();

					int nr = (int)(r + 15.5*scale);
					int nb = (int)(b + 15.5*scale);
					int ng = (int)(g + 15.5*scale);

					nr = Math.min(nr,255);
					ng = Math.min(ng,255);
					nb = Math.min(nb,255);

					Color nc = new Color(nr,ng,nb);
					img.setRGB(j,i,nc.getRGB());
				}
			}
			String newPath=path.substring(0, 42)+"Bright"+path.substring(42, path.length());
	    	File outputfile = new File(newPath);
			ImageIO.write(img,"png",outputfile);
	
	
			return newPath;
	}


}