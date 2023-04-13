package com.example.firstlivewallpaper;

public class HelperFunction {
    String functionName;
    Class[] functionAcceptTypes;
    Object[] functionParameters;

    HelperFunction(String functionName) {
        this.functionName = functionName;
        this.functionAcceptTypes = new Class[0];
        this.functionParameters = new Object[0];
    }

    HelperFunction(String functionName, Class[] functionAcceptTypes, Object[] functionParameters) {
        this.functionName = functionName;
        this.functionAcceptTypes = functionAcceptTypes;
        this.functionParameters = functionParameters;
    }
}