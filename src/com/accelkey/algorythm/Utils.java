package com.accelkey.algorythm;

public class Utils {
    public static final int FULLDEGREE = 18;
    public static final long INTERVAL = 100;

    public static boolean inEpsilon(long x, long y, double e) {
        if((double)x >= (double)y - e && (double)x <= (double)y + e)
            return true;
        return false;
    }

    public static Integer getDeltaArea(Position minus) {
        long a = minus.getXy();
        long b = minus.getXz();
        long c = minus.getYz();

        if(a >= 0 && b >= 0 && c >= 0) return 1;
        else if(a <  0 && b >= 0 && c >= 0) return 2;
        else if(a >= 0 && b <  0 && c >= 0) return 3;
        else if(a <  0 && b <  0 && c >= 0) return 4;
        else if(a >= 0 && b >= 0 && c <  0) return 5;
        else if(a <  0 && b >= 0 && c <  0) return 6;
        else if(a >= 0 && b <  0 && c <  0) return 7;
        else if(a <  0 && b <  0 && c <  0) return 8;
        else return 0;
    }
}
