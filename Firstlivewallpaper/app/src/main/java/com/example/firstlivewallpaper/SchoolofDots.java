package com.example.firstlivewallpaper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class SchoolofDots {
    float x, y, baseX, baseY;

    float distanceFromCenter;
    int alpha, whiteAlpha;
    int groupColor;
    float baseRadius;
    float rotation;
    boolean isDone;
    float rotationProgression;
    Queue<BigDot> dotArray = new LinkedList<>();
    int steps;
    float multiplier;
    int currentStep;
    ArrayList<HelperFunction> behavioralFunctions = new ArrayList<>();

    SchoolofDots(float posX, float posY, int numDots, float baseRadius, float distanceFromCenter, int alpha, int groupColor, int steps) {
        this.x = this.baseX = posX;
        this.y = this.baseY = posY;

        this.distanceFromCenter = distanceFromCenter;
        this.alpha = this.whiteAlpha = alpha;
        this.groupColor = groupColor;
        this.baseRadius = baseRadius;
        this.currentStep = 0;
        this.rotation = 0f;
        this.steps = steps;
        multiplier = (float) Math.random() * 1.5f + 0.5f;
        HelperFunction growAndShrink = new HelperFunction("growAndShrinkDots");
        behavioralFunctions.add(growAndShrink);
        HelperFunction rotateDotGroup = new HelperFunction("rotateDotGroup", new Class[]{int.class}, new Object[]{this.currentStep});
        behavioralFunctions.add(rotateDotGroup);
        HelperFunction swingLeftAndRight = new HelperFunction("swingLeftAndRight");
        behavioralFunctions.add(swingLeftAndRight);
        for (int i = 0; i < numDots; i++) {
            BigDot newDot = new GroupDot(groupColor, baseRadius, 1f, 255, (float)Math.pow(2, i));
            setDotPosition(newDot);
            this.dotArray.add(newDot);
        }
        this.setDotsEquidistant();
    }

    public int usesFunction(String functionName) {
        for (HelperFunction helperFunction : behavioralFunctions) {
            if (helperFunction.functionName == functionName) {
                return behavioralFunctions.indexOf(helperFunction);
            }
        }
        return -1;
    }

    public HelperFunction findHelperFunction(String helperFunctionName) {
        int indexOf = usesFunction(helperFunctionName);
        if (indexOf >= 0)
            return behavioralFunctions.get(indexOf);
        return null;
    }

    public void beginFade() {
        HelperFunction fadeOut = new HelperFunction("fadeOut");
        behavioralFunctions.add(fadeOut);
    }

    public void swingLeftAndRight() {
        this.x = this.baseX + (float) (Math.sin((float) this.currentStep / this.steps * 1.35 * multiplier)) * 200;
    }

    public void growAndShrinkDots() {
        for (BigDot bigDot : dotArray) {
            bigDot.radius = baseRadius * ((0.5f) * (float) Math.sin((float) this.currentStep / this.steps * multiplier) + 1);
        }
    }

    public void fadeOut() {
        if (this.whiteAlpha >= 15)
            whiteAlpha-= 15;
        else if (this.baseRadius >= 8) {
            //this is very clunky, it checks if that function is in every time even when it isn't
            HelperFunction function = this.findHelperFunction("growAndShrinkDots");
            if (function != null) {
                function = new HelperFunction("shrinkDots");
            }
            this.baseRadius -= 8;
        }
        else {
            isDone = true;
        }
    }

    public void shrinkDots() {
        for (BigDot bigDot : dotArray) {
            bigDot.radius -= 8;
        }
    }

    public void rotateDotGroup(int rotationSteps) {
        this.rotation = 720 * (float) Math.sin((float) currentStep / this.steps * multiplier);
    }

    public void setDotPosition(BigDot bigDot) {
        bigDot.x = distanceFromCenter * bigDot.distanceMultiplier * (float)Math.cos(Math.toRadians(bigDot.rotation));
        bigDot.y = distanceFromCenter * bigDot.distanceMultiplier * (float)Math.sin(Math.toRadians(bigDot.rotation));
    }

    public void goDown() {
        this.y = 100 + (float) (Math.cos(this.currentStep / (400.0 * multiplier) )) * 1400;
    }

    public void rotateGroup(){
        this.rotation++;
    }

    public void setDotsEquidistant() {
        float incrementalDistance = 360 / dotArray.size();
//        for (int i = 0; i < dotArray.size(); i++) {
//            dotArray.get(i).rotation = i * incrementalDistance;
//        }
        float i = 0;
        for (BigDot bigDot : dotArray) {
            bigDot.rotation = i * incrementalDistance;
            i++;
        }
    }

    void tick() {
        this.currentStep++;
        synchronized (this) {
            for (BigDot bigDot : dotArray) {
                setDotPosition(bigDot);
                bigDot.tick();
            }
        }

        try {
            for (int i = 0; i < behavioralFunctions.size(); i++) {
                Method m = this.getClass().getMethod(behavioralFunctions.get(i).functionName,
                        behavioralFunctions.get(i).functionAcceptTypes);
                Object o = m.invoke(this, behavioralFunctions.get(i).functionParameters);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
