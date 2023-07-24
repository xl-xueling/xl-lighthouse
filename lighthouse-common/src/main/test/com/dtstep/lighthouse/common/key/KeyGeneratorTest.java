package com.dtstep.lighthouse.common.key;

import org.junit.Assert;
import org.junit.Test;

public class KeyGeneratorTest {

    @Test
    public void generateKeyTest(){
        long id = KeyGenerator.generateId();
        Assert.assertNotNull(id);
    }
}
