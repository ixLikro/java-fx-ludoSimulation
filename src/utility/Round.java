/*
 * CpC - Carport calculator
 *
 * Copyright (c) 2018. by Ole Schleeßelmann.
 */

package utility;

public class Round {
    public static double to2Digits(double val){
        return toXDigits(val,2);
    }

    public static float to2Digits(float val){
        return toXDigits(val,2);
    }

    public static double to3Digits(double val){
        return toXDigits(val,3);
    }

    public static float to3Digits(float val){
        return toXDigits(val,3);
    }

    public static double toXDigits(double val, int X){
        return (Math.round(val * Math.pow(10,X))) / Math.pow(10,X);
    }

    public static float toXDigits(float val, int X){
        return (Math.round(val * Math.pow(10,X))) / (float)Math.pow(10,X);
    }

}
