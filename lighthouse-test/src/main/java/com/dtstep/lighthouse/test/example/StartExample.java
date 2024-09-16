package com.dtstep.lighthouse.test.example;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.enums.*;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.dtstep.lighthouse.core.template.TemplateContext;
import com.dtstep.lighthouse.core.template.TemplateParser;
import com.dtstep.lighthouse.test.relation.*;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StartExample {

    private static final Logger logger = LoggerFactory.getLogger(StartExample.class);

    private static final ExampleContext exampleContext = new ExampleContext();

    public static void main(String[] args) {
        try{
            LDPConfig.loadConfiguration();
            Group group = GroupHandler.queryGroupInfo(SysConst.TEST_SCENE_BEHAVIOR_STAT);
            if(group == null){
                User user = UserHandler.queryUserInfo(SysConst._ADMIN_USER_NAME);
                Validate.notNull(user);
                exampleContext.setAdminId(user.getId());
                exampleContext.setDepartmentId(user.getDepartmentId());
                Integer metaId = MetaHandler.queryValidMetaId();
                Validate.isTrue(metaId != null && metaId > 0);
                exampleContext.setMetaId(metaId);
                createExample();
                logger.info("Create statistic examples success!");
            }else{
                logger.info("Statistic examples already exists,groupId:"+ group.getId());
                startExample();
            }
        }catch (Exception ex){
            logger.error("Failed to create statistic examples!",ex);
            System.exit(1);
        }
        System.exit(0);
    }

    public static void startExample() throws Exception {
        Group groupInfo = GroupHandler.queryGroupInfo(SysConst.TEST_SCENE_BEHAVIOR_STAT);
        if(groupInfo == null){
            return;
        }
        Connection conn = null;
        try {
            conn = CMDBStorageEngineProxy.getInstance().getConnection();
            conn.setAutoCommit(false);
            StatHandler.startByGroupId(conn,groupInfo.getId());
            conn.commit();
        }catch (Exception ex){
            ex.printStackTrace();
            if(conn != null){
                conn.rollback();
            }
        }finally {
            CMDBStorageEngineProxy.getInstance().closeConnection();
        }
    }

    private static void createExample() throws Exception {
        Connection conn = null;
        try {
            conn = CMDBStorageEngineProxy.getInstance().getConnection();
            conn.setAutoCommit(false);
            createProject(conn);
            createGroup(conn);
            createStats(conn);
            conn.commit();
        }catch (Exception ex){
            ex.printStackTrace();
            if(conn != null){
                conn.rollback();
            }
        }finally {
            CMDBStorageEngineProxy.getInstance().closeConnection();
        }
    }


    private static void createProject(Connection conn) throws Exception {
        Project project = new Project();
        project.setTitle("XL-LightHouse演示工程");
        project.setBuiltIn(false);
        project.setDepartmentId(exampleContext.getDepartmentId());
        project.setPrivateType(PrivateTypeEnum.Public);
        project.setDesc("用于演示XL-LightHouse项目使用！");
        LocalDateTime localDateTime = LocalDateTime.now();
        project.setCreateTime(localDateTime);
        project.setUpdateTime(localDateTime);
        Integer projectId = ProjectHandler.createProject(conn,project);
        int departmentManageRoleId = RoleHandler.queryRoleId(conn,project.getDepartmentId(), RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION);
        exampleContext.setDepartManageRoleId(departmentManageRoleId);

        Role manageRole = new Role();
        manageRole.setPid(departmentManageRoleId);
        manageRole.setResourceId(projectId);
        manageRole.setRoleType(RoleTypeEnum.PROJECT_MANAGE_PERMISSION);
        manageRole.setCreateTime(localDateTime);
        manageRole.setUpdateTime(localDateTime);
        manageRole.setDesc(RoleTypeEnum.PROJECT_MANAGE_PERMISSION+"("+project.getTitle()+")");
        int manageRoleId = RoleHandler.createRole(conn,manageRole);
        exampleContext.setProjectManageRoleId(manageRoleId);
        int departmentAccessRoleId = RoleHandler.queryRoleId(conn,project.getDepartmentId(), RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION);
        exampleContext.setDepartAccessRoleId(departmentAccessRoleId);
        project.setId(projectId);
        exampleContext.setProject(project);

        Role accessRole = new Role();
        accessRole.setPid(departmentAccessRoleId);
        accessRole.setResourceId(projectId);
        accessRole.setRoleType(RoleTypeEnum.PROJECT_ACCESS_PERMISSION);
        accessRole.setCreateTime(localDateTime);
        accessRole.setUpdateTime(localDateTime);
        accessRole.setDesc(RoleTypeEnum.PROJECT_ACCESS_PERMISSION+"("+project.getTitle()+")");
        int accessRoleId = RoleHandler.createRole(conn,accessRole);
        exampleContext.setProjectAccessRoleId(accessRoleId);

        Permission managePermission = new Permission();
        managePermission.setRoleId(manageRoleId);
        managePermission.setOwnerId(exampleContext.getAdminId());
        managePermission.setOwnerType(OwnerTypeEnum.USER);
        managePermission.setCreateTime(localDateTime);
        managePermission.setUpdateTime(localDateTime);
        PermissionHandler.createPermission(conn,managePermission);

        Permission accessPermission = new Permission();
        accessPermission.setRoleId(accessRoleId);
        accessPermission.setOwnerId(exampleContext.getAdminId());
        accessPermission.setOwnerType(OwnerTypeEnum.USER);
        accessPermission.setCreateTime(localDateTime);
        accessPermission.setUpdateTime(localDateTime);
        PermissionHandler.createPermission(conn,accessPermission);
        logger.info("create project example completed,projectId:{}",project.getId());
    }

    public static void createGroup(Connection connection) throws Exception {
        Group group = new Group();
        LocalDateTime localDateTime = LocalDateTime.now();
        group.setProjectId(exampleContext.getProject().getId());
        group.setState(GroupStateEnum.RUNNING);
        group.setRefreshTime(localDateTime);
        group.setUpdateTime(localDateTime);
        group.setCreateTime(localDateTime);
        group.setToken(SysConst.TEST_SCENE_BEHAVIOR_STAT);
        String columns = "[{\"name\":\"request_id\",\"type\":\"string\",\"comment\":\"列表请求ID\"},{\"name\":\"imei\",\"type\":\"string\",\"comment\":\"用户ID\"},{\"name\":\"behavior_type\",\"type\":\"string\",\"comment\":\"1：曝光，2：点击\"},{\"name\":\"province\",\"type\":\"string\",\"comment\":\"用户所在省份\"},{\"name\":\"city\",\"type\":\"string\",\"comment\":\"用户所在城市\"},{\"name\":\"item_id\",\"type\":\"string\",\"comment\":\"帖子ID\"},{\"name\":\"top_level\",\"type\":\"string\",\"comment\":\"帖子一级分类\"},{\"name\":\"sec_level\",\"type\":\"string\",\"comment\":\"帖子二级分类\"},{\"name\":\"score\",\"type\":\"number\",\"comment\":\"帖子质量分\"},{\"name\":\"recall_no\",\"type\":\"string\",\"comment\":\"召回模型标识\"},{\"name\":\"app_version\",\"type\":\"string\",\"comment\":\"APP版本号\"}]";
        List<Column> columnList = JsonUtil.toJavaObjectList(columns,Column.class);
        group.setColumns(columnList);
        group.setRandomId(RandomID.id(32));
        group.setSecretKey(RandomID.id(40));
        group.setDesc("Feed召回列表用户行为数据统计，包括曝光量、点击量、点击率等指标数据。");
        Integer groupId = GroupHandler.createGroup(connection,group);
        group.setId(groupId);
        exampleContext.setGroup(group);

        Role manageRole = new Role();
        manageRole.setPid(exampleContext.getProjectManageRoleId());
        manageRole.setResourceId(groupId);
        manageRole.setRoleType(RoleTypeEnum.GROUP_MANAGE_PERMISSION);
        manageRole.setCreateTime(localDateTime);
        manageRole.setUpdateTime(localDateTime);
        manageRole.setDesc(RoleTypeEnum.GROUP_MANAGE_PERMISSION+"("+group.getToken()+")");
        int manageRoleId = RoleHandler.createRole(connection,manageRole);
        exampleContext.setGroupManageRoleId(manageRoleId);

        Role accessRole = new Role();
        accessRole.setPid(exampleContext.getProjectAccessRoleId());
        accessRole.setResourceId(groupId);
        accessRole.setRoleType(RoleTypeEnum.GROUP_ACCESS_PERMISSION);
        accessRole.setCreateTime(localDateTime);
        accessRole.setUpdateTime(localDateTime);
        accessRole.setDesc(RoleTypeEnum.GROUP_ACCESS_PERMISSION+"("+group.getToken()+")");
        int accessRoleId = RoleHandler.createRole(connection,accessRole);
        exampleContext.setGroupAccessRoleId(accessRoleId);

        Permission managePermission = new Permission();
        managePermission.setRoleId(manageRoleId);
        managePermission.setOwnerId(exampleContext.getAdminId());
        managePermission.setOwnerType(OwnerTypeEnum.USER);
        managePermission.setCreateTime(localDateTime);
        managePermission.setUpdateTime(localDateTime);
        PermissionHandler.createPermission(connection,managePermission);

        Permission accessPermission = new Permission();
        accessPermission.setRoleId(accessRoleId);
        accessPermission.setOwnerId(exampleContext.getAdminId());
        accessPermission.setOwnerType(OwnerTypeEnum.USER);
        accessPermission.setCreateTime(localDateTime);
        accessPermission.setUpdateTime(localDateTime);
        PermissionHandler.createPermission(connection,accessPermission);
        logger.info("create group example completed,groupId:{}",group.getId());
    }

    private static final List<Pair<String,String>> templateList = List.of(
            Pair.of("1-minute","<stat-item  title=\"每分钟_日志量\" stat=\"count()\"  />"),
            Pair.of("1-minute","<stat-item  title=\"每分钟_uv量\" stat=\"bitcount(imei)\"  />"),
            Pair.of("1-minute","<stat-item  title=\"每分钟_下发帖子量\" stat=\"bitcount(item_id)\"  />"),
            Pair.of("1-minute","<stat-item  title=\"每分钟_各省份_曝光/点击量\" stat=\"count()\" dimens=\"province;behavior_type\"  />"),
            Pair.of("1-minute","<stat-item  title=\"每分钟_接口请求量\" stat=\"bitcount(request_id)\"  />"),
            Pair.of("1-day","<stat-item  title=\"每天_各一级分类_帖子下发量\" stat=\"bitcount(item_id)\" dimens=\"top_level\"  />"),
            Pair.of("1-hour","<stat-item  title=\"每小时_各召回模型_平均下发质量分\" stat=\"avg(score,behavior_type == '1')\" dimens=\"recall_no\"  />"),
            Pair.of("1-hour","<stat-item  title=\"每小时_各省份_下发量top100\" stat=\"count(behavior_type == '1')\" dimens=\"province\" limit=\"top100\" />"),
            Pair.of("1-minute","<stat-item  title=\"每分钟_各省份_uv\" stat=\"bitcount(imei)\" dimens=\"province\"  />"),
            Pair.of("1-day","<stat-item  title=\"每天_uv量\" stat=\"bitcount(imei)\"  />"),
            Pair.of("5-minute","<stat-item  title=\"每5分钟_各app版本_请求量top50\" stat=\"bitcount(request_id)\" dimens=\"app_version\" limit=\"top50\" />"),
            Pair.of("1-day","<stat-item  title=\"每天_点击率\" stat=\"count(behavior_type == '2')/count(behavior_type == '1')\" />"),
            Pair.of("5-minute","<stat-item  title=\"每5分钟_各分类_点击率top10\" stat=\"count(behavior_type == '2')/count(behavior_type == '1')\" dimens=\"top_level\" limit=\"top30\" />")
    );

    public static void createStats(Connection connection) throws Exception {
        List<Stat> statList = new ArrayList<>();
        for(Pair<String,String> pair : templateList){
            String timeParam = pair.getLeft();
            String template = pair.getRight();
            TemplateContext templateContext = new TemplateContext(template,timeParam,exampleContext.getGroup().getColumns());
            ServiceResult<TemplateEntity> serviceResult = TemplateParser.parseConfig(templateContext);
            TemplateEntity templateEntity = serviceResult.getData();
            Stat stat = new Stat();
            stat.setState(StatStateEnum.RUNNING);
            LocalDateTime localDateTime = LocalDateTime.now();
            stat.setRefreshTime(localDateTime);
            stat.setCreateTime(localDateTime);
            stat.setUpdateTime(localDateTime);
            stat.setRandomId(RandomID.id(32));
            stat.setMetaId(exampleContext.getMetaId());
            stat.setTitle(templateEntity.getTitle());
            stat.setDesc(templateEntity.getTitle());
            stat.setTemplate(template);
            stat.setProjectId(exampleContext.getProject().getId());
            stat.setGroupId(exampleContext.getGroup().getId());
            stat.setTimeparam(timeParam);
            stat.setExpired(1209600L);
            statList.add(stat);
        }
        List<Integer> ids = StatHandler.createStats(connection,statList);
        LocalDateTime localDateTime = LocalDateTime.now();
        for(int i=0;i<ids.size();i++){
            Integer id = ids.get(i);
            Stat stat = statList.get(i);
            Role manageRole = new Role();
            manageRole.setPid(exampleContext.getGroupManageRoleId());
            manageRole.setResourceId(id);
            manageRole.setRoleType(RoleTypeEnum.STAT_MANAGE_PERMISSION);
            manageRole.setCreateTime(localDateTime);
            manageRole.setUpdateTime(localDateTime);
            manageRole.setDesc(RoleTypeEnum.STAT_MANAGE_PERMISSION+"("+ stat.getTitle()+")");
            int manageRoleId = RoleHandler.createRole(connection,manageRole);
            exampleContext.setGroupManageRoleId(manageRoleId);

            Role accessRole = new Role();
            accessRole.setPid(exampleContext.getGroupAccessRoleId());
            accessRole.setResourceId(id);
            accessRole.setRoleType(RoleTypeEnum.STAT_ACCESS_PERMISSION);
            accessRole.setCreateTime(localDateTime);
            accessRole.setUpdateTime(localDateTime);
            accessRole.setDesc(RoleTypeEnum.STAT_ACCESS_PERMISSION+"("+stat.getTitle()+")");
            int accessRoleId = RoleHandler.createRole(connection,accessRole);
            exampleContext.setGroupAccessRoleId(accessRoleId);

            Permission managePermission = new Permission();
            managePermission.setRoleId(manageRoleId);
            managePermission.setOwnerId(exampleContext.getAdminId());
            managePermission.setOwnerType(OwnerTypeEnum.USER);
            managePermission.setCreateTime(localDateTime);
            managePermission.setUpdateTime(localDateTime);
            PermissionHandler.createPermission(connection,managePermission);

            Permission accessPermission = new Permission();
            accessPermission.setRoleId(accessRoleId);
            accessPermission.setOwnerId(exampleContext.getAdminId());
            accessPermission.setOwnerType(OwnerTypeEnum.USER);
            accessPermission.setCreateTime(localDateTime);
            accessPermission.setUpdateTime(localDateTime);
            PermissionHandler.createPermission(connection,accessPermission);
            logger.info("create statistic example completed,statId:{}",id);
        }
    }
}
