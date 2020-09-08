package com.xl.view;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.xl.texturepacker.R;
import com.xl.game.tool.DisplayUtil;
//简单的调色板
public class ColorDraw extends View
{
	Paint paint;
	Context context;
	
	
	public ColorDraw(android.content.Context context)
	{
		super(context);
		initView(context);
	}

    public ColorDraw(android.content.Context context, android.util.AttributeSet attrs) 
	{
		super(context,attrs);
		initView(context);
	}
	
	void initView(Context context)
	{
		this.context = context;
		paint = new Paint();
		setPrassColorPacker();
		setCirColorPacker();
		int min = DisplayUtil.dip2px(getContext(),40);
setMinimumWidth(min);
setMinimumHeight(min);
	}
    
	
	
	
	public int getColor()
	{
		return paint.getColor();
	}
	
	public void setColor(int color)
	{
		paint.setColor(color);
		
		invalidate();
	}
	
	private int getColor(String text)
	{
		int color=0;
		int argb[]=new int[4];
		int start=0;
		int i=0;
		int hex=0; //颜色位数 有3 4 6 8
		for(i=0;i<text.length();i++)
		{
			if(text.charAt(i)=='#')
			{
				start=i+1;
				hex=text.length()-start;
			}

		}
		if(hex==3)
		{
			for(i=0;i<3;i++)
			{
				char c=text.charAt(start+i);
				argb[0]=0xff;
				if(c>='A'&&c<='F')
				{
					argb[i+1]=(c-'A'+10)*16;
				}
				else if(c>='a'&&c<='f')
				{
					argb[i+1]=(c-'a'+10)*16;
				}
				else if(c>='0'&&c<='9')
				{
					argb[i+1]=(c-'0')*16;
				}
			}
		}
		else if(hex==6)
		{
			argb[0]=0xff;
			for(i=0;i<3;i++)
			{
				char c=text.charAt(start+i*2);
				char c2=text.charAt(start+i*2+1);

				if(c>='A'&&c<='F')
				{
					argb[i+1]=(c-'A'+10)<<4;
				}
				else if(c>='a'&&c<='f')
				{
					argb[i+1]=(c-'a'+10)<<4;
				}
				else if(c>='0'&&c<='9')
				{
					argb[i+1]=(c-'0')<<4;
				}
				if(c2>='A'&&c2<='F')
				{
					argb[i+1]|=(c2-'A'+10);
				}
				else if(c2>='a'&&c2<='f')
				{
					argb[i+1]|=(c2-'a'+10);
				}
				else if(c2>='0'&&c2<='9')
				{
					argb[i+1]|=(c2-'0');
				}
			}
		}
		else if(hex==4)
		{
			for(i=0;i<4;i++)
			{
				char c=text.charAt(start+i);
				if(c>='A'&&c<='Z')
				{
					argb[i]=((c-'A')+10)*16;
				}
				else if(c>='a'&&c<='z')
				{
					argb[i]=(c-'a'+10)*16;
				}
				else if(c>='0'&&c<='9')
				{
					argb[i]=(c-'0')*16;
				}
			}
		}
		else if(hex==8)
		{
			for(i=0;i<4;i++)
			{
				char c=text.charAt(start+i*2);
				char c2=text.charAt(start+i*2+1);
				if(c>='A'&&c<='F')
				{
					argb[i]=(c-'A'+10)<<4;
				}
				else if(c>='a'&&c<='f')
				{
					argb[i]=(c-'a'+10)<<4;
				}
				else if(c>='0'&&c<='9')
				{
					argb[i]=(c-'0')<<4;
				}
				if(c2>='A'&&c2<='F')
				{
					argb[i]|=(c2-'A'+10);
				}
				else if(c2>='a'&&c2<='f')
				{
					argb[i]|=(c2-'a'+10);
				}
				else if(c2>='0'&&c2<='9')
				{
					argb[i]|=(c2-'0');
				}
			}
		}
		color=(argb[0]<<24)|(argb[1]<<16)|(argb[2]<<8)|argb[3];

		return color;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		
		canvas.drawCircle(getWidth()/2,getHeight()/2, getWidth()/2,paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO: Implement this method
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				break;
		}
		return super.onTouchEvent(event);
	}
	
	//打开圆形调色板
	private void setCirColorPacker()
	{
		
		
	  
		
	}
	
	//打开进度条调色板
	private void setPrassColorPacker()
	{
		
	//以对话框的方式调用
	OnClickListener listener =  new OnClickListener()
	{

		@Override
		public void onClick(View p1)
		{
		// TODO: Implement this method
		
		Context context = p1.getContext();
		Toast.makeText(getContext(),"长按可打开圆盘调色板",0).show();
		final ColorPickerSeek view = new ColorPickerSeek(context);
		view.setColor(getColor());
		AlertDialog.Builder builder= new AlertDialog.Builder(context);	
		String title = "设置颜色";

		//edit.setPadding(DisplayUtil.dip2px(context,16),8,DisplayUtil.dip2px(context,16),8);
		//edit.setHint(R.string.edit_save_hint);
		//	builder.setIcon(R.drawable.icon);
		builder.setTitle(title);
		builder.setView(view);

		builder.setPositiveButton(context.getString(R.string.accept), new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
				
				setColor(view.getColor());
				}
			});
		builder.setNegativeButton(context.getString(R.string.refused), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{

				}
			}).show();
		}


	};
	setOnClickListener(listener);
	}
	
	
	
}
