package com.dtstep.lighthouse.common.util;
import org.junit.Test;

public class SnappyUtilTest {

    @Test
    public void snappyCompress() throws Exception{
        String str = "eNrtnc1OY0cQhZVXuesR6r/qrvYbjKLsskfGviRWZjAyZiSE5t1znZCssorqRJHOYQPY+Np86r5VX/Xf+/K8v+y//rR/Xnbvy+vpuOyeXr98+bQczq9P18vbsluyJS+W/vhaPi3nl+2x2w+H0/Wvp1P6++nTdqGltDR3PnPaHWpKu3LIj7vhre/c191Mpe6Oc3/765fr/u16+rpur/Fyd3vkcX9YPx9v103ZexmtztRKt1aSb08/rL/uv53Ol5/fnm8vyttD39bLy+n8tP027+adL9+3D3G7QCut5OEjNR8tl1H79sd/vlk2zzUN97Fdd/sU6+GyXn9cb//OMT/Y40Pdj0erbS3zYZ2lre3Yhtf0kI+3a5x/W29vd7nevzyv6/H+y/mX++0/uS7ff3j/3/C0huY5qHjmhObpXDwzmuek4jkmmGdLVDwrGmemwlkczbNw8YR390rF0+E8m3iG8jSu/j7QPLvaZyhPMj1C63vj0qPZ0Ty59KijddO49AhdDTHpUSxPLj1yQ/Pk0qOCriYblx5ldHg3Lj0ydDpvZHqE1k0b0vdQnmR6hC7P21T+Gcmzc+lRR5dDOpcfGdo3e9FshlCeZH6ExsmlRw2tR92k76E8ufSooMtLfej2GYnTlS2F8tTculCeI2lucihPMjtCp0uDy44qvL9X3T9DeZKNHsF5cukRHGdX8wzlyWVHDZ3OD9fgeyhPsrl1aJ7OpUcF3d+dS48yerDYi8pLoTzJ9AiNsykchfI0LZUJ5cmlRx1++xyqLoXydLXPUJ5aehTKc3LpUUbPVZxceoQejJtkdgTv7lx2VNH2Prn0yOC3Ty49qvD+Trb0CI2Ty44czpPLjhyeLnHZkYGrn56SpiaH8uSyo+lonlx61ODts2qni1CeTRsvhfLk0qM+0Ty59KjB49FQvhTK09U+Q3lOLd2M5Jm5/Gig22cmGz1Cx/dcNFocyrPKN0N5yo9ieWpj71ieXefKhPLk8qMOb59kezPA4xGXHzV0fy9JO4OF8uTyo4rOl0rRTpWhPMkWH6HrS0XT62J5cvnRKGieXH404PGdy48mPP/U3nWxPLn8aKL9vWp+XSzPrJMmQnmS7V2Hzudr1cGGoTx1MGwsT9NJKKE8u/LPUJ5cflTR9ZDqqoeE8tTmdaE8G9n4ETqfb1x+NODtsyhfCuXJ5UcdHd9bU3wP5WmaHxLKU+NHsTyHtruIxOlaDhvKU9szhPI0sqOP0OmncelRQYd3K1q+GcqTS48MPdxhTctlQnmalsOG8uTSo4nWIxvK50N5uobjQnmSDR+BcXYuPXK0HnUuPero2WC96CipUJ5Vu9OG8mw6GyGUp+ko01CeXbNtQnkOlT9Debp4hvLk0qOKLteNpKOLQ3lm7XYRw3PmnrZv/9w+q8rJ/5JnpuIJHO744FmoeFZ4f69UPIF+9MGzUfGc8P5uVDwbPB51Kp5W0DwHV/uE83Su9pnRPCdXPDIwT+PyI3R3Ny49cjhPLj1ydDpvVfoeypNLjwa8v3PpEboaYlx2VCqaJ5cdTXg4IrMjNE4uOUIH987lRg3t7p1Ljjq6u3cuOero6N7Jxo7QyXznkqOOzuY7lxwZPL53pUuROMmGjtBy1LnkqMBvn1PyHslzcOnRbGieXHqU0bo5uPTI0Ho0uPTI0fo+mtpnKE/TVJtQnl35ZyhPLj9Cz/wcXHrk8Nvn1GhHJE/n0qOOriY7lx4Z+v7pRdXkUJ5cejTQ8ci59KjA4xGXHhV0uc67ZjOE8hxaGRfKk8uPKpzn1Mq4SJ6Ty4+AO4d88OTyo4bO5yeXH8F3YplcfjTh/b2p/hnKk8uPHH7/5PIjR+efk8uPDO3vk8uP0OWlORXeA3nmlDSbNpRnVvsM5Vm0uCOU53+gR78DrPCxbQ==";
        String origin = ZipUtil.unzip(str);
        String zipStr = SnappyUtil.compress(origin);
        String unprocess = SnappyUtil.uncompress(zipStr);
        System.out.println("unprocess:" + unprocess);
    }


    @Test
    public void zipTest() throws Exception{
        String str = "eNrtnc1OY0cQhZVXuesR6r/qrvYbjKLsskfGviRWZjAyZiSE5t1znZCssorqRJHOYQPY+Np86r5VX/Xf+/K8v+y//rR/Xnbvy+vpuOyeXr98+bQczq9P18vbsluyJS+W/vhaPi3nl+2x2w+H0/Wvp1P6++nTdqGltDR3PnPaHWpKu3LIj7vhre/c191Mpe6Oc3/765fr/u16+rpur/Fyd3vkcX9YPx9v103ZexmtztRKt1aSb08/rL/uv53Ol5/fnm8vyttD39bLy+n8tP027+adL9+3D3G7QCut5OEjNR8tl1H79sd/vlk2zzUN97Fdd/sU6+GyXn9cb//OMT/Y40Pdj0erbS3zYZ2lre3Yhtf0kI+3a5x/W29vd7nevzyv6/H+y/mX++0/uS7ff3j/3/C0huY5qHjmhObpXDwzmuek4jkmmGdLVDwrGmemwlkczbNw8YR390rF0+E8m3iG8jSu/j7QPLvaZyhPMj1C63vj0qPZ0Ty59KijddO49AhdDTHpUSxPLj1yQ/Pk0qOCriYblx5ldHg3Lj0ydDpvZHqE1k0b0vdQnmR6hC7P21T+Gcmzc+lRR5dDOpcfGdo3e9FshlCeZH6ExsmlRw2tR92k76E8ufSooMtLfej2GYnTlS2F8tTculCeI2lucihPMjtCp0uDy44qvL9X3T9DeZKNHsF5cukRHGdX8wzlyWVHDZ3OD9fgeyhPsrl1aJ7OpUcF3d+dS48yerDYi8pLoTzJ9AiNsykchfI0LZUJ5cmlRx1++xyqLoXydLXPUJ5aehTKc3LpUUbPVZxceoQejJtkdgTv7lx2VNH2Prn0yOC3Ty49qvD+Trb0CI2Ty44czpPLjhyeLnHZkYGrn56SpiaH8uSyo+lonlx61ODts2qni1CeTRsvhfLk0qM+0Ty59KjB49FQvhTK09U+Q3lOLd2M5Jm5/Gig22cmGz1Cx/dcNFocyrPKN0N5yo9ieWpj71ieXefKhPLk8qMOb59kezPA4xGXHzV0fy9JO4OF8uTyo4rOl0rRTpWhPMkWH6HrS0XT62J5cvnRKGieXH404PGdy48mPP/U3nWxPLn8aKL9vWp+XSzPrJMmQnmS7V2Hzudr1cGGoTx1MGwsT9NJKKE8u/LPUJ5cflTR9ZDqqoeE8tTmdaE8G9n4ETqfb1x+NODtsyhfCuXJ5UcdHd9bU3wP5WmaHxLKU+NHsTyHtruIxOlaDhvKU9szhPI0sqOP0OmncelRQYd3K1q+GcqTS48MPdxhTctlQnmalsOG8uTSo4nWIxvK50N5uobjQnmSDR+BcXYuPXK0HnUuPero2WC96CipUJ5Vu9OG8mw6GyGUp+ko01CeXbNtQnkOlT9Debp4hvLk0qOKLteNpKOLQ3lm7XYRw3PmnrZv/9w+q8rJ/5JnpuIJHO744FmoeFZ4f69UPIF+9MGzUfGc8P5uVDwbPB51Kp5W0DwHV/uE83Su9pnRPCdXPDIwT+PyI3R3Ny49cjhPLj1ydDpvVfoeypNLjwa8v3PpEboaYlx2VCqaJ5cdTXg4IrMjNE4uOUIH987lRg3t7p1Ljjq6u3cuOero6N7Jxo7QyXznkqOOzuY7lxwZPL53pUuROMmGjtBy1LnkqMBvn1PyHslzcOnRbGieXHqU0bo5uPTI0Ho0uPTI0fo+mtpnKE/TVJtQnl35ZyhPLj9Cz/wcXHrk8Nvn1GhHJE/n0qOOriY7lx4Z+v7pRdXkUJ5cejTQ8ci59KjA4xGXHhV0uc67ZjOE8hxaGRfKk8uPKpzn1Mq4SJ6Ty4+AO4d88OTyo4bO5yeXH8F3YplcfjTh/b2p/hnKk8uPHH7/5PIjR+efk8uPDO3vk8uP0OWlORXeA3nmlDSbNpRnVvsM5Vm0uCOU53+gR78DrPCxbQ==";
        String origin = ZipUtil.unzip(str);
        String zipStr = SnappyUtil.compress(origin);
        int size = 10000;
        for(int n=0;n<100;n++){
            long t3 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                String res = SnappyUtil.uncompress(zipStr);
            }
            long t4 = System.currentTimeMillis();
            System.out.println("cost:" + (t4 - t3));
            Thread.sleep(1000);
        }

    }

    @Test
    public void test3() throws Exception{
        String str = "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";
        String s = SnappyUtil.uncompress(str);
        System.out.println(s);
    }

}
