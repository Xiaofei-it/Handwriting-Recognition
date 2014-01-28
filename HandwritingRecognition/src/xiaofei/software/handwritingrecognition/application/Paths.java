package xiaofei.software.handwritingrecognition.application;

class Point
{
	public final float x,y;
	public Point(float a,float b)
	{
		x=a;y=b;
	}
}
/** The class "Paths" records the user's handwriting patterns.
 *
 */
public class Paths {
	private static final int MAXL=200;
	private static final int MAXP=10000;
	//The array "paths" records the points the user touches on the screen.
	private static final Point[][] paths=new Point[MAXL][MAXP];
	private static final int[] last=new int[MAXL];
	private static int li=-1;
	public static void reset()
	{
		li=-1;
	}
	public static void addPath()
	{
		if (li+1==MAXL)
			return;
		last[++li]=-1;
	}
	public static void addPoint(float x,float y)
	{
		if (last[li]+1==MAXP)
			return;
		paths[li][++last[li]]=new Point(x,y);
	}
	/** The function "process" takes the array "path" as input and returns as output a MAXN-by-MAXN
	 * matrix, where the space touched by the user is labeled with a boolean value "true" and "false"
	 * otherwise.
	 */
	public static boolean[][] process()
	{
		//Find the smallest rectangle that covers all the points.
		float minx=Float.MAX_VALUE,maxx=Float.MIN_VALUE,miny=minx,maxy=maxx;
		if (li==-1)
			return null;
		for (int i=0;i<=li;i++)
			for (int j=0;j<=last[i];j++)
			{
				if (paths[i][j].x<minx)
					minx=paths[i][j].x;
				if (paths[i][j].x>maxx)
					maxx=paths[i][j].x;
				if (paths[i][j].y<miny)
					miny=paths[i][j].y;
				if (paths[i][j].y>maxy)
					maxy=paths[i][j].y;
			}
		if (minx==maxx)
			maxx++;
		if (miny==maxy)
			maxy++;
		//Map all the points to a MAXN-by-MAXN matrix.
		class P
		{
			public int x,y;
			public P(float a,float b)
			{
				x=(int)a;y=(int)b;
				if (x==Const.MAXN)
					x--;
				if (y==Const.MAXN)
					y--;
			}
		}
		P[][] temp=new P[MAXL][MAXL];
		for (int i=0;i<=li;i++)
			for (int j=0;j<=last[i];j++)
			{
				temp[i][j]=new P(
						(paths[i][j].x-minx)*1.0f/(maxx-minx)*Const.MAXN,
						(paths[i][j].y-miny)*1.0f/(maxy-miny)*Const.MAXN);
			}
		//Fill in the blank spaces between two consecutive points.
		boolean[][] map=new boolean[Const.MAXN][Const.MAXN];
		for (int i=0;i<Const.MAXN;i++)
			for (int j=0;j<Const.MAXN;j++)
				map[i][j]=false;
		for (int i=0;i<=li;i++)
		{
			map[temp[i][0].x][temp[i][0].y]=true;
			for (int j=1;j<=last[i];j++)
			{
				map[temp[i][j].x][temp[i][j].y]=true;
				if (temp[i][j-1].x==temp[i][j].x)
				{
					int s,d;
					if (temp[i][j-1].y<temp[i][j].y)
					{
						s=temp[i][j-1].y;d=temp[i][j].y;
					}
					else
					{
						s=temp[i][j].y;d=temp[i][j-1].y;
					}
					for (int k=s;k<=d;k++)
						map[temp[i][j].x][k]=true;
				}
				else if (temp[i][j-1].x<temp[i][j].x)
				{
					int dx=temp[i][j].x-temp[i][j-1].x,
							dy=temp[i][j].y-temp[i][j-1].y;
					if (dy<0)
						dy=-dy;
					if (dx>=dy)
					{
						double kk=(temp[i][j].y-temp[i][j-1].y)*1.0/(temp[i][j].x-temp[i][j-1].x);
						for (int k=temp[i][j-1].x;k<=temp[i][j].x;k++)
							map[k][temp[i][j-1].y+(int)(kk*(k-temp[i][j-1].x))]=true;
					}
					else if (temp[i][j-1].y<temp[i][j].y)
					{
						double kk=(temp[i][j].x-temp[i][j-1].x)*1.0/(temp[i][j].y-temp[i][j-1].y);
						for (int k=temp[i][j-1].y;k<=temp[i][j].y;k++)
							map[temp[i][j-1].x+(int)(kk*(k-temp[i][j-1].y))][k]=true;
					}
					else if (temp[i][j-1].y>temp[i][j].y)
					{
						double kk=(temp[i][j-1].x-temp[i][j].x)*1.0/(temp[i][j-1].y-temp[i][j].y);
						for (int k=temp[i][j].y;k<=temp[i][j-1].y;k++)
							map[temp[i][j].x+(int)(kk*(k-temp[i][j].y))][k]=true;
					}
					else
					{
						for (int k=temp[i][j-1].x;k<=temp[i][j].x;k++)
							map[k][temp[i][j].y]=true;
					}
				}
				else
				{
					int dx=temp[i][j-1].x-temp[i][j].x,
							dy=temp[i][j].y-temp[i][j-1].y;
					if (dy<0)
						dy=-dy;
					if (dx>=dy)
					{
						double kk=(temp[i][j-1].y-temp[i][j].y)*1.0/(temp[i][j-1].x-temp[i][j].x);
						for (int k=temp[i][j].x;k<=temp[i][j-1].x;k++)
							map[k][temp[i][j].y+(int)(kk*(k-temp[i][j].x))]=true;
					}
					else if (temp[i][j-1].y<temp[i][j].y)
					{
						double kk=(temp[i][j].x-temp[i][j-1].x)*1.0/(temp[i][j].y-temp[i][j-1].y);
						for (int k=temp[i][j-1].y;k<=temp[i][j].y;k++)
							map[temp[i][j-1].x+(int)(kk*(k-temp[i][j-1].y))][k]=true;
					}
					else if (temp[i][j-1].y>temp[i][j].y)
					{
						double kk=(temp[i][j-1].x-temp[i][j].x)*1.0/(temp[i][j-1].y-temp[i][j].y);
						for (int k=temp[i][j].y;k<=temp[i][j-1].y;k++)
							map[temp[i][j].x+(int)(kk*(k-temp[i][j].y))][k]=true;
					}
					else
					{
						for (int k=temp[i][j].x;k<=temp[i][j-1].x;k++)
							map[k][temp[i][j].y]=true;
					}
				}
			}
		}
		return map;
	}
}
