package com.dtstep.lighthouse.insights.test.utils;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class TestPbkdfUtil {

    @Test
    public void testPbkdf() throws Exception{
        String s = "123456";
//        String res1 = new Pbkdf2PasswordEncoder().encode(s);
//        System.out.println("result:" + res1);
//        String res2 = new BCryptPasswordEncoder(10).encode(s);
//        System.out.println("result:" + res2);

        boolean is = new BCryptPasswordEncoder().matches(s,"$2a$10$dSBuhvzQCzK9F1LJzMoh8Ob.0glRQ8tC4VkOOupMCdzwEmYnFc6Bi");
        System.out.println("is:" + is);
    }
}
