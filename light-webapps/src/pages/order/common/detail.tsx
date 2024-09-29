import React, {useEffect, useMemo, useState} from 'react';
import {Button, Descriptions, Form, Grid, Input, Steps, Table, Typography} from "@arco-design/web-react";
import UserGroup from "@/pages/user/common/groups";
import useLocale from "@/utils/useLocale";
import locale from "./locale/index";
import {Order} from "@/types/insights-web";
import {ApproveStateEnum, OrderStateEnum, OrderTypeEnum} from "@/types/insights-common";
import {BiListUl} from "react-icons/bi";
import {
    getLimitingSettingsDescription,
    getOrderColumns,
    getOrderDetailColumns,
    getProjectAccessDescription,
    getStatAccessDescription,
    getUserApproveDescription,
    getViewAccessDescription
} from "@/pages/order/common/constants";
import {formatTimeStampBackUp, getRandomString} from "@/utils/util";
import {
    getOrderApproveRoleTypeDescription,
    getOrderDescription,
    getOrderStateDescription,
    getOrderTypeDescription
} from "@/pages/common/desc/base";

const { Text } = Typography;

export default function OrderDetail({orderInfo}:{orderInfo:Order}) {

    const t = useLocale(locale);
    const [listData, setListData] = useState([]);
    const [userListData, setUserListData] = useState([]);
    const [orderDetailData, setOrderDetailData] = useState([]);
    const [showOrderDetail,setShowOrderDetail] = useState(false);

    const orderColumns = useMemo(() => getOrderColumns(t), [t]);
    const userApproveDescription = useMemo(() => getUserApproveDescription(t,orderInfo), [t]);
    const statAccessDescription = useMemo(() => getStatAccessDescription(t,orderInfo), [t]);

    const projectAccessDescription = useMemo(() => getProjectAccessDescription(t,orderInfo), [t]);
    const viewAccessDescription = useMemo(() => getViewAccessDescription(t,orderInfo), [t]);
    const limitingSettingsDescription = useMemo(() => getLimitingSettingsDescription(t,orderInfo), [t]);
    const orderDetailColumns = useMemo(() => getOrderDetailColumns(t,orderInfo), [t,orderInfo]);

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
                <Descriptions border data={userApproveDescription}/>
            )
        }else if(orderInfo.orderType == OrderTypeEnum.PROJECT_ACCESS && orderInfo.extend){
            return (
                <Descriptions border data={projectAccessDescription}/>
            )
        }else if(orderInfo.orderType == OrderTypeEnum.STAT_ACCESS && orderInfo.extend){
            return (
                <Descriptions border data={statAccessDescription}/>
            )
        }else if(orderInfo.orderType == OrderTypeEnum.VIEW_ACCESS && orderInfo.extend){
            return (
                <Descriptions border data={viewAccessDescription}/>
            )
        }else if(orderInfo.orderType == OrderTypeEnum.CALLER_PROJECT_ACCESS && orderInfo.extend){
            return (
                <Descriptions border data={projectAccessDescription}/>
            )
        }else if(orderInfo.orderType == OrderTypeEnum.CALLER_STAT_ACCESS && orderInfo.extend){
            return (
                <Descriptions border data={statAccessDescription}/>
            )
        }else if(orderInfo.orderType == OrderTypeEnum.CALLER_VIEW_ACCESS && orderInfo.extend){
            return (
                <Descriptions border data={viewAccessDescription}/>
            )
        }else if(orderInfo.orderType == OrderTypeEnum.LIMITING_SETTINGS && orderInfo.extend){
            return (
                <Descriptions border data={limitingSettingsDescription}/>
            )
        }else{
            return  <Typography.Text type="secondary">
                {t['detailModal.warning.relateElementDeleted']}
            </Typography.Text>
        }
    }

    const getExpiredDescription = (expired):String => {
        if(expired == null){
            return null;
        }else if(expired == 2592000){
            return t['basic.orderExpired.description.expired1'];
        }else if(expired == 7776000){
            return t['basic.orderExpired.description.expired2'];
        }else if(expired == 15552000){
            return t['basic.orderExpired.description.expired3'];
        }else if(expired == 31104000){
            return t['basic.orderExpired.description.expired4'];
        }
    }

    const orderData = [
        {
            label: t['detailModal.columns.id'],
            value: orderInfo?.id,
        },
        {
            label: t['detailModal.columns.user'],
            value: <UserGroup users={[orderInfo?.user]}/>,
        },
        {
            label: t['detailModal.columns.type'],
            value: getOrderTypeDescription(t,orderInfo?.orderType),
        },
        {
            label: t['detailModal.columns.createTime'],
            value: formatTimeStampBackUp(orderInfo?.createTime),
        },
        {
            label: t['detailModal.columns.updateTime'],
            value: formatTimeStampBackUp(orderInfo?.updateTime),
        },
        {
            label: t['detailModal.columns.state'],
            value: getOrderStateDescription(t,orderInfo?.state),
        },
        {
            label: t['detailModal.columns.desc'],
            value: <>
                {
                    getOrderDescription(t,orderInfo)}
                {
                    (orderInfo?.orderType == OrderTypeEnum.CALLER_PROJECT_ACCESS || orderInfo?.orderType == OrderTypeEnum.CALLER_STAT_ACCESS || orderInfo?.orderType == OrderTypeEnum.CALLER_VIEW_ACCESS) &&
                    orderInfo?.extend?.expired?<Text style={{color:'red'}} bold={true}> [{getExpiredDescription(orderInfo?.extend?.expired)}]</Text>:null}
            </>
        },
    ];

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
              <Descriptions border data={orderData} />
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
              <Grid.Row>
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