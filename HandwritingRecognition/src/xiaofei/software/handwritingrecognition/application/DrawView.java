package xiaofei.software.handwritingrecognition.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.app.Activity;

public class DrawView extends ImageView
{
	public Path path=new Path();
	public float preX,preY;
	private int width,height;
	private Paint paint=new Paint();
	public DrawView(Context context)
	{
		super(context);
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(10);
		paint.setDither(true);
		paint.setAntiAlias(true);
		DisplayMetrics dm=new DisplayMetrics();
		((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
		width=dm.widthPixels;
        height=dm.heightPixels;
        Paths.reset();
	}
	@Override
	public void onDraw(Canvas canvas)
	{
		Bitmap bm=Bitmap.createBitmap(width,height,Config.ARGB_8888);
		canvas.drawBitmap(bm,0,0,null);
		canvas.drawPath(path,paint);
	}
	public void resetPath()
	{
		path.reset();
	}
	
}