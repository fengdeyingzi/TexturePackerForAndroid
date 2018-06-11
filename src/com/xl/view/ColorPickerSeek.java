package com.xl.view;


import android.widget.*;
import android.content.Context;
import android.view.*;
import com.xl.game.tool.*;
import java.util.*;
/*
 进度条版颜色选择器
 获取颜色 getColor
 设置颜色 setColor

 //以对话框的方式调用
 OnClickListener listener =  new OnClickListener()
 {

 @Override
 public void onClick(View p1)
 {
 // TODO: Implement this method
 Context context = p1.getContext();
 final ColorPickerSeek view = new ColorPickerSeek(context);
 view.setColor(getInt(className));
 AlertDialog.Builder builder= new AlertDialog.Builder(context);	
 String title = "设置颜色";

 //edit.setPadding(DisplayUtil.dip2px(context,16),8,DisplayUtil.dip2px(context,16),8);
 //edit.setHint(R.string.edit_save_hint);
 //	builder.setIcon(R.drawable.icon);
 builder.setTitle(title);
 builder.setView(view,DisplayUtil.dip2px(context,8),8,DisplayUtil.dip2px(context,8),8);

 builder.setPositiveButton(context.getString(R.string.accept), new DialogInterface.OnClickListener() 
 {
 public void onClick(DialogInterface dialog, int whichButton) 
 {
 setNum(className, view.getColor());
 colorView.setColor(view.getColor());
 }
 });
 builder.setNegativeButton(context.getString(R.string.refused), new DialogInterface.OnClickListener() {
 public void onClick(DialogInterface dialog, int whichButton)
 {

 }
 }).show();
 }


 };
 */

public class ColorPickerSeek extends LinearLayout implements android.widget.SeekBar.OnSeekBarChangeListener
{


	@Override
	public void onProgressChanged(SeekBar seekbar, int pos, boolean p3)
	{
	int a = seek_a.getProgress();
	int r = seek_r.getProgress();
	int g = seek_g.getProgress();
	int b = seek_b.getProgress();
	if(seekbar.equals(seek_a))
	{
	a = pos;
	}
	else if(seekbar.equals(seek_b))
	{
	b=pos;
	}
	else if(seekbar.equals(seek_r))
	{
	r=pos;
	}
	else if(seekbar.equals(g))
	{
	g=pos;
	}
	int c = (a<<24) | (r<<16) | (g<<8) | b;
	colorView.setBackgroundColor(c);
	colorView.setText(sprintf("#%08x",c));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekbar)
	{
	// TODO: Implement this method
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekbar)
	{
	int a = seek_a.getProgress();
	int r = seek_r.getProgress();
	int g = seek_g.getProgress();
	int b = seek_b.getProgress();
	int c = (a<<24) | (r<<16) | (g<<8) | b;
	colorView.setBackgroundColor(c);

	}
	boolean alphaDraw;
	SeekBar seek_a,seek_r,seek_g,seek_b;
	LinearLayout layout_a,layout_r,layout_g,layout_b;
	TextView text_a,text_r,text_g,text_b;
	TextView colorView;

	public ColorPickerSeek(Context context)
	{
	super(context);
	initView();
	}

	public void initView()
	{
	Context context = getContext();
	int padding = DisplayUtil.dip2px(getContext(),8);
	setPadding(padding,padding,padding,padding);
	LinearLayout.LayoutParams layoutParams=null;
	LinearLayout mainlayout = new LinearLayout(context);
	mainlayout.setOrientation(LinearLayout.VERTICAL);
	colorView = new TextView(context);
	colorView.setTextSize(18);
	colorView.setTextColor(0xffffffff);
	colorView.setGravity(Gravity.CENTER);

	//a
	seek_a = new SeekBar(context);
	seek_a.setMax(255);
	layout_a= new LinearLayout(context);
	layout_a.setOrientation(LinearLayout.VERTICAL);
	text_a = new TextView(context);
	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	layout_a.addView(text_a, layoutParams);

	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	layout_a.addView(seek_a,layoutParams);

	//r
	seek_r = new SeekBar(context);
	seek_r.setMax(255);
	layout_r= new LinearLayout(context);
	layout_r.setOrientation(LinearLayout.VERTICAL);
	text_r = new TextView(context);
	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	layout_r.addView(text_r, layoutParams);

	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	layout_r.addView(seek_r,layoutParams);

	//g
	seek_g = new SeekBar(context);
	seek_g.setMax(255);
	layout_g= new LinearLayout(context);
	layout_g.setOrientation(LinearLayout.VERTICAL);
	text_g = new TextView(context);
	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	layout_g.addView(text_g, layoutParams);

	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	layout_g.addView(seek_g,layoutParams);

	//b
	seek_b = new SeekBar(context);
	seek_b.setMax(255);
	layout_b= new LinearLayout(context);
	layout_b.setOrientation(LinearLayout.VERTICAL);
	text_b = new TextView(context);
	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	layout_b.addView(text_b, layoutParams);

	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	layout_b.addView(seek_b,layoutParams);

	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, DisplayUtil.dip2px(context,30));
	mainlayout.addView(colorView,layoutParams);
	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	mainlayout.addView(layout_a,layoutParams);
	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	mainlayout.addView(layout_r,layoutParams);
	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	mainlayout.addView(layout_g,layoutParams);
	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	mainlayout.addView(layout_b,layoutParams);
	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

	layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	this.setOrientation(LinearLayout.VERTICAL);
	this.addView(mainlayout,layoutParams);
	text_a.setText("a");
	text_r.setText("r");
	text_g.setText("g");
	text_b.setText("b");

	seek_a.setOnSeekBarChangeListener(this);
	seek_r.setOnSeekBarChangeListener(this);
	seek_g.setOnSeekBarChangeListener(this);
	seek_b.setOnSeekBarChangeListener(this);
	}


	//是否显示透明度滑动条
	public void setAlphaDraw(boolean isDraw)
	{
	this.alphaDraw = isDraw;

	if(alphaDraw)
	{
	this.layout_a.setVisibility(View.VISIBLE);
	}
	else
	{
	this.seek_a.setProgress(0xff);
	this.layout_a.setVisibility(View.GONE);
	}

	}

	//获取颜色
	public int getColor()
	{
	int a = seek_a.getProgress();
	int r = seek_r.getProgress();
	int g = seek_g.getProgress();
	int b = seek_b.getProgress();
	return (a<<24) | (r<<16) | (g<<8) | b;
	}

	//设置颜色
	public void setColor(int color)
	{
	int a = color>>24&0xff;
	int r = color>>16&0xff;
	int g = color>>8&0xff;
	int b = color&0xff;
	this.seek_a.setProgress(a);
	this.seek_r.setProgress(r);
	this.seek_g.setProgress(g);
	this.seek_b.setProgress(b);
	int c = (a<<24) | (r<<16) | (g<<8) | b;
	this.colorView.setBackgroundColor(c);
	this.colorView.setText(sprintf("#%08x",c));
	}

	public String sprintf(String text,Object ... obj)
	{
	Formatter r0_Formatter = new Formatter();
	r0_Formatter.format(text, obj);
	return r0_Formatter.toString();
	}

}

