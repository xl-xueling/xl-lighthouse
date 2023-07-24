package com.dtstep.lighthouse.common.util;

public class CalculateUtil {

    public static long getMaxDivisor(long m,long n){
        long temp;
        if (n > m) {
            temp = n;
            n = m;
            m = temp;
        }
        if (m % n == 0) {
            return n;
        }
        return getMaxDivisor(n, m % n);
    }
}
