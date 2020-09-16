import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * I "borrowed" some code off the internet since I had no idea how to
 * read/interpret idx3-ubyte/idx1-ubyte files.
 * 
 * @author Adam
 *
 */
public class ImageReader
{
	public static void main(String[] args)
	{
		File trainFile = new File("Training Data");
		File testFile = new File("Testing Data");
		trainFile.mkdir();
		testFile.mkdir();
		
		FileInputStream inImage = null;
		FileInputStream inLabel = null;
		
		String inputImagePath = "train-images.idx3-ubyte";
		String inputLabelPath = "train-labels.idx1-ubyte";
		
		String outputPath = "Training Data//";
		
		int[] imgCounter = new int[10];
		
		try
		{
			inImage = new FileInputStream(inputImagePath);
			inLabel = new FileInputStream(inputLabelPath);
			
			int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			int numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			int numberOfRows = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			int numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			
			int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());
			int numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());
			
			BufferedImage image = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_INT_ARGB);
			int numberOfPixels = numberOfRows * numberOfColumns;
			int[] imgPixels = new int[numberOfPixels];
			
			for (int i = 0; i < numberOfImages; i++)
			{
				
				if (i % 100 == 0)
				{
					System.out.println("Number of images extracted: " + i);
				}
				
				for (int p = 0; p < numberOfPixels; p++)
				{
					int gray = 255 - inImage.read();
					imgPixels[p] = 0xFF000000 | (gray << 16) | (gray << 8) | gray;
				}
				
				image.setRGB(0, 0, numberOfColumns, numberOfRows, imgPixels, 0, numberOfColumns);
				
				int label = inLabel.read();
				
				imgCounter[label]++;
				File outputfile = new File(outputPath + label + "_0" + imgCounter[label] + ".png");
				
				ImageIO.write(image, "png", outputfile);
			}
			
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (inImage != null)
			{
				try
				{
					inImage.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (inLabel != null)
			{
				try
				{
					inLabel.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		inputImagePath = "t10k-images.idx3-ubyte";
		inputLabelPath = "t10k-labels.idx1-ubyte";
		
		outputPath = "Training Data//";
		
		imgCounter = new int[10];
		
		try
		{
			inImage = new FileInputStream(inputImagePath);
			inLabel = new FileInputStream(inputLabelPath);
			
			int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			int numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			int numberOfRows = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			int numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
			
			int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());
			int numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());
			
			BufferedImage image = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_INT_ARGB);
			int numberOfPixels = numberOfRows * numberOfColumns;
			int[] imgPixels = new int[numberOfPixels];
			
			for (int i = 0; i < numberOfImages; i++)
			{
				
				if (i % 100 == 0)
				{
					System.out.println("Number of images extracted: " + i);
				}
				
				for (int p = 0; p < numberOfPixels; p++)
				{
					int gray = 255 - inImage.read();
					imgPixels[p] = 0xFF000000 | (gray << 16) | (gray << 8) | gray;
				}
				
				image.setRGB(0, 0, numberOfColumns, numberOfRows, imgPixels, 0, numberOfColumns);
				
				int label = inLabel.read();
				
				imgCounter[label]++;
				File outputfile = new File(outputPath + label + "_0" + imgCounter[label] + ".png");
				
				ImageIO.write(image, "png", outputfile);
			}
			
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (inImage != null)
			{
				try
				{
					inImage.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (inLabel != null)
			{
				try
				{
					inLabel.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
