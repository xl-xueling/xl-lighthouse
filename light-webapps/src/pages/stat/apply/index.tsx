import React, {useCallback, useEffect, useMemo, useRef, useState} from 'react';
import {Descriptions, Form, Input, Message, Modal, Notification, Typography} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {requestUpdateById} from "@/api/project";
import {Order, Project} from "@/types/insights-web";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import UserGroup from "@/pages/user/common/groups";
import DepartmentLabel from "@/pages/department/common/depart";
import {OrderTypeEnum} from "@/types/insights-common";
import {requestCreateApply} from "@/api/order";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";

export default function StatApplyModal({statInfo,onClose}) {

    const t = useLocale(locale);
    const [loading, setLoading] = useState(false);
    const formRef = useRef(null);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);

    async function handlerSubmit() {
        await formRef.current.validate();
        setLoading(true);
        const values = formRef.current.getFieldsValue();
        const applyParam = {
            orderType:OrderTypeEnum.STAT_ACCESS,
            userId:userInfo?.id,
            reason:values?.reason,
            extendConfig:{
                statId:statInfo.id,
            }
        }
        requestCreateApply(applyParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['statApply.form.submit.success']});
                onClose();
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error'])
        }).finally(() => {
            setLoading(false);
        })
    }


    useEffect(() => {
        console.log("statInfo is:" + JSON.stringify(statInfo));
    },[])


    const data = [
        {
            label: t['statApply.column.title'],
            value: statInfo?.title,
        },
        {
            label: t['statApply.column.department'],
            value: <DepartmentLabel departmentId={statInfo?.departmentId}/> ,
        },
        {
            label: t['statApply.column.admins'],
            value: <UserGroup users={statInfo?.admins} />,
        },
        {
            label: t['statApply.column.description'],
            value: statInfo?.desc,
        },
    ];

    return (
        <Modal
            title= {t['statApply.modal.title']}
            style={{ width:'750px',top:'20px' }}
            visible={true}
            confirmLoading={loading}
            onOk={handlerSubmit}
            onCancel={onClose}>
            <Typography.Title
                style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
            >
                {t['statApply.statInfo']}
            </Typography.Title>
            <Descriptions
                colon={" :"}
                column={1}
                data={data}
                style={{ marginBottom: 20 }}
                labelStyle={{ paddingRight: 36 }}
            />
            <Form
                colon={":"}
                ref={formRef}
                layout={"vertical"}
            >
                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {t['statApply.reason']}
                </Typography.Title>
                <Form.Item  field="reason" rules={[
                    {required: true ,message:t['projectCreate.form.description.errMsg'],validateTrigger : ['onSubmit']}
                ]}>
                    <Input.TextArea placeholder='Please enter the reason.' style={{ minHeight: 64}} maxLength={150} showWordLimit={true}/>
                </Form.Item>
            </Form>
        </Modal>
    );
}