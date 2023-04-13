package com.example.firstlivewallpaper;

public class GroupDot extends BigDot {


    public GroupDot() {
        this(5, 20f, 1f, 255, 1f);
    }

    public GroupDot(int color, float radius, float distanceMultiplier, int alpha, float rotationSpeed) {
        this.baseColor = this.color = color;
        this.origRadius = this.radius = radius;
        this.distanceMultiplier = distanceMultiplier;
        this.alpha = alpha;
        this.rotationSpeed = rotationSpeed;
    }
}