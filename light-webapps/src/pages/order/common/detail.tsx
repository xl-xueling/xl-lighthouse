import React, {useEffect, useMemo, useState} from 'react';
import {Button, Form, Grid, Input, Steps, Table, Typography} from "@arco-design/web-react";
import UserGroup from "@/pages/user/common/groups";
import useLocale from "@/utils/useLocale";
import locale from "./locale/index";
import {Order} from "@/types/insights-web";
import {ApproveStateEnum, OrderStateEnum, OrderTypeEnum} from "@/types/insights-common";
import {BiListUl} from "react-icons/bi";
import {
    getLimitingSettingsColumns,
    getOrderColumns,
    getOrderDetailColumns,
    getProjectAccessColumns, getStatAccessColumns,
    getUserApproveColumns
} from "@/pages/order/common/constants";
import {getRandomString} from "@/utils/util";
import {getOrderApproveRoleTypeDescription} from "@/pages/common/desc/base";

const { Text } = Typography;

export default function OrderDetail({orderInfo}:{orderInfo:Order}) {

    const t = useLocale(locale);
    const [listData, setListData] = useState([]);
    const [userListData, setUserListData] = useState([]);
    const [orderDetailData, setOrderDetailData] = useState([]);
    const [showOrderDetail,setShowOrderDetail] = useState(false);

    const orderColumns = useMemo(() => getOrderColumns(t), [t]);
    const userApproveColumns = useMemo(() => getUserApproveColumns(t), [t]);
    const projectAccessColumns = useMemo(() => getProjectAccessColumns(t), [t]);
    const statAccessColumns = useMemo(() => getStatAccessColumns(t), [t]);
    const orderDetailColumns = useMemo(() => getOrderDetailColumns(t,orderInfo), [t,orderInfo]);
    const limitingSettingsColumns = useMemo(() => getLimitingSettingsColumns(t), [t]);

    const toggleShowOrderDetail = () => {
        setShowOrderDetail(!showOrderDetail);
    }

    const generateOrderSteps = () => {
        const startNode = (
            <Steps.Step key={getRandomString(32)} title='Start' />
        );
        const steps = orderInfo?.orderDetails? orderInfo.orderDetails.map(z => {
            const roleId = z.roleId;
            const admins = orderInfo.adminsMap[roleId];
            const roleType = z.roleType;
            const title = getOrderApproveRoleTypeDescription(t,roleType);
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
        }):[];
        const stopNode = orderInfo?.state == OrderStateEnum.Approved ?
            <Steps.Step key={getRandomString(32)} status={'finish'} title='End' />
            :  <Steps.Step key={getRandomString(32)} status={'wait'} title='End' />;
        return [startNode,...steps,stopNode];
    }

    useEffect(() => {
        if(orderInfo){
            setListData([orderInfo]);
            setUserListData([orderInfo?.user])
            setOrderDetailData(orderInfo?.orderDetails)
        }
    },[orderInfo])

    const getRelatedInformation = () => {
        if(orderInfo.orderType == OrderTypeEnum.USER_PEND_APPROVE){
            return (
                <Table size={"small"} rowKey="id" pagination={false} columns={userApproveColumns} data={userListData} />
            )
        }else if(orderInfo.orderType == OrderTypeEnum.PROJECT_ACCESS && orderInfo.extend){
            return (
                <Table size={"small"} rowKey="id" pagination={false} columns={projectAccessColumns} data={[orderInfo.extend]} />
            )
        }else if(orderInfo.orderType == OrderTypeEnum.STAT_ACCESS && orderInfo.extend){
            return (
                <Table size={"small"} rowKey="id" pagination={false} columns={statAccessColumns} data={[orderInfo.extend]} />
            )
        }else if(orderInfo.orderType == OrderTypeEnum.LIMITING_SETTINGS && orderInfo.extend){
            return (
                <Table size={"small"} rowKey="id" pagination={false} columns={limitingSettingsColumns} data={[orderInfo.extend]} />
            )
        }else{
            return  <Typography.Text type="secondary">
                {t['detailModal.warning.relateElementDeleted']}
            </Typography.Text>
        }
    }

    return (
      <div>
          <Form
              labelCol={{ span: 0 }}
              wrapperCol={{ span: 24 }}
              initialValues={{
                  reason:orderInfo?.reason,
              }}
          >
              <Typography.Title
                  style={{ marginTop: 0, marginBottom: 16 }}
                  heading={6}
              >
                  {t['detailModal.label.order.info']}
              </Typography.Title>
              <Table size={"small"} rowKey="id" pagination={false} columns={orderColumns} data={listData} />

              <Typography.Title
                  style={{ marginTop: 30 }}
                  heading={6}>
                  {t['detailModal.label.related.information']}
              </Typography.Title>
              {
                  getRelatedInformation()
              }

              {
                  orderInfo?.orderType == OrderTypeEnum.USER_PEND_APPROVE ? null :
                  <>
                      <Typography.Title
                          style={{ marginTop: 30 }}
                          heading={6}
                      >
                          {t['detailModal.label.reason']}
                      </Typography.Title>
                      <Form.Item field="reason" >
                            <Input.TextArea maxLength={200} rows={3}  showWordLimit={true} disabled={true}/>
                      </Form.Item>
                  </>
              }
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
                     current={4} style={{marginBottom: 20,marginTop:10}}>

                  {generateOrderSteps()}
              </Steps>
          </Form>
          {showOrderDetail && <Table size={"small"} rowKey="id" pagination={false} columns={orderDetailColumns} data={orderDetailData} />}
      </div>
    );
}