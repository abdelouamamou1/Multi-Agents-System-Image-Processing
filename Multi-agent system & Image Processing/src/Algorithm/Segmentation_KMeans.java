package Algorithm;
import java.awt.image.BufferedImage; 
import java.io.File;
import java.io.IOException;
import java.util.Arrays; 
import javax.imageio.ImageIO;
 
public class Segmentation_KMeans { 
    BufferedImage original; 
    BufferedImage result; 
    Cluster[] clusters; 
    public static final int MODE_CONTINUOUS = 1; 
    public static final int MODE_ITERATIVE = 2; 
     
    public Segmentation_KMeans() {    } 
     
    public String calculate(String path,  int k, int mode) throws IOException { 
    	BufferedImage image=loadImage(path);
   
        int w = image.getWidth(); 
        int h = image.getHeight(); 
        // create clusters 
        clusters = createClusters(image,k); 
        // create cluster lookup table 
        int[] lut = new int[w*h]; 
        Arrays.fill(lut, -1); 
         
        // at first loop all pixels will move their clusters 
        boolean pixelChangedCluster = true; 
        // loop until all clusters are stable! 

        while (pixelChangedCluster) { 
            pixelChangedCluster = false; 
     
            for (int y=0;y<h;y++) { 
                for (int x=0;x<w;x++) { 
                    int pixel = image.getRGB(x, y); 
                    Cluster cluster = findMinimalCluster(pixel); 
                    if (lut[w*y+x]!=cluster.getId()) { 
                        // cluster changed 
                        if (mode==MODE_CONTINUOUS) { 
                            if (lut[w*y+x]!=-1) { 
                                // remove from possible previous  
                                // cluster 
                                clusters[lut[w*y+x]].removePixel( 
                                                            pixel); 
                            } 
                            // add pixel to cluster 
                            cluster.addPixel(pixel); 
                        } 
                        // continue looping  
                        pixelChangedCluster = true; 
                     
                        // update lut 
                        lut[w*y+x] = cluster.getId(); 
                    } 
                } 
            } 
            if (mode==MODE_ITERATIVE) { 
                // update clusters 
                for (int i=0;i<clusters.length;i++) { 
                    clusters[i].clear(); 
                } 
                for (int y=0;y<h;y++) { 
                    for (int x=0;x<w;x++) { 
                        int clusterId = lut[w*y+x]; 
                        // add pixels to cluster 
                        clusters[clusterId].addPixel( 
                                            image.getRGB(x, y)); 
                    } 
                } 
            } 
             
        } 
        // create result image 
        BufferedImage result = new BufferedImage(w, h,  
                                    BufferedImage.TYPE_INT_RGB); 
        for (int y=0;y<h;y++) { 
            for (int x=0;x<w;x++) { 
                int clusterId = lut[w*y+x]; 
                result.setRGB(x, y, clusters[clusterId].getRGB()); 
            } 
        } 
    
    	String newPath=path.substring(0, 42)+"Segmented"+path.substring(42, path.length());
    	File outputfile = new File(newPath);
		ImageIO.write(result, "png", outputfile);
		return newPath;
		
    } 
     
    public Cluster[] createClusters(BufferedImage image, int k) { 
        // Here the clusters are taken with specific steps, 
        // so the result looks always same with same image. 
        // You can randomize the cluster centers, if you like. 
        Cluster[] result = new Cluster[k]; 
        int x = 0; int y = 0; 
        int dx = image.getWidth()/k; 
        int dy = image.getHeight()/k; 
        for (int i=0;i<k;i++) { 
            result[i] = new Cluster(i,image.getRGB(x, y)); 
            x+=dx; y+=dy; 
        } 
        return result; 
    } 
     
    public Cluster findMinimalCluster(int rgb) { 
        Cluster cluster = null; 
        int min = Integer.MAX_VALUE; 
        for (int i=0;i<clusters.length;i++) { 
            int distance = clusters[i].distance(rgb); 
            if (distance<min) { 
                min = distance; 
                cluster = clusters[i]; 
            } 
        } 
        return cluster; 
    } 
     
    public static void saveImage(String filename,  
            BufferedImage image) { 
        File file = new File(filename); 
        try { 
            ImageIO.write(image, "png", file); 
        } catch (Exception e) { 
            System.out.println(e.toString()+" Image '"+filename 
                                +"' saving failed."); 
        } 
    } 
     
    public static BufferedImage loadImage(String filename) { 
        BufferedImage result = null; 
        try { 
            result = ImageIO.read(new File(filename)); 
        } catch (Exception e) { 
            System.out.println(e.toString()+" Image '" 
                                +filename+"' not found."); 
        } 
        return result; 
    } 
     
    
    //class cluster
    
    class Cluster { 
        int id; 
        int pixelCount; 
        int red; 
        int green; 
        int blue; 
        int reds; 
        int greens; 
        int blues; 
         
        public Cluster(int id, int rgb) { 
            int r = rgb>>16&0x000000FF;  
            int g = rgb>> 8&0x000000FF;  
            int b = rgb>> 0&0x000000FF;  
            red = r; 
            green = g; 
            blue = b; 
            this.id = id; 
            addPixel(rgb); 
        } 
         
        public void clear() { 
            red = 0; 
            green = 0; 
            blue = 0; 
            reds = 0; 
            greens = 0; 
            blues = 0; 
            pixelCount = 0; 
        } 
         
        int getId() { 
            return id; 
        } 
         
        int getRGB() { 
            int r = reds / pixelCount; 
            int g = greens / pixelCount; 
            int b = blues / pixelCount; 
            return 0xff000000|r<<16|g<<8|b; 
        } 
        void addPixel(int color) { 
            int r = color>>16&0x000000FF;  
            int g = color>> 8&0x000000FF;  
            int b = color>> 0&0x000000FF;  
            reds+=r; 
            greens+=g; 
            blues+=b; 
            pixelCount++; 
            red   = reds/pixelCount; 
            green = greens/pixelCount; 
            blue  = blues/pixelCount; 
        } 
         
        void removePixel(int color) { 
            int r = color>>16&0x000000FF;  
            int g = color>> 8&0x000000FF;  
            int b = color>> 0&0x000000FF;  
            reds-=r; 
            greens-=g; 
            blues-=b; 
            pixelCount--; 
            red   = reds/pixelCount; 
            green = greens/pixelCount; 
            blue  = blues/pixelCount; 
        } 
         
        int distance(int color) { 
            int r = color>>16&0x000000FF;  
            int g = color>> 8&0x000000FF;  
            int b = color>> 0&0x000000FF;  
            int rx = Math.abs(red-r); 
            int gx = Math.abs(green-g); 
            int bx = Math.abs(blue-b); 
            int d = (rx+gx+bx) / 3; 
            return d; 
        } 
    } 
     
} 