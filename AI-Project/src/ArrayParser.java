
/**
 * Class for parsing strings into either single dimensional int or double
 * arrays, 2D double arrays, or 3D double arrays.
 * 
 * @author Adam
 *
 */
public class ArrayParser
{
	/**
	 * Parses a string into an int array.
	 * @param in String to be parsed.
	 * @return The int array represented by in
	 */
	static int[] parseIntArray(String in)
	{
		String[] outStrings = in.replace("[", "").replace("]", "").replace(" ", "").split(",");
		int[] out = new int[outStrings.length];
		for (int i = 0; i < out.length; i++)
		{
			
			out[i] = Integer.parseInt(outStrings[i]);
			
		}
		return out;
	}
	
	/**
	 * Parses a string into an double array.
	 * @param in String to be parsed.
	 * @return The double array represented by in.
	 */
	static double[] parseDoubleArray(String in)
	{
		String[] outStrings = in.replace("[", "").replace("]", "").replace(" ", "").split(",");
		double[] out = new double[outStrings.length];
		for (int i = 0; i < out.length; i++)
		{
			
			out[i] = Double.parseDouble(outStrings[i]);
			
		}
		return out;
	}
	
	/**
	 * Parses a string into an 2D double array.
	 * @param in String to be parsed.
	 * @return The 2D array represented by in.
	 */
	static double[][] parseDouble2DArray(String in)
	{
		in = in.replace(" ", "");
		String[] outStrings = in.split("],");
		double[][] out = new double[outStrings.length][];
		for (int i = 0; i < outStrings.length; i++)
		{
			out[i] = parseDoubleArray(outStrings[i]);
		}
		return out;
	}
	
	/**
	 * Parses a string into a 3D double array.
	 * @param in String to be parsed.
	 * @return The 3D double array represented by in.
	 */
	static double[][][] parseDouble3DArray(String in)
	{
		in = in.replace(" ", "");
		String[] outStrings = in.split("]],");
		double[][][] out = new double[outStrings.length][][];
		for (int i = 0; i < outStrings.length; i++)
		{
			out[i] = parseDouble2DArray(outStrings[i]);
		}
		return out;
	}
}
