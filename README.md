# VPLine
A highly customizable Java library to draw line graphs. I had first built a graph from scratch using pure Java for my stock market prediction Android app (https://github.com/vsp412/Stocks-Prediction-App). Then I spun my code off into this library to help others build a better line graph.

Instructions to use:
In your project's build.gradle file, put this:

`allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}`
	
In your app's build.gradle file, put this: 

`dependencies {
	        implementation 'com.github.vsp412:VPLine:1.0'
	}`
	
Sync both files
Next, in your activity's layout file, create the `com.vinx.vpline.vpLine` widget, assign it an ID, layout height and width, and any other parameters you may choose. Example:
 
 `<com.vinx.vpline.vpLine
                android:id="@+id/lineview"
                android:layout_width="wrap_content"
                android:layout_height="500dp"
                android:layout_gravity="center_horizontal"
                android:foregroundGravity="center_horizontal"
                android:paddingBottom="10sp" />`
                
Now, in your activity's java file, instantiate and initialize the widget as follows:

 `vpLine vp  = (vpLine) findViewById(R.id.lineview);`

Then pass all the graph properties you want to the static methods as shown below. I will update this readme file shortly and explain what each passed parameter means. Meanwhile, you can toy around with the parameter values:

        `vpLine.setGraphAttrib(h, w,0.3f,0.05f,0.85f,0.6f, Color.BLACK,10,true);
        vpLine.setXY(xval,yval,2, Color.BLUE,10, 0.8f, 0.9f);
        vpLine.setPointLooks(true, Color.GREEN,25.0f,7);
        vpLine.setLabels(true,true,xval,yval,100,300,50,50,3,3, true, "","",Color.BLACK,Color.RED,10);`

Then finally, call the non-static 'draw' method as shown:
  
     `vp.draw();`
     
Contact me on vsp4991@gmail.com for any questions/issues.     



     
     


