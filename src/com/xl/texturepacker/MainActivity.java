package com.xl.texturepacker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.github.chrisbanes.photoview.PhotoView;
import com.xl.game.math.Str;
import com.xl.game.tool.DisplayUtil;
import com.xl.game.tool.SharedPreferencesUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import com.xl.game.tool.ViewTool;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener
{

	public static final int
	DLG_HELP=100,
	DLG_ABOUT=101;
	public static final int
	DLG_UNPACKER=102;
	
	
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch(p1.getId()){
			case R.id.btn_look:
				look();
				break;
			case R.id.btn_save:
				save();
				break;
			
		}
		
	}
	
	
	
	//字体路径 文字大小 位移 颜色 文件名
	TextView text_fileName;
	TextView text_input,text_output;
	TextView text_width, text_height;
    SharedPreferencesUtil preference;
	
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		text_input = (TextView) findViewById(R.id.edit_input);
		text_output = (TextView) findViewById(R.id.edit_output);
		text_width = (TextView) findViewById(R.id.edit_width);
		text_height = (TextView) findViewById(R.id.edit_height);
		text_fileName = (TextView) findViewById(R.id.edit_filename);
		
		
        setOnClickListenerAllButtons(this);
		preference = new SharedPreferencesUtil(this);
		
		
    }

	@Override
	protected void onStart()
	{
		// TODO: Implement this method
		text_input.setText(preference.getString("input",null));
		text_output.setText(preference.getString("output",null));
		text_fileName.setText(preference.getString("name","packer"));
		text_width.setText(preference.getString("width","1024"));
		text_height.setText(preference.getString("height","1024"));
		
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		preference.setString("input",text_input.getText().toString());
		preference.setString("output",text_output.getText().toString());
		preference.setString("width", text_width.getText().toString());
		preference.setString("height",text_height.getText().toString());
		preference.setString("name",text_fileName.getText().toString());
		preference.commit();
		
		super.onStop();
	}
	
	
	
	
	
	//为根布局下所有按钮设置监听
	public void setOnClickListenerAllButtons(View.OnClickListener listener){
		//获取根布局
		ViewGroup group = (ViewGroup)((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
		setOnClickListenerAllButtons(group,listener);
	}

	//为所有按钮设置监听
	private void setOnClickListenerAllButtons(View view,View.OnClickListener listener) {

		List<View> allchildren = new ArrayList<View>();

		if(view instanceof Button)
		{
			if(view.getId()!= -1)
				view.setOnClickListener(listener);
		}
		else if(view instanceof ImageButton)
		{
			if(view.getId()!=-1)
				view.setOnClickListener(listener);
		}

		else if (view instanceof ViewGroup) {

			ViewGroup vp = (ViewGroup) view;

			for (int i = 0; i < vp.getChildCount(); i++) {

				View viewchild = vp.getChildAt(i);

				setOnClickListenerAllButtons(viewchild,listener);

			}

		}



	}
	
	
	public static String getTextFromAssets(Context context, String assetspath,String coding)
	{
		String r0_String;
		String r1_String = "";
		AssetManager assets = context.getResources().getAssets();
		try {
			InputStream input = assets.open(assetspath);
			byte[] buffer = new byte[input.available()];
			input.read(buffer);
			r0_String = new String(buffer, coding);
			input.close();
			return r0_String;
		} catch (IOException r0_IOException) {
			r0_String = r1_String;
		}


		return r0_String;

	}


	private void look()
	{
		String input = text_input.getText().toString();
		String output = text_output.getText().toString();
		String name = text_fileName.getText().toString();
		int width = Str.atoi(text_width.getText().toString());
		int height = Str.atoi(text_height.getText().toString());
		
		TexturePacker packer = new TexturePacker(this,input,output,name);
		packer.setWidth(width);
		packer.setHeight(height);
		File file_input = new File(input);
		if(file_input.isDirectory()){
			File[] file_png = file_input.listFiles();
			for(int i=0;i<file_png.length;i++){
				if(file_png[i].getName().endsWith(".png") || file_png[i].getName().endsWith(".PNG")){
					packer.addPNG(file_png[i].getPath());
				}
			}
		}
		else{
			Toast.makeText(this,R.string.input_error,1).show();
			return;
		}
		packer.run();
	    
		PhotoView photoView = new PhotoView(this);
		photoView.setMinimumHeight(DisplayUtil.dip2px(this,320));
		photoView.setMinimumWidth(DisplayUtil.dip2px(this,320));
		photoView.setImageDrawable(new BitmapDrawable(packer.getBitmapAndRect()));
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("返回", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
					
				}
				
			
		}
		);
		builder.setView(photoView);
		
		builder.create().show();
		
		
		
	}
	
	
	private void save()
	{
		String input = text_input.getText().toString();
		String output = text_output.getText().toString();
		String name = text_fileName.getText().toString();
		int width = Str.atoi(text_width.getText().toString());
		int height = Str.atoi(text_height.getText().toString());

		TexturePacker packer = new TexturePacker(this,input,output,name);
		packer.setWidth(width);
		packer.setHeight(height);
		File file_input = new File(input);
		if(file_input.isDirectory()){
			File[] file_png = file_input.listFiles();
			for(int i=0;i<file_png.length;i++){
				if(file_png[i].getName().endsWith(".png") || file_png[i].getName().endsWith(".PNG")){
					packer.addPNG(file_png[i].getPath());
				}
			}
		}
		else{
			Toast.makeText(this,R.string.input_error,1).show();
			return;
		}
		packer.run();
	    //保存图片
		Bitmap bitmap = packer.getBitmap();
		FileOutputStream out=null;
		try
		{
			 out = new FileOutputStream(new File(output, name + ".png"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		//保存
		packer.saveAtlas();
Toast.makeText(this,"生成成功",1).show();
		


	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		// TODO: Implement this method
		if(id == DLG_ABOUT)
		{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.app_about);
			builder.setPositiveButton("返回", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method

					}


				}
			);
		return builder.create();
		}
		
		if(id == DLG_HELP)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.app_help);
			builder.setPositiveButton("返回", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method

					}


				}
			);
			return builder.create();
		}
		
		if(id == DLG_UNPACKER)
		{
			View view = ViewTool.getView(this,R.layout.dlg_unpack);
			final EditText edit_png = (EditText) view.findViewById(R.id.edit_pngpath);
			final EditText edit_undir = (EditText) view.findViewById(R.id.edit_undir);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setView(view);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
          UnPacker unpacker = new UnPacker();
		  String path_png = edit_png.getText().toString();
		  String path_atlas = null;
		  String path_output = edit_undir.getText().toString();
		  int index = path_png.lastIndexOf('.');
		  if(index>0){
			  path_atlas = path_png.substring(0,index)+".atlas";
		  }
		  unpacker.unPNG( path_atlas,path_png, path_output);
		  Toast.makeText(MainActivity.this,"解包完成",0).show();
					}


				}
			);
			builder.setNegativeButton("返回", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method

					}


				}
			);
			return builder.create();
		}
		
		
		return super.onCreateDialog(id);
	}
	
	//菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		int i=0;
		//组别 id 顺序 文本
		menu.add(0,3,1,"解包atlas");
		menu.add(0,0,1,"帮助");
		menu.add(0,1,1,"关于");
        menu.add(0,2,1,"检查更新");

		return true; //super.onCreateOptionsMenu(menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case 0://
showDialog(DLG_HELP);
				break;
			case 1://
				showDialog(DLG_ABOUT);
				break;
			case 2://
			N2J_wap("http://www.yzjlb.net/app/libgdx/texturepacker/");
				break;
			case 3:
				showDialog(DLG_UNPACKER);
		}



		return super.onOptionsItemSelected(item);
	}
	
	//调用浏览器打开
	void N2J_wap(String http)
	{
		/*
		 Uri uri=Uri.parse(http);
		 Intent intent=new Intent( Intent.ACTION_VIEW ,uri );
		 run_activity.startActivity(intent);
		 */
		Intent intent= new Intent();

		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(http);
		intent.setData(content_url);
		try
		{
			this.startActivity(intent);
		}
		catch(Exception e)
		{
			Toast.makeText(this,"请下载网页浏览器",0).show();
		}
	}
	
	
	
}
