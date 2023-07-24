package com.dtstep.lighthouse.common.util;

import org.junit.Test;

import java.util.UUID;

public class EncryptUtilTest {

    @Test
    public void encryptTest() throws Exception{
        String encryptStr = "eNrtndFuGzkMRbG/Ms+FIUqiRPUPisW+9T2w48musds6cNwCQdB/70wX/QIiQ94BA7RI7dRJDiRTl6Qu36bn4+345a/j8/Txbfp2OU8fpzp64SG91Eo1Tx+mx+u3r/fb6/oUURJKvz6WJ64vy2PrJ4+X+++nf38sj16WF50aHYhl+ZMPRG159OV+fL1fvszLc3kc1q97Oj7On9bvvLx45s6DsxSm5fOxfvvT/M/x++V6+/z6vP4nWh76Pt9eLtevy7/GYRz69OPD9P9LEvdBPRVKvcjyvebH23z/c15/uDOd+OlUjv2JS53zOM0j17mea5eSTnReXvZ+/XdeX/R2f3h5nufzw3/Xvx+Wn/e+/jK/0Cy/BuecC1dJIiWn6ccfb6YMR0JnSOYMK6EzzPbrsKAzLOYMCzzDar8O4fcymzPMgs6w2e/ljs6wxzpUMxT7dQjPcJgzJPQzdrHXKehHm0JxtFEzdCBTKjpDe5nC8OvQXqagnw4LR8ZBzdBepTA6QnuRMho6Q3uRwvAHbHuR0tCjck1xwlYzdKBS0BFGLUXPsERIUTO0FymCfrSp9iqlwe9le5WS0ZM21V6mNPSMQ5XoE1EztJcpPYMz5BR7Wc3QXqYQekxhe50i8OvQXqeghxS2lymEnsVme5nS4dehvUzp6FKP7WVKR08fskRFSs1wRPpQy7ClONpoEdqrFHSx3OxFSoNnWKKmp0VoL1Iaeh92cyBS0IVesxcpBB+VHbR8oee9mkQTsZqhvUgp6HG524uUjL4Ou4P78+gIHbR8oR8Pe4myqJqhvUyp8CGFoyyqZtiiTUTN0F6mCHo9qjuQKegph24vU+q71qPGezJc/pbcHNjaFEZnaB+XC6EzbDu/mrIFQ/u4zPAMHcTlgc7QQfowgzN0YGvT0PeyA1+b973eswVD+/xh7ugMy86vBGzBsO67U2QLhPYypcJvZQe+NoLO0IH7JvzxcO/um1swdHBjFD0sOzC2qQmdob1MKehpbAfONg39bOPA2aajx2UHzjb4McVepxC61nPgbFPh16G9TqnoKVgHzjaCrlMcONswelx24GwDn8Z24GxT4Bna6xRCfz904GxT0dttHFjbCPr50IG1DcOvQwft2PDvhw7avuD3ctRT9AzHzm88bsDQgbWNoGs9B9428GdsB+Y2A72u58HcBj0uO3C3gQ8pvPPh31swdFBOgQ/LPRiqGcq+h91ugXDEZT0tQwfeNhX9hO3A2wZ/He7dgnMLhg4up6BX9RyY28AvQ965feQWDO1ViqB7OTjwtkHP2TiwthH03KEDaxv4mp6kuFehZkgRUtQMc9iyqBk6qKWg3wkQB7UU9Lgs9jIlw78ftrBy0CLs0YqtZihx3VHN0EExBT2kjHD60jOkuCKlZhhXU/QM7WUKo6ccRt25y/0WDDmuLqsZtkhjqxn2qOqpGUq0wGoR2suUBp616clepnR0hBSzFtQMc1zUUzMsYZamZljD01nNMHq+9AzbzmdxbcGwR1FPzVDCcE7NcMTZRsuQ7GUKNXSGFA1LaoY5sthqhvY6RdDPh+TAkBh+L3OYR6oZtkgfahHay5SBLlNIdj6GfguG9jJloB+xcwpvIDVDe5nS4NdhjlGtaoYlxoyqGdrLlAbP0IFMQU8fOhhDDy/1HIyhb/AxRSIuqxnG3BQ1Qwdj6Ds8QwrjQzXDHA5LWoQlQoqaYY0OTjVDDuNDNUMHY+jR09gOxtA3+HUoIfXUDEd0LGkZOhhDj97k4GAKfUYPKQ6m0KMnYB0MocdfhjXcgdQM7VXKgN/LLa7dqhn2GDKqZihhpqtmOCLjoGXoYAg9fD+7gyH0NaEzzPtucqjpXRm22ttwUKBP6AgdzAtu4Aw91OcrOsOdZ742YejAPJLRGZZ9y+VNGDqwZYHfy/apL4Fn6GBgMPxe7vvuZ9+E4SJTfgJqId1F";
        String s1 = ZipUtil.unzip(encryptStr);
        System.out.println(s1);
    }

    @Test
    public void decryptTest() throws Exception{
        System.out.println("ss");
        String str = "00:11:33:45:22:26;" + UUID.randomUUID().toString() + UUID.randomUUID().toString();
        System.out.println("str:" + str);
        String key = UUID.randomUUID().toString();
        String securt = EncryptUtil.encrypt(str,key);
        String original = EncryptUtil.decrypt(securt,key);
        System.out.println("securt:" + securt);
        System.out.println("original:" + original);

    }
}
