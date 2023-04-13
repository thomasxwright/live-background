package com.example.firstlivewallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BigDotWallpaperV2 extends AnimationWallpaper {

    @Override
    public Engine onCreateEngine() {
        return new BigDotWallpaperV2.BigDotEngine();
    }

    class BigDotEngine extends AnimationEngine {
        int height;
        int width;

        Paint paint = new Paint();
        Shader shader = new Shader();

        Set<BigDot> dots = new HashSet<BigDot>();
        Set<SchoolofDots> dotGroups = new HashSet<SchoolofDots>();


        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            this.height = height;
            this.width = width;

            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
//            SchoolofDots newSchool = new SchoolofDots(1300f, 1500f, 3,100f, 255, Color.rgb(179,0,33), 50);
//            dotGroups.add(newSchool);
            super.onSurfaceCreated(holder);
        }

        @Override
        public Bundle onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
            if ("android.wallpaper.tap".equals(action)) {
                createDot(x, y);
            }
            iteration();
            drawFrame();
            return super.onCommand(action, x, y, z, extras, resultRequested);
        }

        void createDot(int x, int y) {
            int[] colorList = {Color.rgb(255,174,50),
                    Color.rgb(183, 37,18)};
            int color = colorList[(int)Math.round(Math.random())];
            BigDot dot = new BigDot(x, y, 140, color, 1f);
            synchronized (this.dots) {
                this.dots.add(dot);
                Log.d("add", "there are " + dots.size() + " dots" + " and " + dotGroups.size() + " dotgroups");
            }
        }

        @Override
        protected void drawFrame() {
            SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    draw(c);
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }
        }

        void draw(Canvas c) {
            c.save();
            int vanilla =  Color.rgb(250, 244, 222);
            int bloodRed =  Color.rgb(237, 74, 52);
//            c.drawColor(Color.argb(255, 250,244,222));
            c.drawColor(Color.BLACK);

//            paint.setAlpha(254);
//            paint.setColor(bloodRed);

            synchronized (dots) {
//                int radial =  Color.argb(0.90f, 78, 210, 157);
//                int radiall =  Color.argb(0.9f, 210, 30, 7);
                for (BigDot dot : dots) {

//                    int[] colorArray = {Color.RED, Color.BLUE};
//                    float[] alignmentArray = new float[]{0.15f, 1};
//                    RadialGradient radialShader = new RadialGradient(1440f, 1440f, 1800f, colorArray, alignmentArray, Shader.TileMode.CLAMP);
//                    paint.setShader(radialShader);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
                    paint.setColor(Color.rgb(Color.red(dot.color)+ (int)(Math.random() * 4 - 2),
                            Color.green(dot.color)+ (int)(Math.random()*4 - 2),
                            Color.blue(dot.color)+ (int)(Math.random()*6 - 3)));
                    paint.setStyle(Paint.Style.FILL);
                    paint.setAntiAlias(false);
//                    c.drawCircle((float)Math.random()* 1440f, (float)Math.random()* 2960f, (float)Math.random()* 50f + 30, paint);
                    c.drawCircle(dot.x, dot.y, dot.radius, paint);
                }
            }

            c.restore();
        }

        @Override
        protected void iteration() {
            synchronized (dots) {
                for (Iterator<BigDot> it = dots.iterator(); it.hasNext();) {
                    BigDot dot = it.next();
                    dot.tick();
                    if (dot.isDone()) {
                        it.remove();
                    }
                }
            }

            super.iteration();
        }



    }
}
