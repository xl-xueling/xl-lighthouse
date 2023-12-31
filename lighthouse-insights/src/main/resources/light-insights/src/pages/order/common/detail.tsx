import React, {useEffect, useState} from 'react';
import {Badge, Button, Grid, Input, Steps, Table, TableColumnProps, Typography} from "@arco-design/web-react";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStamp, getRandomString} from "@/utils/util";
import useLocale from "@/utils/useLocale";
import locale from "./locale/index";
import DepartmentLabel from "@/pages/department/common/depart";
import {Order} from "@/types/insights-web";
import {ApproveStateEnum, OrderStateEnum, RoleTypeEnum} from "@/types/insights-common";
import {IconDesktop} from "@arco-design/web-react/icon";
import { BiListUl } from "react-icons/bi";
import { CiViewList } from "react-icons/ci";

const { Text } = Typography;

export default function OrderDetail({orderInfo}:{orderInfo:Order}) {

    const t = useLocale(locale);
    const [listData, setListData] = useState([]);
    const [userListData, setUserListData] = useState([]);
    const [orderDetailData, setOrderDetailData] = useState([]);
    const [showOrderDetail,setShowOrderDetail] = useState(false);

    const toggleShowOrderDetail = () => {
        console.log("change order detail,showOrderDetail:" + showOrderDetail);
        setShowOrderDetail(!showOrderDetail);
    }

    const orderColumns: TableColumnProps[] = [
            {
                title: t['order.columns.id'],
                dataIndex: 'id',
                render: (value,record) =>
                    <Text>{value}</Text>
                ,
            },
            {
                title: t['order.columns.user'],
                dataIndex: 'user',
                render: (value,record) =>
                    <UserGroup users={[value]}/>
                ,
            },
            {
                title: t['order.columns.type'],
                dataIndex: 'orderType',
                render: (value) => {
                    if(value == '1'){
                        return <Text>{t['order.columns.type.project.access']}</Text>;
                    }else if(value == '2'){
                        return <Text>{t['order.columns.type.stat.access']}</Text>;
                    }else if(value == '3'){
                        return <Text>{t['order.columns.type.metrics.access']}</Text>;
                    }else if(value == '4'){
                        return <Text>{t['order.columns.type.adjust.limited.threshold']}</Text>;
                    }else if(value == '5'){
                        return <Text>{t['order.columns.type.stat.pend.approve']}</Text>;
                    }else if(value == '6'){
                        return <Text>{t['order.columns.type.user.pend.approve']}</Text>;
                    }
                },
            },
            {
                title: t['order.columns.createTime'],
                dataIndex: 'createTime',
                render: (value) => {return formatTimeStamp(value)},
            },
            {
                title: t['order.columns.state'],
                dataIndex: 'state',
                render: (value) => {
                    if(value === 0){
                        return <Badge status="processing" text={t['order.columns.state.pending']}/>;
                    }else if (value === 1) {
                        return <Badge status="success" text={t['order.columns.state.approved']}/>;
                    }else if(value === 2){
                        return <Badge status="error" text={t['order.columns.state.rejected']}/>;
                    }else if(value === 3){
                        return <Badge status="error" text={t['order.columns.state.retracted']}/>;
                    }
                },
            }
    ];

    const userApproveColumns: TableColumnProps[] = [
        {
            title: t['order.user.approve.columns.id'],
            dataIndex: 'id',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['order.user.approve.columns.username'],
            dataIndex: 'username',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['order.user.approve.columns.email'],
            dataIndex: 'email',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['order.user.approve.columns.department'],
            dataIndex: 'department',
            render: (value,record) =>
                <DepartmentLabel department={value}/>
            ,
        },
        {
            title: t['order.user.approve.columns.createTime'],
            dataIndex: 'createTime',
            render: (value) => {return formatTimeStamp(value)},
        },
        {
            title: t['order.user.approve.columns.state'],
            dataIndex: 'state',
            render: (value) => {
                if(value === 0){
                    return <Badge status="processing" text={t['order.user.approve.columns.state.pending']}/>;
                }else if (value === 1) {
                    return <Badge status="success" text={t['order.user.approve.columns.state.normal']}/>;
                }else if(value === 2){
                    return <Badge status="error" text={t['order.user.approve.columns.state.frozen']}/>;
                }else if(value === 3){
                    return <Badge status="error" text={t['order.user.approve.columns.state.deleted']}/>;
                }
            },
        }
    ];

    const orderDetailColumns: TableColumnProps[] = [
        {
            title: t['order.user.approve.columns.id'],
            dataIndex: 'id',
            cellStyle:{
                display:"none",
            },
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: '系统角色',
            dataIndex: 'roleType',
            render: (value,record) => {
                if(value === RoleTypeEnum.FULL_MANAGE_PERMISSION){
                    return "系统管理员";
                }else if(value == RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION){
                    return "部门管理员";
                }else if(value == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
                    return "工程管理员";
                }else if(value == RoleTypeEnum.OPT_MANAGE_PERMISSION){
                    return "运维管理员";
                }
            },
        },
        {
            title: '审批人',
            dataIndex: 'user',
            render: (value,record) =>
                <UserGroup users={orderInfo.adminsMap[record.roleId]}/>
            ,
        },
        {
            title: '审核状态',
            dataIndex: 'state',
            render: (value) => {
                if(value === 0){
                    return <Badge status="processing" text={t['order.columns.state.pending']}/>;
                }else if (value === 1) {
                    return <Badge status="success" text={t['order.columns.state.approved']}/>;
                }else if(value === 2){
                    return <Badge status="error" text={t['order.columns.state.rejected']}/>;
                }else if(value === 3){
                    return <Badge status="error" text={t['order.columns.state.retracted']}/>;
                }
            },
        },
        {
            title: '审核时间',
            dataIndex: 'approveTime',
            render: (value,record) => {
                if(value){
                    return <Text>{formatTimeStamp(value)}</Text>
                }
            }
            ,
        },
        {
            title: '审核批复',
            dataIndex: 'apply',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
    ]

    const generateOrderSteps = () => {
        return (
            orderInfo.orderDetails?.map(z => {
                const roleId = z.roleId;
                const admins = orderInfo.adminsMap[roleId];
                if(z.state == ApproveStateEnum.PENDING){
                    return <Steps.Step status={'process'} key={z.id} title='Pending' description={
                        <div>
                            <UserGroup users={admins}/>
                        </div>
                    }/>;
                }else if(z.state == ApproveStateEnum.APPROVED){
                    return <Steps.Step status={'finish'} key={z.id} title='Pending' />;
                }else if(z.state == ApproveStateEnum.REJECTED){
                    return <Steps.Step status={"error"} key={z.id} title='Pending' />;
                }
            })
        );
    }

    const getOrderStatus = ():string => {
        if(orderInfo.state == OrderStateEnum.PENDING){
            return "process";
        }else if(orderInfo.state == OrderStateEnum.APPROVED){
            return "finish";
        }else if(orderInfo.state == OrderStateEnum.REJECTED){
            return "error";
        }else{
            return "finish";
        }
    }

    useEffect(() => {
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
              工单信息
          </Typography.Title>
          <Table size={"small"} rowKey="id" pagination={false} columns={orderColumns} data={listData} />

          <Typography.Title
              style={{ marginTop: 30 }}
              heading={6}
          >
              用户信息
          </Typography.Title>
          <Table size={"small"} rowKey="id" pagination={false} columns={userApproveColumns} data={userListData} />

          <Typography.Title
              style={{ marginTop: 30 }}
              heading={6}
          >
              申请原因
          </Typography.Title>
          <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}/>
          <Grid.Row style={{ marginTop:'30px' }}>
              <Grid.Col span={16}>
                  <Typography.Title
                      heading={6}
                  >
                      流程信息
                  </Typography.Title>
              </Grid.Col>
              <Grid.Col span={8} style={{ textAlign: 'right' }}>
                  <Button type={"secondary"} size={"mini"} icon={<BiListUl/>} onClick={toggleShowOrderDetail}/>
              </Grid.Col>
          </Grid.Row>
          <Steps  size={"small"}
                 current={4} style={{maxWidth: 780, marginBottom: 20,marginTop:10}}>
              <Steps.Step title='Start' />
              {generateOrderSteps()}
              <Steps.Step status={"wait"} title='End' />
          </Steps>
          {showOrderDetail && <Table size={"small"} rowKey="id" pagination={false} columns={orderDetailColumns} data={orderDetailData} />}
      </div>
    );
}