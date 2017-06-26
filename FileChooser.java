import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.util.Properties;
import java.io.*;
  
/**
 * A class to make working with a file chooser easier
 * for students.  It uses a JFileChooser to let the user
 * pick a file and returns the choosen file name.
 * 
 * Copyright Georgia Institute of Technology 2004
 * @author Barb Ericson ericson@cc.gatech.edu
 */
public class FileChooser 
{

  ///////////////////////////// class fields ///////////////////
   /**
   * Properities to use during execution
   */
  private static Properties appProperties = null;
  
  /**
   * Property key for the media directory
   */
  private static final String MEDIA_DIRECTORY = "mediaDirectory";
  
  /**
   * Name for property file
   */
  private static final String PROPERTY_FILE_NAME = 
    "SimplePictureProperties.txt";
  
  /////////////////////// methods /////////////////////////////
 
  /**
   * Method to let the user pick a file and return
   * the full file name as a string.  If the user didn't 
   * pick a file then the file name will be null.
   * @return the full file name of the picked file or null
   */
  public static String pickAFile()
  {
    JFileChooser fileChooser = null;
    
    // start off the file name as null
    String fileName = null;
    
    // get the current media directory
    String mediaDir = getMediaDirectory();
    
    /* create a file for this and check that the directory exists
     * and if it does set the file chooser to use it
     */
    try {
      File file = new File(mediaDir);
      if (file.exists())
        fileChooser = new JFileChooser(file);
    } catch (Exception ex) {
    }
    
    // if no file chooser yet create one
    if (fileChooser == null)
      fileChooser = new JFileChooser();
    
    /* create a JFrame to be the parent of the file 
     * chooser open dialog if you don't do this then 
     * you may not see the dialog.
     */
    JFrame frame = new JFrame();
    
    // get the return value from choosing a file
    int returnVal = fileChooser.showOpenDialog(frame);
    
    // if the return value says the user picked a file 
    if (returnVal == JFileChooser.APPROVE_OPTION)
      fileName = fileChooser.getSelectedFile().getPath();
    
    return fileName;
  }
  
   /**
  * Method to get the full path for the passed file name
  * @param fileName the name of a file
  * @return the full path for the file
  */
 public static String getMediaPath(String fileName) 
 {
   String path = null;
   String directory = getMediaDirectory();
   
   // get the full path
   path = directory + fileName;
   
   return path;
 }
 
 /**
  * Method to get the directory for the media
  * @return the media directory
  */
 public static String getMediaDirectory() 
 {
   String directory = null;
   
   // check if the application properties are null
   if (appProperties == null)
   {
     appProperties = new Properties();
     
     // load the properties from a file
     try {
       FileInputStream in = new FileInputStream(PROPERTY_FILE_NAME);
       appProperties.load(in);
       in.close();
     } catch (Exception ex) {
       directory = "c:/intro-prog-java/mediasources/";
     }
   }
   
   // get the media directory
   if (appProperties != null)
     directory = (String) appProperties.get(MEDIA_DIRECTORY);
   
   return directory;
 }
 
 /**
  * Method to set the media path by setting the directory to use
  * @param directory the directory to use for the media path
  */
 public static void setMediaPath(String directory) 
 { 
   
   // check if the directory exists
   File file = new File(directory);
   if (!file.exists())
     System.out.println("Sorry but " + directory + 
                 " doesn't exist, try a different directory.");
   else {
     
     /* check if there is an application properties object 
      * and if not create one
      */
     if (appProperties == null)
       appProperties = new Properties();
     
     // set the media directory property
     appProperties.put(MEDIA_DIRECTORY,directory);
     
     // write out the application properties to a file
     try {
       FileOutputStream out = 
         new FileOutputStream(PROPERTY_FILE_NAME);
       appProperties.store(out, 
                     "Properties for the Simple Picture class");
       out.close();
       System.out.println("The media directory is now " + 
                          directory);
     } catch (Exception ex) {
       System.out.println("Couldn't save media path to a file");
     }
   }
 }
   
}