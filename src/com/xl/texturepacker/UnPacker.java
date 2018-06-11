package com.xl.texturepacker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import com.xl.game.math.Str;
import com.xl.game.tool.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UnPacker
{
	 class Texture{
		String name;
		int index;
		int x,y;
		int width,height;
		public Texture(int index ){
			
		}

		public int getWidth()
		{
			// TODO: Implement this method
			return this.width;
		}

		public int getHeight()
		{
			// TODO: Implement this method
			return this.height;
		}
		
		public void setName(String name){
			this.name = name;
		}
		
		public void setXY(int x,int y){
			this.x=x;
			this.y=y;
		}
		
		public void setWH(int w,int h){
			this.width=w;
			this.height=h;
		}
		
		public void setIndex(int index){
			this.index=index;
		}
		
		//获得文件名
		public String getPNGName(){
			if(index<=0)
				return this.name+".png";
			else{
				String temp = Str.sprintf("%s_%03d.png", this.name,this.index);
				return temp;
			}
		}
		
		
		
	}
	
	
	
	public  void unPNG(String pathTxt,String pathPNG,String OUT)  
    { 
		Bitmap bitmap = BitmapFactory.decodeFile(pathPNG);
	
	
	    String text=null;
		String encoding = "UTF-8";  
		try
		{
			FileInputStream read =  
				new FileInputStream(pathTxt);
			
			
				byte[] buf = new byte[read.available()];
			read.read(buf);
			read.close();
			text= new String(buf,encoding);
			
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int type=0;
		int start=0;
		int end=0;
		int line = 1;
		String png_name=null;
		int flag=0;
		String key=null;
		String items[]= new String[4];
		Texture texture=new Texture(-1);
		for(int i=0;i<text.length();i++){
			char c = text.charAt(i);
			//Log.e("unpacker","c="+c+" "+"type="+type);
			switch(type){
				
				case 0:
					if(c=='\n')
						line++;
					if(line==2){
						start=i+1;
						type=1;
					}
					break;
				case 1:
					if(c=='\n')
					{
						line++;
						start=i+1;
						}
					if(line>=7){
						type=2;
						
					}
					break;
				case 2://图片名
				if(c=='\n' || c=='\r'){
					end=i;
					png_name = text.substring(start,i);
					texture.setName(png_name);
				}
				if(c=='\n'){
					line++;
					type=3;
				}
				break;
				case 3: //rotate
				start = i;
				type=4;
				break;
				case 4:
					if(c>='a' && c<='z')
					{
						start=i;
						type=5;
					}
				break;
				case 5:
				if(c==':'){
					 key=text.substring(start,i);
					type=6;
					start=i+1;
				}
				break;
				case 6:
					if(c==':'){
						start = i+1;
						
					}
					if(c==','){
						items[flag]=text.substring(start,i);
						flag++;
						start = i+1;
					}
					if(c=='\n'){
						
						type=3;
						items[flag]=text.substring(start,i);
						flag=0;
						if(key.equals("xy")){
							Log.e("packer","x"+items[0]+" y"+items[1]);
                        texture.setXY(Str.atoi(items[0]) ,Str.atoi(items[1]));
						}
						else if(key.equals("size")){
							Log.e("packer","setWH"+items[0]+" "+items[1]);
texture.setWH(Str.atoi(items[0]), Str.atoi(items[1]));
						}
						else if(key.equals("index")){
texture.setIndex(Str.atoi(items[0]));
						type=1;
						start = i+1;
						Bitmap bitmap_temp = Bitmap.createBitmap(texture.getWidth(),texture.getHeight(),Bitmap.Config.ARGB_8888);
						Canvas canvas = new Canvas(bitmap_temp);
						canvas.drawBitmap(bitmap,-texture.x,-texture.y,null);
						FileOutputStream out=null;
						try
						{
							out = new FileOutputStream(new File(OUT, texture.getPNGName()));
						}
						catch (FileNotFoundException e)
						{
							e.printStackTrace();
						}
						bitmap_temp.compress(Bitmap.CompressFormat.PNG, 100, out);
						}
						else{
							type=3;
						}
						
						
					}
					
			}
		}
		
		  
		
	}

	//解包成单张图片
	public static void toPNG(String pathTxt,String pathPNG,String OUT)  
    {  
        ArrayList<String> name=new ArrayList<String>();  
        ArrayList<String> xy=new ArrayList<String>();  
        ArrayList<String> size=new ArrayList<String>();  
        try {  
            String encoding = "GBK";  
            File file = new File(pathTxt);  
            if (file.isFile() && file.exists()) { // 判断文件是否存在  
                InputStreamReader read = new InputStreamReader(  
					new FileInputStream(file), encoding);// 考虑到编码格式  
                BufferedReader bufferedReader = new BufferedReader(read);  
                String lineTxt = null;  
                int lineNum=0,lineNum2=0;  
                while ((lineTxt = bufferedReader.readLine()) != null) {  
                    lineNum++;  
                    if(lineNum2>0)  
                        lineNum2++;  
                    if(lineNum==5)  
                        lineNum2=1;  
                    if(lineNum%7==5)  
                        name.add(lineTxt);  
                    if(lineNum2%7==3)  
                        xy.add(lineTxt);  
                    if(lineNum2%7==4)  
                        size.add(lineTxt);            
                }  
                read.close();  
            } else {  
                System.out.println("找不到指定的文件");  
            }  
            BufferedImage image = (BufferedImage)ImageIO.read(new File(pathPNG));  
            for(int i=0;i<name.size();i++)  
            {  
                String p1=name.get(i),p2=xy.get(i),p3=size.get(i);  

                int x=0,y=0,w=0,h=0,flag=0;  
                for(int j=0;j<p2.length();j++)  
                {  
                    if(p2.charAt(j)<='9' && p2.charAt(j)>='0' )  
                    {  
                        if(flag==0)  
                        {  
                            x=x*10+p2.charAt(j)-'0';  
                        }  
                        else  
                        {  
                            y=y*10+p2.charAt(j)-'0';  
                        }  
                    }  
                    if(p2.charAt(j)==',')  
                        flag=1;  

                }  
                flag=0;  
                for(int j=0;j<p3.length();j++)  
                {  
                    if(p3.charAt(j)<='9' && p3.charAt(j)>='0' )  
                    {  
                        if(flag==0)  
                            w=w*10+p3.charAt(j)-'0';  
                        else  
                            h=h*10+p3.charAt(j)-'0';  
                    }  
                    if(p3.charAt(j)==',')  
                        flag=1;  

                }  

                File f=new File(OUT);  
                if(!f.exists())  
                    f.mkdirs();  
                ImageIO.write(image.getSubimage(x,y,w,h),"png",new FileOutputStream(OUT+"/"+p1+".png"));  
                System.out.println(p1+":finished");  
            }  

        } catch (Exception e) {  
            System.out.println("读取文件内容出错");  
            e.printStackTrace();  
        }  

    }
	
	
	
	
}
