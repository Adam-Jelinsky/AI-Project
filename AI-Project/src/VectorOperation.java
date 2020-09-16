
/**
 * 
 * Class that contains static methods for vector operations
 * 
 * @author Adam
 *
 */
public class VectorOperation
{
	/**
	 * 
	 * @param m rectangular matrix
	 * @param v column vector with length of a row of m
	 * 
	 * @return m*v
	 */
	public static double[] matrixMult(double[][] m, double[] v)
	{
		double[] out = new double[m.length];
		for (int i = 0; i < m.length; i++)
		{
			out[i] = dotProd(m[i], v);
		}
		return out;
	}
	
	/**
	 * 
	 * @param m rectangular matrix
	 * @param v column vector with length of a row of m
	 *
	 * @return m^T * v
	 */
	public static double[] transposeMatrixMult(double[][] m, double[] v)
	{
		double[] out = new double[m[0].length];
		for (int col = 0; col < m[0].length; col++)
		{
			for (int row = 0; row < m.length; row++)
			{
				out[col] += v[row] * m[row][col];
			}
		}
		return out;
	}
	
	public static double dotProd(double[] v1, double[] v2)
	{
		double out = 0;
		for (int i = 0; i < v1.length; i++)
		{
			out += v1[i] * v2[i];
		}
		return out;
	}
	
	/**
	 * vector addition
	 * 
	 * @param v1 vector
	 * @param v2 vector
	 * @return v1 + v2
	 */
	public static double[] add(double[] v1, double[] v2)
	{
		double[] out = new double[v1.length];
		for (int i = 0; i < out.length; i++)
		{
			out[i] = v1[i] + v2[i];
		}
		return out;
	}
	
	/**
	 * vector subtraction
	 * 
	 * @param v1 vector
	 * @param v2 vector
	 * @return v1 - v2
	 */
	public static double[] subtract(double[] v1, double[] v2)
	{
		double[] out = new double[v1.length];
		for (int i = 0; i < out.length; i++)
		{
			out[i] = v1[i] - v2[i];
		}
		return out;
	}
	
	/**
	 * matrix addition
	 * 
	 * @param m1 matrix
	 * @param m2 matrix
	 * @return m1 - m2
	 */
	public static double[][] add(double[][] m1, double[][] m2)
	{
		double[][] out = new double[m1.length][];
		for (int row = 0; row < out.length; row++)
		{
			out[row] = new double[m1[row].length];
			for (int col = 0; col < out[row].length; col++)
			{
				out[row][col] = m1[row][col] + m2[row][col];
			}
		}
		return out;
	}
	
	/**
	 * 3D matrix addition
	 * 
	 * @param m1 matrix
	 * @param m2 matrix
	 * @return m1 + m2
	 */
	public static double[][][] add(double[][][] m1, double[][][] m2)
	{
		double[][][] out = new double[m1.length][][];
		for (int i = 0; i < m1.length; i++)
		{
			out[i] = add(m1[i], m2[i]);
		}
		return out;
	}
	
	/**
	 * multiplication of a matrix by a scalar
	 * 
	 * @param m matrix
	 * @param k scalar
	 * @return k*m
	 */
	public static double[][] scalarMult(double[][] m, double k)
	{
		double[][] out = new double[m.length][];
		for (int row = 0; row < m.length; row++)
		{
			out[row] = new double[m[row].length];
			for (int col = 0; col < m[row].length; col++)
			{
				out[row][col] = k * m[row][col];
			}
		}
		return out;
	}
	
	/**
	 * multiplication of a 3D matrix by a scalar
	 * 
	 * @param m 3D matrix
	 * @param k scalar
	 * @return k*m
	 */
	public static double[][][] scalarMult(double[][][] m, double k)
	{
		double[][][] out = new double[m.length][][];
		for (int i = 0; i < out.length; i++)
		{
			out[i] = scalarMult(m[i], k);
		}
		return out;
	}
	
	/**
	 * multiplication of a vector by a scalar
	 * 
	 * @param v vector
	 * @param k scalar
	 * @return k*v
	 */
	public static double[] scalarMult(double[] v, double k)
	{
		double[] out = new double[v.length];
		for (int i = 0; i < v.length; i++)
		{
			out[i] = k * v[i];
		}
		return out;
	}
	
	/**
	 * 
	 * @param v1 vector
	 * @param v2 vector
	 * @return the hadamard product of v1 and v2
	 */
	public static double[] vectProd(double[] v1, double[] v2)
	{
		double[] out = new double[v1.length];
		for (int i = 0; i < out.length; i++)
		{
			out[i] = v1[i] * v2[i];
		}
		return out;
	}
	
	/**
	 * 
	 * @param m rectangular matrix
	 * @return the transposition of m
	 */
	public static double[][] transpose(double[][] m)
	{
		double[][] out = new double[m[0].length][m.length];
		for (int row = 0; row < m.length; row++)
		{
			for (int col = 0; col < m[0].length; col++)
			{
				out[col][row] = m[row][col];
			}
		}
		return out;
	}
}
