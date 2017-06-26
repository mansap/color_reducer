/**
  *
  *  Title: ReduceColor.java
  *  
  *  Description: This class implements the methods for 8-bit color reduction 
  *  			  task to reduces colors from 256 to 5 colors in digital image 
  *  			  files.
  *  
  *  Along with the main() method, it has the following methods:
  *  	1. createDataSet()
  *  	2. runKMeans()
  *  	3. assignClusters()
  *  	4. findColor()
  *  	5. max()
  *  	6. checkColor()
  *  	In addition to this, it also has an inner static class called color.
  *  	This class has three variables r,g,b to represent the 
  *  	corresponding Red, Green and Blue values.
  *	
  *	 Other classes imported are:
  *		1. SimplePicture.java
  *		2. PictureFrame.java
  *		3. Pixel.java
  *		4. ImageDisplay.java
  *		5. Picture.java
  *  Author for all the above files is: Dr.Trudy Howles.
  *  Reference : https://www.cs.rit.edu/~tmh/courses/720-2016/
  *  It uses the Media Computation library developed at Georgia 
  *  Institute of Technology (Barbara Ericson).
  *
  *  Author(s) for ReduceColor.java:
  *  		  1. Mansa Pabbaraju
  *  		  2. Bhanu Nallabothula
  *  		  3. Palash Gandhi
  *  
  *  References for the following code:
  *  1. http://stackoverflow.com/questions/12953958/how-to-create-an-
  *                                             arff-file-from-an-array-in-java
  *  2. https://ianma.wordpress.com/2010/01/16/weka-with-java-eclipse-
  *                                                            getting-started/
  *  3. http://stackoverflow.com/questions/12953958/how-to-create-an-
  *                                             arff-file-from-an-array-in-java
  *  
  *  References for code from WEKA:
  *  1. http://weka.sourceforge.net/doc.stable/weka/core/Instances.html
  *  2. http://weka.sourceforge.net/doc.stable/weka/core/Instance.html
  *  3. http://weka.sourceforge.net/doc.stable/weka/core/Attribute.html
  *  4. http://weka.sourceforge.net/doc.dev/weka/clusterers/SimpleKMeans.html
  *  	
  *  The Kmeans clustering algorithm used is the inbuilt SimplekMeans algorithm 
  *  provided by WEKA. 
  *  Reference: http://www.java2s.com/Code/Jar/w/weka.htm
  *  	
  *  Date:    16th October 2016
  *
**/

import weka.clusterers.SimpleKMeans;
import weka.clusterers.Clusterer;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;

/** 
 *  class ReduceColor provides the main function and the methods to 
 *  implement color reduction of a digital image from 256 colors to 5 colors.
 *  These 5 colors chosen are : White, Black, Red, Green and Blue.
 *  It has a static class variable clustColor which designates appropriate 
 *  color out of the chosen 5 to the cluster based on how close the cluster 
 *  values are to each of the 5 colors. 
 *
 *  @param args - an array of command line arguments
 *  @throws Exception 
 **/

public class ReduceColor 
{
	static int[] clustColor = new int[5];
	
	/** 
     *  main() This program expects a digital file name as the
     *  only required command line argument.  Note that for
     *  brevity, it does not perform any error checking. 
     *
     *  @param args - an array of command line arguments
     *  @throws Exception 
     **/
    public static void main (String args[] ) throws Exception 
    {
        Picture pic = new Picture (args[0]);
        /* 1D array of pixel objects representing each pixel in image */

        Pixel [] pixels = pic.getPixels();
        /* Loop to extract pixels */
        for (int loop = 0; loop < pixels.length; loop++) 
        {
            Pixel p = pixels [loop];
            /* get the original color of each pixel */
            p.red_value = p.getRed();        
            p.blue_value = p.getBlue();
            p.green_value = p.getGreen();
        } 
        /* Loop to extract pixels */
        ReduceColor rr = new ReduceColor();
        Instances data = rr.createDataSet(pixels);
        rr.runKMeans(pixels,data);
        pic.show();
    }
       
	/** 
	 *  Method: createDataSet()
	 *  Description: The given input format is in the form of pixels. 
	 *  			 As we use SimpleKMeans clustering algorithm from Weka,
	 *  			 which takes an input in the form of Instances type, 
	 *  			 we need to convert this array of pixels .
	 *   			 into an Instances format. 
	 *   			 This is done by the method createDataSet().
     *	
     *  @param  pixels -  This is an array of Pixel objects of length as the 
     *  				  number of pixels in the image.
     *  @throws Exception 
     *  @return dataset - containing all the instances for input digital image.
     *  				  Every pixel in the input image is converted into a 
     *  				  corresponding instance having attributes Red, Green 
     *  			      and Blue.
     **/
    /* Creates dataset in arff format from array values */
    public Instances createDataSet (Pixel[] pixels) throws Exception
    {   	
    	/*********************Create Attributes**************************/
    	ArrayList<Attribute> attrList = new ArrayList<Attribute>();
    	ArrayList<Instance> instances = new ArrayList<Instance>();
    	
    	/************************Attribute Array List ********************/
    	/* Declare 3 attributes - R,G,B, these should take 0-255 int values */
              
        Attribute Attr1 = new Attribute("R");
        Attribute Attr2 = new Attribute("G");
        Attribute Attr3 = new Attribute("B");
        
        attrList.add(Attr1);
        attrList.add(Attr2);
        attrList.add(Attr3);
        /******************Fill attribute values ********************/
        
        for(int obj = 0; obj < pixels.length; obj++)
        {
            instances.add(new SparseInstance(3));
        }
        /* h variable loops over the 3 attributes - R,G and B */
        for(int h = 0; h < 3; h++)
        {
        	int val = 0;
               for(int i = 0; i < pixels.length; i++)
               {  
            	   if (h == 0)
            		   val = pixels[i].red_value;
                   if (h == 1)
                	   val = pixels[i].green_value;
                   if (h == 2)
                       val = pixels[i].blue_value;
                       
                   instances.get(i).setValue(h,val);
               }
        }
        /************************create data set ********************/
        Instances newDataset = new Instances("Dataset",attrList, pixels.length);
        for(Instance inst : instances)
        {
            newDataset.add(inst);
        }
        return newDataset;
    }
    
	/** 
	 *  Method: runKMeans()
	 *  Description: This method runs the SimpleKMeans algorithm 
	 *  			 provided in Weka to form clusters. We give the size of 
	 *  			 k = 5 as we need to reduce 256 colors to 5 colors.
	 *       		 The centroids of the clusters formed are analysed and 
	 *       		 colors are assigned to every pixel point belonging to 
	 *       		 that cluster according to result of analysis.
     *	
     *  @param  pixels -  This is an array of Pixel objects of length as the 
     *  				  number of pixels in the image.
     *  @param  data   -  dataset of all pixels represented in Instances form.
     *  @throws Exception 
     *  @return none
     **/
     
   /* Run K means Algorithm */ 
   public void runKMeans(Pixel[] pixels,Instances data) 
   {    
        SimpleKMeans kMeansObj = new SimpleKMeans();
        double[][] centroids;
        Instances op;
        int[] clusters = null;
        
        /* Preserve instances order */
        kMeansObj.setPreserveInstancesOrder(true);
        try 
        {
        	/* Set k = 5, as need to reduce to 5 colors */
        	kMeansObj.setNumClusters(5);
        	kMeansObj.buildClusterer(data);
	        clusters = kMeansObj.getAssignments();
		} catch (Exception e) 
        {
			e.printStackTrace();
		}
     
		/********************** set clusters for pixels ********************/
		/* set the pixel values to 5 colors based on clustering */
		
		for(int pix = 0; pix < pixels.length; pix++)
		{
			pixels[pix].cluster = clusters[pix];
		}
		
		centroids = new double[5][3];
		op = kMeansObj.getClusterCentroids();
		for(int clust = 0; clust < op.size(); clust++)
		{
			for(int colorComp = 0; colorComp < 3; colorComp++)
			{
				centroids[clust][colorComp]= op.instance(clust).value(colorComp);
			}
		}
		
		/*Assign color value to each clusters*/
		this.assignClusters(centroids);
		
		/* Assign colors to all pixel values */
		for(int pix = 0; pix < pixels.length; pix++)
		{
			color colorObj = checkColor(pixels[pix].cluster);
			pixels[pix].setRed(colorObj.r);
			pixels[pix].setGreen(colorObj.g);
			pixels[pix].setBlue(colorObj.b);
		}
    }
   
	/** 
	 *  Method: assignClusters()
	 *  Description: This method analyses the centroids of the 5 clusters formed.
	 *  			 Based on the closeness of each cluster centroid to the 
	 *  			 5 chosen colors: White(255,255,255), Black(0,0,0),
	 *  			 Red(255,0,0), Green(0,255,0) and Blue(0,0,255),
	 *  			 pixels of the image are re-colored.
     *	
     *  @param  centroids -  
     *   			This is an array of centroids of the clusters formed 
     *  			by the KMeans Algorithm. Each centroid is given 
     *  			in the form of (R,G,B) values.
     *  @return none
     **/
   public void assignClusters(double[][] centroids)
   {
	   	int ind = 0;
		for(int g = 0; g < 5; g++)
		{
			if(centroids[g][0] > 150 && centroids[g][1] > 150 && centroids[g][2] > 150)
			{
				/* White */
				clustColor[ind] = 0;
			}
			else if(centroids[g][0] < 60 && centroids[g][1] < 50 && centroids[g][2] < 50)
			{
				/* Black */
				clustColor[ind] = 4;
			}
			else
			{
				double dVal = findColor(centroids[g][0],centroids[g][1],centroids[g][2]);
				
				/* Red */
				if ((dVal ==  centroids[g][0]))
				{
					if((centroids[g][0] - centroids[g][1]) > 20)
					{
						clustColor[ind] = 1;
					}
					else
					{
						if((centroids[g][1] - centroids[g][2] > 20))
						{
							clustColor[ind] = 2;
						}
						else
						{
							clustColor[ind] = 3;
						}
					}
				}
				/* Green */
				else if (dVal ==  centroids[g][1])
				{
					clustColor[ind] = 2;
				}
				/* Blue */
				else 
				{
					clustColor[ind] = 3;
				}
			}
			ind++;
		}
   }
   
	/** 
	 *  Method: findColor()
	 *  Description: This method finds the most dominant color component for 
	 *  			 particular value of pixel(in this case, all input values 
	 *  			 will be the R,G,B values for the 5 centroids
     *  @param  r -  Red color component 
     *  @param  g -  Red color component
     *  @param  b -	 Red color component
     *  @return max value of r,g,b color components
     **/
    /* Finds most dominant color between r,g and b values */
    public double findColor(double r, double g, double b)
    {
    	return max(max(r,g),b);
    }
    
	/** 
	 *  Method: max()
	 *  Description: Returns maximum of two values
     *  @param  a -  First value 
     *  @param  b -  Second value
     *  @return max value of a and b
     **/
    /* Returns max of two values */
    public double max(double a , double b)
    {
    	if (a>b)
    		return a;
		else
			return b;
    }
    
	/** 
	 *  Method: checkColor()
	 *  Description: Assign color values to clusters based on assigned 
	 *  			 cluster indices. Cluster indices are assigned colors as:
	 *  			 0 - White
	 *  			 1 - Red
	 *  			 2 - Green
	 *  			 3 - Blue
	 *   			 4 - Black
     *  @param  cluster -  The cluster to be assigned color 
     *  @return colorObj -  An object with the correct R,G and B values assigned 
     *  					as per the required color.
     **/
    public color checkColor(int cluster)
    {
    	int val = clustColor[cluster];
    	color colorObj = new color();
    	/*White*/
    	if (val == 0)
    	{
    		colorObj.r = 255;
    		colorObj.g = 255;
    		colorObj.b = 255;
    	}
    	/* Red */
    	else if (val == 1)
    	{
    		colorObj.r = 255;
    		colorObj.g = 0;
    		colorObj.b = 0;
    	}
    	/* Green */
    	else if (val == 2)
    	{
    		colorObj.r = 0;
    		colorObj.g = 255;
    		colorObj.b = 0;
    	}
    	/* Blue */
    	else if (val == 3)
    	{
    		colorObj.r = 0;
    		colorObj.g = 0;
    		colorObj.b = 255;
    	}
    	/* Black */
    	else
    	{
    		colorObj.r = 0;
    		colorObj.g = 0;
    		colorObj.b = 0;
    	}
    	return colorObj;
    }
    
    /* class with three attributes Red, Green and Blue color components
     * to represent a particular pixel in an image */
    public static class color
    {
    	int r;
    	int g; 
    	int b;
    }    
}
