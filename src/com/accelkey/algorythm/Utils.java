package com.accelkey.algorythm;

public class Utils {
    public static final int FULLDEGREE = 180;
    public static final long INTERVAL = 200;

    public static boolean inEpsilon(long x, long y, double e) {
        if((double)x >= (double)y - e && (double)x <= (double)y + e)
            return true;
        return false;
    }

    public static Integer getDeltaArea(Position minus) {
        long a = minus.getXy();
        long b = minus.getXz();
        long c = minus.getYz();
        int r = 0;



        if (a>= -10 && a <= 10 ) r+=100;
        else if (a > 10 && a < 170) r+=200;
        else if (a> -170 && a < -10) r+=300;
        else if (a <= -170 || a >= 170) r+=400;
        if (b>= -10 && b <= 10 ) r+=10;
        else if (b > 10 && b < 170) r+=20;
        else if (b> -170 && b < -10) r+=30;
        else if (b <= -170 || b >= 170) r+=40;
        if (c>= -10 && c <= 10 ) r+=1;
        else if (c > 10 && c < 170) r+=2;
        else if (c> -170 && c < -10) r+=3;
        else if (c <= -170 || c >= 170) r+=4;

        return r;
    }
}
