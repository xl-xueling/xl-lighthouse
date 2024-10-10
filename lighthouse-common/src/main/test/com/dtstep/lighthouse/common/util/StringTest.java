package com.dtstep.lighthouse.common.util;

import org.junit.Test;

public class StringTest {

    @Test
    public void test1() throws Exception {
        String s = String.format("WITH RECURSIVE role_hierarchy AS (\n" +
                "    SELECT id, pid\n" +
                "    FROM ldp_roles\n" +
                "    WHERE id = '%s'\n" +
                "    UNION ALL\n" +
                "    SELECT r.id, r.pid\n" +
                "    FROM ldp_roles r\n" +
                "    INNER JOIN role_hierarchy rh ON rh.pid = r.id\n" +
                ")\n" +
                "\n" +
                "SELECT \n" +
                "    CASE WHEN EXISTS (\n" +
                "        SELECT 1\n" +
                "        FROM ldp_permissions p\n" +
                "        INNER JOIN role_hierarchy rh ON rh.id = p.role_id\n" +
                "        WHERE p.owner_id = '%s'\n" +
                "        AND p.owner_type = '%s'\n" +
                "        AND (p.expire_time = null || p.expire_time > NOW())\n" +
                "    )\n" +
                "    THEN 'true'\n" +
                "    ELSE 'false'\n" +
                "    END AS has_permission",1224,11033,3);
        System.out.println("s is:" + s);

    }
}
