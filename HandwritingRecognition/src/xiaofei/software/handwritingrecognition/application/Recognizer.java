package xiaofei.software.handwritingrecognition.application;

/** This class is a naive Bayes classifier.
 * 
 */
public class Recognizer
{
	private static final int MAXN=Const.MAXN; 
	private static final int MAXNUM=Const.MAXNUM;
	private static final int[][][][] count=new int[MAXN][MAXN][2][MAXNUM+1];
	private static final int[] count2=new int[MAXNUM+1];
	private static final double[][][][] po=new double[MAXN][MAXN][2][MAXNUM+1];
	private static final double[] po2=new double[MAXNUM+1];
	public static void init()
	{
		for (int i=0;i<MAXN;i++)
			for (int j=0;j<MAXN;j++)
				for (int k=0;k<=MAXNUM;k++)
					count[i][j][0][k]=count[i][j][1][k]=0;
		for(int i=0;i<=MAXNUM;i++)
			count2[i]=0;
	}
	public static void add(boolean[][] input,int n)
	{
		for (int i=0;i<MAXN;i++)
			for (int j=0;j<MAXN;j++)
				if (input[i][j])
					count[i][j][1][n]++;
				else
					count[i][j][0][n]++;
		count2[n]++;
	}
	public static void learn()
	{
		boolean flag=false;
		for (int i=0;i<=MAXNUM;i++)
			if (count2[i]==0)
			{
				flag=true;
				break;
			}
		if (flag)
		{
			for (int i=0;i<=MAXNUM;i++)
				count2[i]++;
		}
		int sum=0;
		for (int i=0;i<=MAXNUM;i++)
			sum+=count2[i];
		for (int i=0;i<=MAXNUM;i++)
			po2[i]=count2[i]*1.0/sum;
		for (int i=0;i<MAXN;i++)
			for (int j=0;j<MAXN;j++)
				for (int k=0;k<=MAXNUM;k++)
				{
					if (count[i][j][0][k]!=0&&count[i][j][1][k]!=0)
					{
						po[i][j][0][k]=count[i][j][0][k]*1.0/count2[k];
						po[i][j][1][k]=count[i][j][1][k]*1.0/count2[k];
					}
					else
					{
						po[i][j][0][k]=(count[i][j][0][k]+1)*1.0/(count2[k]+2);
						po[i][j][1][k]=(count[i][j][1][k]+1)*1.0/(count2[k]+2);
					}
				}
	}
	public static double[] recognize(boolean[][] input)
	{
		double[] result=new double[MAXNUM+1];
		for (int i=0;i<=MAXNUM;i++)
		{
			result[i]=1;
			for (int j=0;j<MAXN;j++)
				for (int k=0;k<MAXN;k++)
					if (input[j][k])
						result[i]*=po[j][k][1][i];
					else
						result[i]*=po[j][k][0][i];
			result[i]*=po2[i];
		}
		return result;
	}
}