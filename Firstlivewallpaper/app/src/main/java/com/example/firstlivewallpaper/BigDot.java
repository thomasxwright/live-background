package com.example.firstlivewallpaper;

import android.graphics.Color;

public class BigDot {

    float origX, deltaX, x;

    float origY, deltaY, y;

    float origRadius, radius;

    float rotation, rotationSpeed;

    int baseColor;

    int color;

    float distanceMultiplier;

    int alpha;

    String state;

    int currentStep;
    int steps;
    float fraction;

    public BigDot() {
        this(0f, 0f, 200f, Color.BLUE, 1f);
    }

    public BigDot(float xCenter, float yCenter, float radius, int color, float rotationSpeed) {
        this.x = origX = xCenter;

        this.y = origY = yCenter;

        this.radius = origRadius = radius;

        this.rotation = (float) Math.random()* 360f;
        this.rotation = 0f;
        this.rotationSpeed = (float) Math.random()* 3f;

        this.color = this.baseColor = color;

        this.distanceMultiplier = 1.0f;

        this.alpha = 0;

        this.state = "waiting";

        this.currentStep = 0;
        this.steps = 25;

        deltaX = (int) ((Math.random() - 0.5f ) * 2 * 1000);
        deltaY = (int) ((Math.random() - 0.5f ) * 2 * 1000);

//        if (Math.random() > 0.5) {
//            this.deltaX = (int) ((Math.random()-0.5f) * 2 * 1000);
//            this.deltaY = 0;
//        }
//        else {
//            this.deltaY = (int) ((Math.random() - 0.5f) * 2 * 1000);
//            this.deltaX = 0;
//        }
    }

    float animateProgress(float multiplier) {
        float adjustedStart = fraction * multiplier;
        if (adjustedStart > 1)
            adjustedStart = 1;
        float progression = (float) (3 * Math.pow(adjustedStart, 2) - 2 * Math.pow(adjustedStart, 3));
        return progression;
    }


    //curve_stretching at 1 is neutral.  Higher number = more dramatic.  Lower = less of a rush.  Going below 0.2 makes it actually ease in a bit
    float animateProgressEaseOutSin(float multiplier, float curve_stretching) {
        float adjustedStart = fraction * multiplier;
        if (adjustedStart > 1)
            adjustedStart = 1;
        float progression = (float) (Math.sin(Math.PI * (Math.pow(adjustedStart, 0.4 * (1 / curve_stretching))) / 2 ));
        return progression;
    }

    float animateProgressEase(float multiplier, float curve_stretching) {
        float adjustedStart = fraction * multiplier;
        if (adjustedStart > 1)
            adjustedStart = 1;
        float progression = (float) (3 * Math.pow(adjustedStart, 2 * curve_stretching) - 2 * Math.pow(adjustedStart, 3 * curve_stretching));
        return progression;
    }

    void tick() {
        this.currentStep++;
//        this.rotation += rotationSpeed;
        float easedProgression = animateProgressEase(1, 1.0f);
//        float progressionRushedOut = animateProgressEase(1, 2.5f);

//        fraction = (float) this.currentStep / (float) this.steps;

//        int red = Color.red(this.color);

//        color = Color.rgb(Color.red(this.color) - 1, Color.green(this.color), Color.blue(this.color));

//        radius = origRadius * (1 - easedProgression);

//        x = origX + fraction * deltaX;
//        y = origY + fraction * deltaY;

//        x = origX + progressionRushedOut * deltaX;
//        y = origY + progressionRushedOut * deltaY;
    }

    boolean isDone() {
        return this.radius < 1;
    }

}
