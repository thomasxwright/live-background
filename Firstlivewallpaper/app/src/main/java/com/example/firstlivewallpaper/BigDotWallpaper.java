package com.example.firstlivewallpaper;

import android.app.WallpaperManager;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class BigDotWallpaper extends AnimationWallpaper {

    @Override
    public Engine onCreateEngine() {
        return new BigDotEngine();
    }

    class BigDotEngine extends AnimationEngine {
        int height;
        int width;

        Paint paint = new Paint();
        Shader shader = new Shader();

        Set<BigDot> dots = new HashSet<BigDot>();
//        Set<SchoolofDots> dotGroups = new HashSet<SchoolofDots>();
        Queue<SchoolofDots> dotGroups = new LinkedList<>();


        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            Log.d("tag1", "onCreate()");


            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
        }


        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            this.height = height;
            this.width = width;
            Log.d("tag1", "onSurfaceChanged()");

            super.onSurfaceChanged(holder, format, width, height);
        }
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
//            SchoolofDots newSchool = new SchoolofDots(700f, 1100f, 40f, 255, Color.rgb(179,0,33), 50);
//            dotGroups.add(newSchool);
            Log.d("tag1", "onSurfaceCreated()");

//            super.onSurfaceCreated(holder);
        }

        @Override
        public Bundle onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
            if (action.equals(WallpaperManager.COMMAND_TAP)) {
//            if ("android.wallpaper.tap".equals(action)) {
                int red = Color.rgb(179, 0, 33);
                int pink = Color.rgb(250, 31, 111);
                int blue = Color.rgb (23, 23, 206);
                int yellow = Color.rgb(243, 215, 30);
                int purple = Color.rgb(109, 2, 192);
                int[] colors = {red, blue, purple};
                float randX = (float) Math.random() * width;
                float randY = (float) Math.random() * height;
                int randColor = Color.rgb((int) (Math.random()*255), (int) (Math.random() * 255), (int) (Math.random() * 255));
                float randomSize = (float)Math.random();
                float baseRadius = (float) randomSize * (width/15) + (width/25);
                float distance = baseRadius * (float) (1 + (Math.random() - 0.5) * 0.6);
                int num = (int) (randomSize * 5) + 3;
                SchoolofDots newSchool = new SchoolofDots(x, y, num,baseRadius, distance, 255, colors[new Random().nextInt(colors.length)], 300);
                dotGroups.add(newSchool);
                if (dotGroups.size() > 3) {
                    //Go through the dot clusters until you find one that isn't vanishing
                    for (SchoolofDots dotGroup : dotGroups) {
                        if (dotGroup.usesFunction("fadeOut") == -1) {
                            dotGroup.beginFade();
                            break;
                        }
                    }
                }
            }
            return super.onCommand(action, x, y, z, extras, resultRequested);
        }

//        @Override
//        public void onTouchEvent(MotionEvent event) {
//            if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                SchoolofDots newSchool = new SchoolofDots(700f, 1100f, 100f, 255, Color.rgb(179,0,33), 50);
//                dotGroups.add(newSchool);
//            }
//            super.onTouchEvent(event);
//        }

        void createDot(int x, int y) {
            int[] colorList = {Color.rgb(255,174,50),
                    Color.rgb(183, 37,18)};
            int color = colorList[(int)Math.round(Math.random())];
            BigDot dot = new BigDot(x, y, 140, color, 1);
            synchronized (this.dots) {
                this.dots.add(dot);
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
            c.drawColor(0xff000000);

            synchronized (dotGroups) {
                for (SchoolofDots schoolOfDots : dotGroups) {
                    c.save();
                    c.translate(schoolOfDots.x, schoolOfDots.y);
                    c.rotate(schoolOfDots.rotation);
                    paint.setColor(Color.WHITE);
                    for ( BigDot bigDot : schoolOfDots.dotArray) {
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                        paint.setStyle(Paint.Style.FILL);
                        paint.setAlpha(schoolOfDots.whiteAlpha);
                        paint.setAntiAlias(true);
                        c.drawCircle(bigDot.x, bigDot.y, bigDot.radius, paint);
                    }
                    c.restore();
                }
            }

            synchronized (dotGroups) {
                for (SchoolofDots schoolOfDots : dotGroups) {
                    c.save();
                    c.translate(schoolOfDots.x, schoolOfDots.y);
                    c.rotate(schoolOfDots.rotation);
                    for ( BigDot bigDot : schoolOfDots.dotArray) {
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
                        paint.setColor(schoolOfDots.groupColor);
                        paint.setStyle(Paint.Style.FILL);
                        paint.setAlpha(schoolOfDots.alpha);
                        paint.setAntiAlias(true);
                        c.drawCircle(bigDot.x, bigDot.y, bigDot.radius, paint);
                    }
                    c.restore();
                }
            }

            c.restore();
        }

        @Override
        protected void iteration() {
            synchronized (dotGroups) {
                for (Iterator<SchoolofDots> it = dotGroups.iterator(); it.hasNext();) {
                    SchoolofDots dotGroup = it.next();
                    dotGroup.tick();
                    if (dotGroup.isDone) {
                        it.remove();
                    }
                }
            }

            super.iteration();
        }



    }
}
