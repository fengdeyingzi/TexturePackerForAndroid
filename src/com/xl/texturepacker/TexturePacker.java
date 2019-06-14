package com.xl.texturepacker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import com.xl.game.math.Str;
import com.xl.game.tool.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class TexturePacker
{
	String TAG = "TexturePacker";
	String name;
	String input;
	String output; //保存目录
	int width;
	int height;
	ArrayList<Texture> list_png;
	
	//单张图片素材的属性
	class Texture{
		String filename;
		int index;
		int x,y;
		int width;
		int height;
		boolean isNine;
		public Texture(String name){
			BitmapFactory.Options options = getImageWH(name);
			this.width = options.outWidth;
			this.height = options.outHeight;
			this.x=0;
			this.y=0;
			this.index = -1;
			this.filename = name;
			if(isNine()){
				this.width-=2;
				this.height-=2;
			}
			
		}
		
		public boolean isNine(){
			String temp = this.filename.toLowerCase();
			if(temp.endsWith(".9.png")){
				return true;
			}
			return false;
		}
		
		//获取点9图矩形区域
		public Rect getNineRect(){
			int x=0,y=0,w=0,h=0;
			Bitmap bitmap = BitmapFactory.decodeFile(filename);
			int type=0;
			for(int ix=1;ix<bitmap.getWidth();ix++){
				//Log.e("","颜色："+bitmap.getPixel(ix,0)+"\n");
				if(bitmap.getPixel(ix,0)!=0x000000){
					if(type==0){
					x=ix;
					w=1;
					type=1;
					}
					else if(type==1){
					w++;
					}
				}
			}
			type=0;
			for(int iy=1;iy<bitmap.getHeight();iy++){
				if(bitmap.getPixel(0,iy)!=0x000000){
					if(type==0){
						y=iy;
						h=1;
						type=1;
					}
					else if(type==1){
						h++;
					}
				}
			}
			return new Rect(x-1,y-1,bitmap.getWidth()- (x+w+1),bitmap.getHeight() - (y+h+1));
		}

		public Bitmap getBitmap()
		{
			Bitmap temp = BitmapFactory.decodeFile(filename);
			if(isNine()){
				temp = temp.createBitmap(temp,1,1,temp.getWidth()-2,temp.getHeight()-2);
			}
			//bitmap.setConfig(Bitmap.Config.ARGB_8888);
			Bitmap bitmap = Bitmap.createBitmap(temp.getWidth(),temp.getHeight(),Bitmap.Config.ARGB_8888);
		    Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(temp,0,0,new Paint());
			return bitmap;
		}
		
		public Rect getRect(){
			return new Rect(this.x,this.y,this.x+this.width,this.y+this.height);
		}

		public int getIndex()
		{
			// TODO: Implement this method
			return this.index;
		}
		
		public void setIndex(int index){
			this.index= index;
		}
		
		public String getFileName()
		{
			return this.filename;
		}
		
		public String getName(){
			String name = new File(this.filename).getName();
			int endIndex = name.lastIndexOf(".");
			if(isNine()){
				endIndex = name.lastIndexOf(".9");
			}
			
			if(this.index>=0)
				endIndex = name.lastIndexOf("_");
			if(endIndex>0)
				return name.substring(0,endIndex);
			else
				return name;
		}
		
		public int getWidth(){
			return this.width;
		}
		
		public void setWidth(int w){
			this.width = w;
		}
		
		public int getHeight(){
			return this.height;
		}
		
		public void setHeight(int h){
		    this.height = h;
		}
		
		public int getX(){
			return this.x;
		}
			
		public void setX(int x){
			this.x = x;
		}
		
		public int getY(){
			return this.y;
		}
		
		public void setY(int y){
			this.y = y;
		}
		
		
	}
	
	
	public TexturePacker(Context context,String input,String output,String name)
	{
		this.list_png = new ArrayList<Texture>();
		this.input = input;
		this.output=output;
		this.name=name;
	}
	
	public void setWidth(int width){
		this.width=width;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	
	
	
	//计算图片位置
	public void run(){
		//先按名称排列 设置index
		listPNGforName(list_png);
		int index = -1;
		for(int i=0;i<list_png.size();i++){
			Texture texture = list_png.get(i);
			Texture texture_next = null;
			if(i<list_png.size()-1)
				texture_next = list_png.get(i+1);
			if(isPNGList(texture.getFileName())){
				if(texture_next!=null){
					Log.e(TAG,"序列图片："+texture.getName());
					//匹配成功 那么index++
					if(nextPNGList(texture.getFileName()).equals(texture_next.getFileName())){
						index++;
						texture.setIndex(index+1);
					}
					else if(index>=0){
						index ++;
						texture.setIndex(index+1);
						index = -1;
					}
				}
				else if(index >=0){
					index++;
					texture.setIndex(index+1);
				}
			else{
				index=-1;
				texture.setIndex(index);
			}
				
			}
		}
		
		//排列图片
		//listPNGforHeight(list_png);
		/*
		int ix=0;
		int iy=0;
		int hh=0; //当前高度值
		if(list_png.size()==0) return;
		hh= list_png.get(0).getHeight();
		for(int i=0;i<list_png.size();i++){
			Texture temp = list_png.get(i);
			Texture temp_next=null;
			if(i<list_png.size()-1){
				temp_next = list_png.get(i+1);
			}
			
			temp.setX(ix);
			temp.setY(iy);
			ix+=temp.getWidth();
			if(temp_next!=null)
			if(ix+temp_next.getWidth()>this.width){
				
				ix=0;iy+=hh;
				hh=temp_next.getHeight();
			}
		}
		*/
		listRect();
		
	}
	
   //显示rect排列区域，用于测试 过时
   public Bitmap getRectBitmap(){
	   Bitmap newb = getBitmap();// 创建位图
	   Canvas canvas = new Canvas(newb);// 创建画布
	   canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
	   Paint paint_rect = new Paint();
	   Paint paint_rect2 = new Paint();
	   paint_rect2.setColor(0x200000ff);
	   paint_rect.setStyle(Paint.Style.FILL);
	   paint_rect.setColor(0x20ff0000);
	   paint_rect.setAntiAlias(true);
	   //排列图片
	   listPNGforHeight(list_png);
       Log.e(TAG,"≡————————————————————");
	   ArrayList<Rect> list_rect=new ArrayList<Rect>();
	   int ix=0;
	   int iy=0;
	   int hh=0; //当前高度值
	   int ww=0;
	   int x_1,y_1, w_1,h_1;
	   int x_2,y_2,w_2,h_2;
	   boolean isSet=false;

	   if(list_png.size()==0) return null;
	   hh= list_png.get(0).getHeight();
	   ww = list_png.get(0).getWidth();
	   for(int i=0;i<list_png.size();i++){
		   isSet=false;
		   Texture temp = list_png.get(i);
		   Texture temp_next=null;
		   if(i<list_png.size()-1){
			   temp_next = list_png.get(i+1);
		   }

		   //遍历留白部分，判断是否有可用区域

		   for(int n=0;n<list_rect.size();n++){
			   if(list_rect.get(n).width()> temp.getWidth() && list_rect.get(n).height()>temp.getHeight()){

				   temp.setX(list_rect.get(n).left);
				   temp.setY(list_rect.get(n).top);
				   Rect rect = list_rect.get(n);

				   list_rect.remove(n);

				   x_1=rect.left+temp.getWidth();
				   y_1=rect.top;
				   w_1= rect.width()-temp.getWidth();
				   h_1= temp.getHeight();
				   x_2= rect.left;
				   y_2= rect.top+temp.getHeight();
				   w_2= rect.width();
				   h_2= rect.height()-temp.getHeight();
				  // list_rect.add(new Rect(x_1,y_1,x_1+w_1,y_1+h_1));
				   // Log.e(TAG,"添加rect"+x_1+" "+ y_1+" "+w_1+ " "+ h_1);
				   //list_rect.add(new Rect(x_2,y_2,x_2+w_2,y_2+h_2));
				   Log.e(TAG,"添加rect"+x_2+" "+ y_2+" "+w_2+" "+ h_2);
                   canvas.drawRect(new Rect(x_1,y_1,x_1+w_1,y_1+h_1),paint_rect);
				   canvas.drawRect(new Rect(x_2,y_2,x_2+w_2,y_2+h_2),paint_rect2);
				   isSet=true;
				   Log.e(TAG,"填充留白区域"+temp.getName()+" x:"+temp.getX()+" width:"+rect.width());
				   break;
			   }
			   else{
				   isSet=false;
			   }
		   }

		   if(temp.getY()==iy && !isSet){
			   ww=temp.getWidth();
			   Log.e(TAG,"ww="+ww);
		   }
		   if(ix!=0 && temp.getY()==iy && !isSet){
			   x_1=ix+temp.getWidth();
			   y_1=iy;
			   w_1= ww-temp.getWidth();
			   h_1= temp.getHeight();
			   x_2= ix;
			   y_2= iy+temp.getHeight();
			   w_2= ww;
			   h_2= hh-temp.getHeight();
			   //list_rect.add(new Rect(x_1,y_1,x_1+w_1,y_1+h_1));
			   //Log.e(TAG,"添加rect"+x_1+" "+ y_1+" "+w_1+ " "+ h_1);
			   list_rect.add(new Rect(x_2,y_2,x_2+w_2,y_2+h_2));
			   canvas.drawRect(new Rect(x_2,y_2,x_2+w_2,y_2+h_2),paint_rect2);
               Log.e(TAG,"添加rect"+x_2+" "+ y_2+" "+w_2+" "+ h_2);
		   }
		   if(!isSet){
			   temp.setX(ix);
			   temp.setY(iy);
			   Log.e(TAG,"设置图片"+ix+" "+iy);
			   ix+=temp.getWidth();
			   Log.e(TAG,"ix="+ix );
			   ww = temp.getWidth();
		   }


		   if(temp_next!=null)
			   if(ix+temp_next.getWidth()>this.width){

				   ix=0;iy+=hh;
				   //ww=temp_next.getWidth();
				   hh=temp_next.getHeight();
				   Log.e(TAG,"hh="+hh);
			   }
	   }
	   return newb;
   }
	
   //分析图片位置并排列
   private void listRect(){
	   //排列图片
	   listPNGforHeight(list_png);
       Log.e(TAG,"≡————————————————————");
	   ArrayList<Rect> list_rect=new ArrayList<Rect>();
	   int ix=0;
	   int iy=0;
	   int hh=0; //当前高度值
	   int ww=0;
	   int x_1,y_1, w_1,h_1;
	   int x_2,y_2,w_2,h_2;
	   boolean isSet=false;
	   
	   if(list_png.size()==0) return;
	   hh= list_png.get(0).getHeight();
	   ww = list_png.get(0).getWidth();
	   for(int i=0;i<list_png.size();i++){
		   isSet=false;
		   Texture temp = list_png.get(i);
		   Texture temp_next=null;
		   if(i<list_png.size()-1){
			   temp_next = list_png.get(i+1);
		   }
           
		   //遍历留白部分，判断是否有可用区域
		   
		   for(int n=0;n<list_rect.size();n++){
			   if(list_rect.get(n).width()>= temp.getWidth() && list_rect.get(n).height()>=temp.getHeight()){
				   
				   temp.setX(list_rect.get(n).left);
				   temp.setY(list_rect.get(n).top);
				   Rect rect = list_rect.get(n);
				   
				   list_rect.remove(n);
				   
				   x_1=rect.left+temp.getWidth();
				   y_1=rect.top;
				   w_1= rect.width()-temp.getWidth();
				   h_1= temp.getHeight();
				   x_2= rect.left;
				   y_2= rect.top+temp.getHeight();
				   w_2= rect.width();
				   h_2= rect.height()-temp.getHeight();
				   list_rect.add(0,new Rect(x_1,y_1,x_1+w_1,y_1+h_1));
				  // Log.e(TAG,"添加rect"+x_1+" "+ y_1+" "+w_1+ " "+ h_1);
				   list_rect.add(0,new Rect(x_2,y_2,x_2+w_2,y_2+h_2));
				   Log.e(TAG,"添加rect"+x_2+" "+ y_2+" "+w_2+" "+ h_2);
				   
				   isSet=true;
				   Log.e(TAG,"填充留白区域"+temp.getName()+" x:"+temp.getX()+" width:"+rect.width());
				   break;
			   }
			   else{
				   isSet=false;
			   }
		   }
		   
		   
		   
		   if(ix!=0 && !isSet){
			   x_2= ix;
			   y_2= iy+temp.getHeight();
			   w_2= temp.getWidth();
			   h_2= hh-temp.getHeight();
			   //list_rect.add(new Rect(x_1,y_1,x_1+w_1,y_1+h_1));
			   //Log.e(TAG,"添加rect"+x_1+" "+ y_1+" "+w_1+ " "+ h_1);
			   list_rect.add(new Rect(x_2,y_2,x_2+w_2,y_2+h_2));
               Log.e(TAG,"添加rect"+x_2+" "+ y_2+" "+w_2+" "+ h_2);
		   }
		   if(!isSet){
			   temp.setX(ix);
			   temp.setY(iy);
			   Log.e(TAG,"设置图片"+ix+" "+iy);
			   ix+=temp.getWidth();
			   Log.e(TAG,"ix="+ix );
			   ww = temp.getWidth();
		   }
		   
		   
		   if(temp_next!=null)
			   if(ix+temp_next.getWidth()>this.width){
				   Rect rect = new Rect(ix,iy,this.width,iy+hh);
				   list_rect.add(rect);
				   ix=0;iy+=hh;
				   
				   //ww=temp_next.getWidth();
				   hh=temp_next.getHeight();
				   Log.e(TAG,"hh="+hh);
			   }
	   }
   }
	
	
	//添加图片
	public void addPNG(String filename)
	{
		this.list_png.add(new Texture(filename));
	}
	
	//按名称排列
	private void listPNGforName(ArrayList<Texture> list){
		for(int i=0; i<list.size();i++){
			for(int j=i+1;j<list.size();j++){
				Texture temp = list.get(i);
				Texture temp2 = list.get(j);
				if(compareName(temp.getFileName(),temp2.getFileName())>0){
				list.remove(i);
				list.add(i,temp2);
				list.remove(j);
				list.add(j,temp);
				}
			}
		}
	}
	
	
	//按图片高度排列(重要)
	private void listPNGforHeight(ArrayList<Texture> list){
		for(int i=0; i<list.size();i++){
			for(int j=i+1;j<list.size();j++){
				Texture temp = list.get(i);
				Texture temp2 = list.get(j);
				if(temp.getHeight()<temp2.getHeight()){
					list.remove(i);
					list.add(i,temp2);
					list.remove(j);
					list.add(j,temp);
				}
			}
		}
	}
	
	//随机排列 暂未实现
	
	
	//生成atlas
	public boolean saveAtlas()
	{
		StringBuilder builder = new StringBuilder();
		//第一行 什么也木有 就是这么任性
		builder.append("\n");
		//第二行 图片名字
		builder.append(name).append(".png").append("\n");
		//第三行 图片大小
		builder.append("size: ").append(width).append(",").append(height).append("\n");
		//第四行 位图格式
		builder.append("format: ").append("RGBA8888\n");
		//第五行 不清楚。。。谁帮我解答一下
		builder.append("filter: Nearest,Nearest\n");
		//第六行 我也不知道。。。
		builder.append("repeat: none\n");
		
		
		
		for(int i=0;i<list_png.size();i++){
			Texture texture = list_png.get(i);
			builder.append(texture.getName()).append("\n");
			builder.append("  rotate: false\n");
			builder.append("  xy: "+texture.getX()+", "+texture.getY()+"\n");
			builder.append("  size: "+texture.getWidth()+", "+texture.getHeight()+"\n");
			//点9图
			if(texture.isNine()){
				Rect rect = texture.getNineRect();
				builder.append("  split: "+ rect.left+", "+rect.right+", "+ (rect.top) + ", "+(rect.bottom)+"\n");
				builder.append("  pad: "+ rect.left+", "+rect.top+", "+ (rect.right) + ", "+(rect.bottom)+"\n");
			}
			
			builder.append("  orig: "+texture.getWidth()+", "+texture.getHeight()+"\n");
			builder.append("  offset: 0, 0\n");
			builder.append("  index: "+texture.getIndex()+"\n");
			
		}
		
		if(name==null || name.length()==0)return false;
		try {
			FileOutputStream outStream = new FileOutputStream(
				new File(this.output,this.name+".atlas"), false);
			OutputStreamWriter writer = new OutputStreamWriter(outStream,
															   "utf-8");
			writer.write(builder.toString());
			writer.flush();
			writer.close();// 记得关闭
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	//生成图片
	public Bitmap getBitmap()
	{
		Bitmap newb = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);// 创建位图
		Canvas canvas = new Canvas(newb);// 创建画布
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		
		for(int i=0;i<list_png.size();i++){
			Texture texture = list_png.get(i);
			canvas.drawBitmap(texture.getBitmap(),texture.getX(),texture.getY(),null);
		}
		
		
		return newb;
	}
	//生成图片并显示边距
	public Bitmap getBitmapAndRect()
	{
		Bitmap newb = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);// 创建位图
		Canvas canvas = new Canvas(newb);// 创建画布
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Paint paint_rect = new Paint();
		paint_rect.setStyle(Paint.Style.STROKE);
		paint_rect.setColor(0xffff0000);
		paint_rect.setAntiAlias(true);
		paint_rect.setStrokeWidth(1);
		for(int i=0;i<list_png.size();i++){
			Texture texture = list_png.get(i);
			canvas.drawBitmap(texture.getBitmap(),texture.getX(),texture.getY(),null);
			canvas.drawRect(texture.getX(),texture.getY(),texture.getX()+texture.getWidth(), texture.getY()+texture.getHeight(),paint_rect);
		}


		return newb;
	}
	
	//获取图片宽高
	private BitmapFactory.Options getImageWH(String filename)
    {
	BitmapFactory.Options options = new BitmapFactory.Options();    

	/**  
	 * 最关键在此，把options.inJustDecodeBounds = true;  
	 * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了  
	 */    
	options.inJustDecodeBounds = true;    
	Bitmap bitmap = BitmapFactory.decodeFile(filename, options); // 此时返回的bitmap为null    
	/**  
	 *options.outHeight为原始图片的高  
	 */    
	
	
	//Log.e("Test", "Bitmap Height == " + options.outHeight);  
	return options;
	}
	
	
	//按名字比较 用于排列图片
	public int compareName(String str1,String str2){
		try {
			byte[] b1 = str1.getBytes("GBK");
			byte[] b2 = str2.getBytes("GBK");
			int l1=b1.length;
			int l2=b2.length;
			int l=Math.min(l1, l2);
			int k=0;
			while(k<l){
				int bt1=b1[k]&0xff;
				int bt2=b2[k]&0xff;
				if(bt1!=bt2)
					return bt1-bt2;
				k++;
			}
			return l1-l2;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//判断图片是否为点9图
	private boolean isNine(String name){
		String temp = name.toLowerCase();
		//识别点九图
		boolean isNine=false;
		if(temp.endsWith(".9.png")){
			isNine = true;
		}
		return isNine;
	}
	
	//检测文件名是否符合序列图片规则
	private boolean isPNGList(String name)
	{
		int type=0;
		String temp = name.toLowerCase();
		//识别点九图
		boolean isNine=false;
		if(temp.endsWith(".9.png")){
			isNine = true;
		}
		int endlength = (isNine)? name.length()-4:name.length();
		PNGWHILE:
		for(int i=endlength-1;i>0;i--)
		{
			char c=name.charAt(i);
		    switch(type){
				case 0: //.
					if(c=='.')
						type=1;
					break;
				case 1: // 0-9
					if(c>='0' && c<='9')
						type=2;
					else
						return false;
					break;
				case 2:  //_
				    if(c>='0' && c<='9')
					{
						
					}
				    else if(c=='_')
						return true;
					else
						return false;
					break;
			}
		}
		return false;
	}
	
	//获取图片序列的下一张图片名字
	private String nextPNGList(String name)
	{
		int type=0;
		int num=0;   //数字
		int num_size=0; //数字的长度
		int start=0;
		String start_text=null;
		String end_text=null;
		PNGWHILE:
		for(int i=name.length()-1;i>0;i--)
		{
			char c=name.charAt(i);
		    switch(type){
				case 0: //.
					if(c=='.'){
						start = i;
						end_text = name.substring(i);
						type=1;
					}
						
					break;
				case 1: // 0-9
					if(c>='0' && c<='9'){
						type=2;
						num_size++;
					}
						
					break;
				case 2:  //_
				    if(c>='0' && c<='9')
					{
                     num_size++;
					}
				    else if(c=='_'){
						num = Str.atoi(name.substring(i+1,start));
						start_text = name.substring(0,i+1);
						break PNGWHILE;
					}
						
					else
						return null;
					break;
			}
		}
		
		
		//生成图片名字
		String pngnext_name = Str.sprintf(start_text+ "%0"+num_size+"d"+end_text, num+1);
		Log.e(TAG,"生成序列图片名字："+pngnext_name);
		
		return pngnext_name;
	}
	
	
	  
	
	
}
