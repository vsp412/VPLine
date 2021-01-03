package com.vinx.vpline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;




public class vpLine extends View {

    private Paint graphPaint=new Paint();
    private  Paint lgpaint=new Paint();
    private  Paint ppaint=new Paint();
    private  Paint xlabelpaint=new Paint();
    private  Paint ylabelpaint=new Paint();
    private  Paint notchpaint=new Paint();

    //Screen attributes
    private static float sw,sh;

    //Graph attributes
    private static PointF p1,p2,pO;
    private static float p1x_,p1y_,p2x_,p2y_;
    private static int gcol;
    private static int lineWidth;
    private static boolean arrows;


    //X and Y axis arrays and lines
    private static float[] inpx,inpy, ix, iy;
    private static PointF[] pts;
    private static int lcol;
    private static int lineWidthPlotLines;
    private static int lineType;
    private static float occX,occY;

    //Point attributes
    private static int pcol;
    private static float pradius;
    private static boolean FilledBool;
    private static int circBordThickness;

    //Labelling attributes
    private  static float base_Y,base_X,lab_dist_x,lab_dist_y,x_lab_size,y_lab_size;
    private static boolean xshow,yshow,notches;
    private static String xtit, ytit;
    private static int x_ratio,y_ratio;
    private static int labcol,labwidth,titcolor;


    public vpLine(Context context) {
        super(context);
    }

    public vpLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public vpLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("DrawAllocation")
    protected  void onDraw(Canvas canvas){


        graphPaint.setColor(gcol);
        graphPaint.setStrokeWidth(lineWidth);

        lgpaint.setColor(lcol);
        lgpaint.setStrokeWidth(lineWidthPlotLines);
        switch(lineType){
            case 1: lgpaint.setStyle(Paint.Style.FILL);
                break;
            case 2: lgpaint.setStyle(Paint.Style.STROKE);
                lgpaint.setPathEffect(new DashPathEffect(new float[] {10,10}, 0));
                break;

        }

        ppaint.setColor(pcol);
        ppaint.setStrokeWidth(circBordThickness);
        if(!FilledBool){
            ppaint.setStyle(Paint.Style.STROKE);
        }
        else{
            ppaint.setStyle(Paint.Style.FILL);
        }


        xlabelpaint.setTextSize(x_lab_size);
        xlabelpaint.setColor(labcol);
        xlabelpaint.setStrokeWidth(labwidth);

        ylabelpaint.setTextSize(y_lab_size);
        ylabelpaint.setColor(labcol);
        ylabelpaint.setStrokeWidth(labwidth);

        notchpaint.setColor(Color.BLACK);
        notchpaint.setStrokeWidth(15);

        //Draw graph
        canvas.drawLine(p1.x, p1.y,pO.x, pO.y,graphPaint);
        canvas.drawLine(pO.x, pO.y,p2.x, p2.y,graphPaint);
        if(arrows) {
            canvas.drawLine(p1.x, p1.y, p1.x - 0.03f * Math.min(sw, sh), p1.y + 0.03f * Math.min(sw, sh), graphPaint);
            canvas.drawLine(p1.x, p1.y, p1.x + 0.03f * Math.min(sw, sh), p1.y + 0.03f * Math.min(sw, sh), graphPaint);

            canvas.drawLine(p2.x, p2.y, p2.x - 0.03f * Math.min(sw, sh), p2.y - 0.03f * Math.min(sw, sh), graphPaint);
            canvas.drawLine(p2.x, p2.y, p2.x - 0.03f * Math.min(sw, sh), p2.y + 0.03f * Math.min(sw, sh), graphPaint);
        }

        //Draw lines
        if(pts.length>1) {
            Collections.sort(Arrays.asList(pts),new Comparator<PointF>() {

                public int compare(PointF o1, PointF o2) {
                    return Float.compare(o1.x, o2.x);
                }
            });

            for (int i = 0; i < pts.length - 1; i++) {
                canvas.drawLine(pts[i].x, pts[i].y, pts[i + 1].x, pts[i + 1].y, lgpaint);
            }
        }

        //Draw labels

        if(xshow) {
            Collections.sort(Arrays.asList(pts),new Comparator<PointF>() {

                public int compare(PointF o1, PointF o2) {
                    return Float.compare(o1.x, o2.x);
                }
            });

            Arrays.sort(inpx);
            float minmaxdiff=Math.abs(inpx[0]-inpx[inpx.length-1]);
            float ptsmmdiff=Math.abs(pts[0].x-pts[pts.length-1].x);

            float jx=inpx[0];
            float px=pts[0].x;
            if(x_ratio<2){
                Toast.makeText(getContext(), "The x_axis_labels_spread value must be greater than or equal to 2. Please refer the documentation.", Toast.LENGTH_SHORT).show();
            }

            for (int i = 0; i < x_ratio; i++){
                canvas.drawText(jx + "", px, base_Y + lab_dist_x, xlabelpaint);
                Rect rect = new Rect();
                xlabelpaint.getTextBounds(jx+"", 0, (jx+"").length(), rect);
                //canvas.translate(,0);
                //paint.setStyle(Paint.Style.FILL);
                if(notches)
                    canvas.drawLine(px, base_Y - 0.05f * ptsmmdiff, px, base_Y + 0.05f * ptsmmdiff, notchpaint);
                jx=jx+minmaxdiff/(x_ratio-1);
                px=px+ptsmmdiff/(x_ratio-1);
            }

        }
        if(yshow){
            Collections.sort(Arrays.asList(pts),new Comparator<PointF>() {

                public int compare(PointF o1, PointF o2) {
                    return Float.compare(o1.y, o2.y);
                }
            });

            Arrays.sort(inpy);
            float minmaxdiff=Math.abs(inpy[0]-inpy[inpy.length-1]);
            float ptsmmdiff=Math.abs(pts[0].y-pts[pts.length-1].y);

            float jy=inpy[inpy.length-1];
            float py=pts[0].y;
            if(y_ratio<2){
                Toast.makeText(getContext(), "The y_axis_labels_spread value must be greater than or equal to 2. Please refer the documentation.", Toast.LENGTH_SHORT).show();
            }

            for (int i = 0; i < y_ratio; i++){
                canvas.drawText(jy + "", base_X - lab_dist_y, py, ylabelpaint);
                if(notches)
                    canvas.drawLine(base_X - 0.05f * ptsmmdiff, py, base_X + 0.05f * ptsmmdiff, py, notchpaint);
                jy=jy-minmaxdiff/(y_ratio-1);
                py=py+ptsmmdiff/(y_ratio-1);
            }
        }

        //Draw points
        for (PointF pt : pts) {
            canvas.drawCircle(pt.x, pt.y, pradius, ppaint);

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int pX = (int) event.getX();
            int pY = (int) event.getY();

            Log.d("pwersa", pX + "");
            Log.d("pwersa", pY + "");
            invalidate();
        }
        return true;
    }


    public static void setGraphAttrib(int screenHeight, int screenWidth){
        p1=new PointF(0.2f*screenWidth,0.15f*screenHeight);
        p2=new PointF(0.85f*screenWidth,0.8f*screenHeight);
        pO=new PointF(0.2f*screenWidth,0.8f*screenHeight);
        arrows=true;
        sh=screenHeight;
        sw=screenWidth;

        p1x_=0.2f;
        p2x_=0.85f;
        p1y_=0.15f;
        p2y_=0.8f;


        Log.d("grahxg",Integer.toString(screenHeight)+"******"+Integer.toString(screenWidth));
        gcol = Color.BLACK;
        lineWidth=10;
    }

    public static void setGraphAttrib(int screenHeight, int screenWidth, float p1x, float p1y, float p2x, float p2y, int color, int lineThickness, boolean axis_arrows){

        p1=new PointF(p1x*screenWidth,p1y*screenHeight);
        p2=new PointF(p2x*screenWidth,p2y*screenHeight);
        pO=new PointF(p1x*screenWidth,p2y*screenHeight);
        arrows = axis_arrows;

        sh=screenHeight;
        sw=screenWidth;

        p1x_=p1x;
        p2x_=p2x;
        p1y_=p1y;
        p2y_=p2y;

        Log.d("grahxg",Integer.toString(screenHeight)+"******"+Integer.toString(screenWidth));
        gcol = color;
        lineWidth=lineThickness;

    }

    public static void setXY(float[] xval,float[] yval) {

        ix=xval;
        iy=yval;
        pts = new PointF[xval.length];
        float inptx = Math.min(p1x_, p2x_) * sw;
        base_X=inptx;
        float baseY = ((Math.max(p1y_, p2y_)) * sh);
        base_Y=baseY;
        Log.d("lmny", "" + inptx);
        occX=0.9f;
        occY=0.9f;
        for (int i = 0; i < xval.length; i++) {

            float proportionOfYwrtOnlyGraphHeight = (yval[i] - minValue(yval)) / diffValue(yval);
            float graphHeight = ((Math.abs(p2y_ - p1y_)) * sh) * occY ;
            float YwrtOnlyGraphHeight = proportionOfYwrtOnlyGraphHeight * graphHeight;

            float actualY = baseY - YwrtOnlyGraphHeight;
            Log.d("actvy", "" + actualY + "_____" + inptx);

            if (xval.length > 1)
                pts[i] = new PointF(inptx, actualY);
            else
                pts[i] = new PointF(inptx, baseY - graphHeight / 2);


            float xlen=(Math.abs(p1x_ - p2x_))*sw * occX;

            inptx+=(xlen)/(xval.length-1 );


            Log.d("haloxea", Float.toString(pts[i].x));
            Log.d("oplmnu", Float.toString(actualY));

        }

        lcol=Color.BLUE;
        lineWidthPlotLines=10;
        lineType=1;

    }

    public static void setXY(float[] xval,float[] yval, int lineStyle, int color, int lineThickness, float occupiedProportionOfX_Axis, float occupiedProportionOfY_Axis) {

        ix=xval;
        iy=yval;
        pts = new PointF[xval.length];
        float inptx = Math.min(p1x_, p2x_)  * sw;
        base_X=inptx;
        float baseY = ((Math.max(p1y_, p2y_)) * sh);
        base_Y=baseY;
        Log.d("lmny", "" + inptx);
        occX = occupiedProportionOfX_Axis;
        occY = occupiedProportionOfY_Axis;
        for (int i = 0; i < xval.length; i++) {

            float proportionOfYwrtOnlyGraphHeight = (yval[i] - minValue(yval)) / diffValue(yval);
            float graphHeight = ((Math.abs(p2y_ - p1y_))  * sh) * occY;
            float YwrtOnlyGraphHeight = proportionOfYwrtOnlyGraphHeight  * graphHeight;

            float actualY = baseY - YwrtOnlyGraphHeight;
            Log.d("actvy", "" + actualY + "_____" + inptx);


            if (xval.length > 1)
                pts[i] = new PointF(inptx, actualY);
            else
                pts[i] = new PointF(inptx, baseY - graphHeight / 2);


            // inptx += ((Math.abs(p1x_ - p2x_)) / (xval.length - 1)) * sw;
            float xlen=(Math.abs(p1x_ - p2x_))*sw * occX;

            inptx+=(xlen)/(xval.length-1 );


            Log.d("haloxea", Float.toString(pts[i].x));
            Log.d("oplmnu", Float.toString(actualY));

        }

        lcol=color;
        lineWidthPlotLines=lineThickness;
        lineType=lineStyle;

    }

    public static void setPointLooks(){
        FilledBool=true;
        pcol=Color.RED;
        pradius=5.0f;
        circBordThickness=7;
    }

    public static void setPointLooks(boolean isFilled, int color, float radius, int borderThickness){
        FilledBool=isFilled;
        pcol=color;
        pradius=radius;
        circBordThickness=borderThickness;
    }

    public static void setLabels(float[] xval,float[] yval){

        inpx=xval;
        inpy=yval;
        x_ratio=2;
        y_ratio=2;
        lab_dist_x=100;
        lab_dist_y=500;
        x_lab_size=50;
        y_lab_size=50;
        xshow=true;
        yshow=true;
        xtit="";
        ytit="";
        //xtit_d=
        titcolor=Color.RED;
        labcol=Color.BLACK;
        labwidth=15;

    }
    public static void setLabels(boolean x_axis_show, boolean y_axis_show, float[] xval,float[] yval, float label_distance_from_x_axis, float label_distance_from_y_axis, float x_label_font_size, float y_label_font_size, int x_axis_labels_spread,  int y_axis_labels_spread, boolean notches_on_axes, String x_title, String y_title, int labels_color, int axes_title_color, int font_boldness){
        xshow=x_axis_show;
        yshow=y_axis_show;
        inpx=xval;
        inpy=yval;
        lab_dist_x=label_distance_from_x_axis;
        lab_dist_y=label_distance_from_y_axis;
        x_lab_size=x_label_font_size;
        y_lab_size=y_label_font_size;
        x_ratio=x_axis_labels_spread;
        y_ratio=y_axis_labels_spread;
        notches=notches_on_axes;
        xtit=x_title;
        ytit=y_title;

        titcolor=axes_title_color;
        labcol = labels_color;
        labwidth=font_boldness;

    }

    public static float minValue(float[] array){
        List<Float> list = new ArrayList<Float>();
        for (float v : array) {
            list.add(v);
        }
        return Collections.min(list);
    }

    public static float maxValue(float[] array){
        List<Float> list = new ArrayList<Float>();
        for (float v : array) {
            list.add(v);
        }
        return Collections.max(list);
    }

    public static float diffValue(float[] array){
        List<Float> list = new ArrayList<Float>();
        for (float v : array) {
            list.add(v);
        }
        if(array.length>1)
            return Collections.max(list)-Collections.min(list);
        else
            return list.get(0);
    }

    public void draw(){
        invalidate();
        requestLayout();
    }
}


