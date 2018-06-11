
AIDE自带一个游戏开发引擎libgdx(如图)，用libgdx引擎就能在手机上开发灰常流畅的游戏，但是手机端开发游戏有各种不方便，比如不能制作游戏素材。。。libgdx引擎通过加载fnt文件来显示文字，它自带的fnt字体生成工具是电脑端的，影子今天给大家带来libgdx手机端字库生成工具！！

下载链接：http://www.yzjlb.net/app/libgdx/texturepacker/

本软件类似于libgdx引擎自带的TexturePacker工具，可以将图片打包为atlas文件和png图片
实现了png序列列表功能，支持打包序列图片。
inputDir ：要打包的图片的目录
outputDir: 打包完成后，图片的输出目录
packFileName:输出的文件名


更新说明

1.1
  -实现序列图片功能
  
1.2
  -优化图片排序算法
  -增加解包功能


使用方法：
stage=new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);   
TextureAtlas atlas=new TextureAtlas(Gdx.files.internal("packFileName.atlas"));//根据packFileName.atlas文件获取所有图片   
Image image1=new Image(atlas.findRegion("btn")); //获取名为btn的图片，并创建一个Image对象   
image1.scaleX=image1.scaleY=0.2f;   
image1.x=image1.y=0;   
Image image2=new Image(atlas.findRegion("star"));   
image2.x=image2.y=40;   
image2.scaleX=image2.scaleY=0.5f;   
stage.addActor(image1);   
stage.addActor(image2);   
[java] view plaincopy
stage=new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);   
TextureAtlas atlas=new TextureAtlas(Gdx.files.internal("packFileName.atlas"));//根据packFileName.atlas文件获取所有图片   
Image image1=new Image(atlas.findRegion("btn")); //获取名为btn的图片，并创建一个Image对象   
image1.scaleX=image1.scaleY=0.2f;   
image1.x=image1.y=0;   
Image image2=new Image(atlas.findRegion("star"));   
image2.x=image2.y=40;   
image2.scaleX=image2.scaleY=0.5f;   
stage.addActor(image1);   
stage.addActor(image2);  






