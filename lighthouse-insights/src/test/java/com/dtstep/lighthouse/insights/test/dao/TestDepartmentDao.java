package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.common.modal.Department;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestDepartmentDao {

    @Autowired
    private DepartmentDao departmentDao;

    @Test
    public void testInsert() {
        Department department = new Department();
        department.setCreateTime(new Date());
        department.setUpdateTime(new Date());
        department.setName("sssvvv");
        int result = departmentDao.insert(department);
        System.out.println(result);
    }

    @Test
    public void testUpdate() {
        Department department = new Department();
        department.setId(10054);
        department.setName("aaaa");
        department.setUpdateTime(new Date());
        departmentDao.update(department);
    }

    @Test
    public void testQueryById(){
        int id = 10054;
        Department department = departmentDao.queryById(id);
        System.out.println("department:" + JsonUtil.toJSONString(department));
    }

    @Test
    public void testQueryAll(){
        PageHelper.startPage(1,1);
        try{
            List<Department> departmentList = departmentDao.queryAll();
            PageInfo<Department> departmentPageInfo = new PageInfo<>(departmentList);
            System.out.println("departmentPageInfo:" + departmentPageInfo.getTotal());
        }finally {
            PageHelper.clearPage();
        }


    }

    @Test
    public void deleteById(){
        int id = 10055;
        int res = departmentDao.deleteById(id);
        System.out.println("res:" + res);
    }

    @Test
    public void getLevel(){
        int level = departmentDao.getLevel(0);
        System.out.println("level:" + level);
    }
}
