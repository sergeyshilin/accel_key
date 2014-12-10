package com.accelkey.algorythm;

public class Utils {
    public static final int FULLDEGREE = 180;
    public static final int TEN = 20;    // first was TEN = 10
    public static final int OHS = 160;   // first was One Hundred Seventy = 170

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

        if (a>= -TEN && a <= TEN ) r+=100;
        else if (a > TEN && a < OHS) r+=200;
        else if (a> -OHS && a < -TEN) r+=300;
        else if (a <= -OHS || a >= OHS) r+=400;
        if (b>= -TEN && b <= TEN ) r+=10;
        else if (b > TEN && b < OHS) r+=20;
        else if (b> -OHS && b < -TEN) r+=30;
        else if (b <= -OHS || b >= OHS) r+=40;
        if (c>= -TEN && c <= TEN ) r+=1;
        else if (c > TEN && c < OHS) r+=2;
        else if (c> -OHS && c < -TEN) r+=3;
        else if (c <= -OHS || c >= OHS) r+=4;

        if (r > 200 && r < 300) r += 200;
        else if (r > 200 && r < 400) r -= 200;
        if (r % 100 > 20 && r % 100 < 30) r += 20;
        else if (r % 100 > 20 && r % 100 < 40) r -= 20;
        if (r % 10 >= 2 && r % 10 < 3) r += 2;
        else if (r % 10 > 2 && r % 10 < 4) r -= 2;

        if (r == 411)
            return 111;
//        if (r == 114)
//            return 111;

        return r;
    }
}
