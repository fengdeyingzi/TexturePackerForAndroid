package com.xl;


import android.app.Application;
import android.view.WindowManager;

import java.util.*;
import java.io.*;
import android.os.*;
import java.lang.Thread.UncaughtExceptionHandler;
//import android.util.*;
import android.content.*;



import android.preference.*;
import com.xl.game.math.*;

import java.text.*;
import android.content.pm.*;

import java.util.Locale;
import java.util.Date;
import android.util.Log;


/**
 * 
 * @author <a href="mailto:kris@krislq.com">Kris.lee</a>
 * @website www.krislq.com
 * @date Nov 29, 2012
 * @version 1.0.0
 * 
 */
public class XLApplication extends Application
{
	private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
	public SharedPreferences sharedPreferences;


	
	
	
 
	
	
		//public static String URL="http://www.pgyer.com/QTBW";
	
//实现悬浮窗到状态栏
	public XLApplication()
	{
		windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
	}
	public WindowManager.LayoutParams getWindowParams()
	{
		return windowParams;
	}




	public static  String DIR = "mnt/sdcard/";
	//Environment.getExternalStorageDirectory()
	//.getAbsolutePath() + "/survey/log/";
	public static String NAME ="log.txt"; //getCurrentDateString() + ".txt";

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
		String temp;
		
		//蒲公英日志
		//PgyCrashManager.register(this);
				/*if(temp.equals("横屏"))
		{
			BaseConfig. orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}
		else if(temp.equals("竖屏"))
		{
			BaseConfig. orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		}
		else
		{
			BaseConfig. orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
		}
		*/
		
		
		
		
		
		
		

		//tvCheckout.setText(settings.getBoolean(Consts.CHECKOUT_KEY, false)+"");
		//tvEditText.setText(settings.getString(Consts.EDIT_KEY, ""));
	}

	/**
	 * 捕获错误信息的handler
	 */
	private UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {

			DIR= "mnt/sdcard/";
			Log.e("App","我崩溃了"+DIR);

			String info = null;
			ByteArrayOutputStream baos = null;
			PrintStream printStream = null;
			try {
				baos = new ByteArrayOutputStream();
				printStream = new PrintStream(baos);
				ex.printStackTrace(printStream);
				byte[] data = baos.toByteArray();
				info = new String(data);
				data = null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (printStream != null) {
						printStream.close();
					}
					if (baos != null) {
						baos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			writeErrorLog(info);
			//info=info.substring(info.lastIndexOf(  "com.xl"));
			Log.e("_ERROR",info);
			/*
			 Intent intent = new Intent(getApplicationContext(),		 printActivity.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 startActivity(intent);
			 */
			android.os.Process.killProcess(android.os.Process.myPid());
			//System.exit(0);
		}
	};

	/**
	 * 向文件中写入错误信息
	 * 
	 * @param info
	 */
	protected void writeErrorLog(String info) {
		File dir = new File(DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, NAME);
		try
		{
			if (!file.isFile())file.createNewFile();
		}
		catch (IOException e)
		{}
		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(file, false);
			fileOutputStream.write(info.getBytes());
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	private static String getCurrentDateString() {
		String result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
																								Locale.getDefault());
		Date nowDate = new Date();
		result = sdf.format(nowDate);
		return result;
	}
	
	//创建文件夹
	public void createDir(String dirname)
	{
		File file=new File(getSDPath()+File.separatorChar+ dirname);
		if(!file.isDirectory())
		{
			file.mkdirs();
		}
	}
	public String getSDPath()
	{
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();//获取sd卡目录
		}
		else 
		{
			return null;
		}
		return sdDir.toString();
	}
}

