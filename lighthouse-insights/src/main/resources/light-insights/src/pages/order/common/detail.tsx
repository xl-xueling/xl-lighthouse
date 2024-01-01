import React, {useEffect, useMemo, useState} from 'react';
import {Button, Grid, Input, Steps, Table, Typography} from "@arco-design/web-react";
import UserGroup from "@/pages/user/common/groups";
import useLocale from "@/utils/useLocale";
import locale from "./locale/index";
import {Order} from "@/types/insights-web";
import {ApproveStateEnum, OrderStateEnum, RoleTypeEnum} from "@/types/insights-common";
import {BiListUl} from "react-icons/bi";
import {getOrderColumns, getOrderDetailColumns, getUserApproveColumns} from "@/pages/order/common/constants";
import {getRandomString} from "@/utils/util";

const { Text } = Typography;

export default function OrderDetail({orderInfo}:{orderInfo:Order}) {

    const t = useLocale(locale);
    const [listData, setListData] = useState([]);
    const [userListData, setUserListData] = useState([]);
    const [orderDetailData, setOrderDetailData] = useState([]);
    const [showOrderDetail,setShowOrderDetail] = useState(false);

    const orderColumns = useMemo(() => getOrderColumns(t), [t]);
    const userApproveColumns = useMemo(() => getUserApproveColumns(t), [t]);
    const orderDetailColumns = useMemo(() => getOrderDetailColumns(t,orderInfo), [t]);

    const toggleShowOrderDetail = () => {
        setShowOrderDetail(!showOrderDetail);
    }

    const generateOrderSteps = () => {
        const startNode = (
            <Steps.Step key={getRandomString(32)} title='Start' />
        );
        const steps = orderInfo.orderDetails?.map(z => {
            const roleId = z.roleId;
            const admins = orderInfo.adminsMap[roleId];
            const roleType = z.roleType;
            let title = "";
            if(roleType == RoleTypeEnum.OPT_MANAGE_PERMISSION){
                title = t['detailModal.detail.columns.roleType.operateManager'];
            }else if(roleType == RoleTypeEnum.FULL_MANAGE_PERMISSION){
                title = t['detailModal.detail.columns.roleType.systemManager'];
            }else if(roleType == RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION){
                title = t['detailModal.detail.columns.roleType.departmentManager'];
            }else if(roleType == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
                title = t['detailModal.detail.columns.roleType.projectManager'];
            }else if(roleType == RoleTypeEnum.METRIC_MANAGE_PERMISSION){
                title = t['detailModal.detail.columns.roleType.metricManager'];
            }
            if(z.state == ApproveStateEnum.Wait){
                return <Steps.Step status={'wait'} key={z.id} title={title} description={
                    <div>
                        <UserGroup users={admins}/>
                    </div>
                }/>;
            }else if(z.state == ApproveStateEnum.Pending){
                return <Steps.Step status={'process'} key={z.id} title={title} description={
                    <div>
                        <UserGroup users={admins}/>
                    </div>
                }/>;
            }else if(z.state == ApproveStateEnum.Approved){
                console.log("----z.user is:" + JSON.stringify(z));
                return <Steps.Step status={'finish'} key={z.id} title={title} description={
                    <div>
                        <UserGroup users={[z.user]} />
                    </div>
                }/>;
            }else if(z.state == ApproveStateEnum.Rejected){
                return <Steps.Step status={'error'} key={z.id} title={title} description={
                    <div>
                        <UserGroup users={[z.user]} />
                    </div>
                }/>;
            }else if(z.state == ApproveStateEnum.Retracted){
                return <Steps.Step status={'finish'} key={z.id} title={title} description={
                    <div>
                        <UserGroup users={admins}/>
                    </div>
                }/>;
            }else if(z.state == ApproveStateEnum.Suspend){
                return <Steps.Step status={'wait'} key={z.id} title={title} description={
                    <div>
                        <UserGroup users={admins}/>
                    </div>
                }/>;
            }
        });
        const stopNode = orderInfo.state == OrderStateEnum.Approved ?
            <Steps.Step key={getRandomString(32)} status={'finish'} title='End' />
            :  <Steps.Step key={getRandomString(32)} status={'wait'} title='End' />;
        return [startNode,...steps,stopNode];
    }

    const getOrderStatus = ():string => {
        if(orderInfo.state == OrderStateEnum.Processing){
            return "process";
        }else if(orderInfo.state == OrderStateEnum.Approved){
            return "finish";
        }else if(orderInfo.state == OrderStateEnum.Rejected){
            return "error";
        }else{
            return "finish";
        }
    }

    useEffect(() => {
        console.log("orderInfo:" + JSON.stringify(orderInfo));
        setListData([orderInfo]);
        setUserListData([orderInfo.user])
        setOrderDetailData(orderInfo.orderDetails)
    },[])

    return (
      <div>
          <Typography.Title
              style={{ marginTop: 0, marginBottom: 16 }}
              heading={6}
          >
              {t['detailModal.label.order.info']}
          </Typography.Title>
          <Table size={"small"} rowKey="id" pagination={false} columns={orderColumns} data={listData} />

          <Typography.Title
              style={{ marginTop: 30 }}
              heading={6}
          >
              {t['detailModal.label.user.info']}
          </Typography.Title>
          <Table size={"small"} rowKey="id" pagination={false} columns={userApproveColumns} data={userListData} />

          <Typography.Title
              style={{ marginTop: 30 }}
              heading={6}
          >
              {t['detailModal.label.reason']}
          </Typography.Title>
          <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}/>
          <Grid.Row style={{ marginTop:'30px' }}>
              <Grid.Col span={16}>
                  <Typography.Title
                      heading={6}
                  >
                      {t['detailModal.label.process.info']}
                  </Typography.Title>
              </Grid.Col>
              <Grid.Col span={8} style={{ textAlign: 'right' }}>
                  <Button type={"secondary"} size={"mini"} icon={<BiListUl/>} onClick={toggleShowOrderDetail}/>
              </Grid.Col>
          </Grid.Row>
          <Steps  size={"small"}
                 current={4} style={{maxWidth: 780, marginBottom: 20,marginTop:10}}>

              {generateOrderSteps()}
          </Steps>
          {showOrderDetail && <Table size={"small"} rowKey="id" pagination={false} columns={orderDetailColumns} data={orderDetailData} />}
      </div>
    );
}