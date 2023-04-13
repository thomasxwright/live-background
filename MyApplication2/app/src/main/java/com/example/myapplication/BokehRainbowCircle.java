package com.example.myapplication;

import android.graphics.Bitmap;

public class BokehRainbowCircle {
    float origRadius;

    float deltaRadius;

    float radius;

    float origX;

    float deltaX;

    float x;

    float origY;

    float deltaY;

    float y;

    int color;

    int alpha;

    int steps;

    int currentStep;

    Bitmap bitmap;

    public BokehRainbowCircle(float xCenter, float yCenter, float radius,
                              int color, int steps) {
        this.x = xCenter;
        this.origX = xCenter;
        this.deltaX = (float) (40.0 * Math.random() - 20.0);

        this.y = yCenter;
        this.origY = yCenter;
        this.deltaY = (float) (40.0 * Math.random() - 20.0);

        this.origRadius = radius;
        this.radius = radius;
        this.deltaRadius = 0.5f * radius;

        this.color = color;
        this.alpha = 0;

        this.steps = steps;
    }

    void tick() {
        this.currentStep++;

        float fraction = (float) this.currentStep / (float) this.steps;

        this.radius = this.origRadius + fraction * this.deltaRadius;
        this.x = this.origX + fraction * this.deltaX;
        this.y = this.origY + fraction * this.deltaY;

        if (fraction <= 0.25f) {
            this.alpha = (int) (128 * 4.0f * fraction);
        } else {
            this.alpha = (int) (-128 * (fraction - 1) / 0.75f);
        }
    }

    boolean isDone() {
        return this.currentStep > this.steps;
    }
}
