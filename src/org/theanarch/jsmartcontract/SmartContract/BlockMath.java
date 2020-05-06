package org.theanarch.jsmartcontract.SmartContract;

import java.util.Random;

public class BlockMath {

    private static Long time;

    //RETURNING THESE DOUBLES WILL ADD A .0 IF ITS EXACT...

    public BlockMath(Long time){
        this.time = time;
    }

    public double PI(){
        return 3.141592653589793;
    }

    public Long round(double x){
        return Math.round(x);
    }

    public double pow(double x, double y){
        return Math.pow(x, y);
    }

    public double sqrt(double x){
        return Math.sqrt(x);
    }

    public double abs(double x){
        return Math.abs(x);
    }

    public double ceil(double x){
        return Math.ceil(x);
    }

    public double floor(double x){
        return Math.floor(x);
    }

    public double sin(double x){
        return Math.sin(x);
    }

    public double cos(double x){
        return Math.cos(x);
    }

    public double min(double x, double y){
        return Math.min(x, y);
    }

    public double max(double x, double y){
        return Math.max(x, y);
    }

    public double random(){
        return new Random(time).nextDouble();
    }
}
