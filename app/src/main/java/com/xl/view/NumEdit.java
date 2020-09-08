package com.xl.view;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.xl.game.math.Str;
import com.xl.game.tool.DisplayUtil;
/*
数字编辑框
继承linearLayout

*/
public class NumEdit extends LinearLayout implements OnClickListener
{

	boolean isPositive;
	@Override
	public void onClick(View p1)
	{
		switch(p1.getId())
		{
			case 1:
				setSize(getSize()-1);
				break;
			case 2:
				setSize(getSize()+1);
				break;
		}
	}
	
	EditText edit;
	Button btn_left,btn_right;
	
	
	public NumEdit(Context context)
	{
		super(context);
		init(context);
	}
	
	public NumEdit(android.content.Context context, android.util.AttributeSet attrs)
	
	{
		super(context,attrs);
		init(context);
	}
	
	void init(Context context)
	{
		isPositive=false;
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,10f);
		//setLayoutParams(params);
		LayoutParams params_btn=new LayoutParams(dip2px(context,40),LayoutParams.WRAP_CONTENT);
		//params_btn.setMargins(0,0,0,0);
		
		edit=new EditText(context);
		edit.setGravity(Gravity.CENTER);
		edit.setInputType( EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
		btn_left=new Button(context);
		btn_left.setText("<");
		btn_left.setMinWidth(dip2px(context,30));
		btn_left.setId(1);
		btn_left.setMaxWidth(DisplayUtil.dip2px(context,15));
		btn_right=new Button(context);
		btn_right.setText(">");
		btn_right.setId(2);
		btn_right.setMaxWidth(DisplayUtil.dip2px(context,15));
		
		edit.setLayoutParams(params);
		btn_left.setLayoutParams(params_btn);
		btn_right.setLayoutParams(params_btn);
		addView(btn_left);
		addView(edit);
		addView(btn_right);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		edit.setText("0");
	}
	
	//设置不可为负数
	public void setpositive()
	{
		isPositive=true;
	}
	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 *
	 * @param dipValue
	 * @param scale
	 * （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		int px= (int) (dipValue * scale + 0.5f);
		//Log.e("dip2px",""+dipValue+" "+px);
		return px;
	}
	public int getSize()
	{
		return Str.atoi(edit.getText().toString());
	}
	
	public void setSize(int size)
	{
		if(isPositive)
		if(size<=0)size=1;
		edit.setText(String.valueOf(size));
	}
}

