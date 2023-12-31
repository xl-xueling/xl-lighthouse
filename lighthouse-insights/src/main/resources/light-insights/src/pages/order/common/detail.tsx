import React, {useEffect, useMemo, useState} from 'react';
import {Badge, Button, Grid, Input, Steps, Table, Typography} from "@arco-design/web-react";
import UserGroup from "@/pages/user/common/groups";
import useLocale from "@/utils/useLocale";
import locale from "./locale/index";
import {Order} from "@/types/insights-web";
import {ApproveStateEnum, OrderStateEnum} from "@/types/insights-common";
import { BiListUl } from "react-icons/bi";
import {getOrderColumns, getOrderDetailColumns, getUserApproveColumns} from "@/pages/order/common/constants";
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