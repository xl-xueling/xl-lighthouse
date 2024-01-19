import React, {useCallback, useEffect, useMemo, useRef, useState} from 'react';
import {Descriptions, Form, Input, Message, Modal, Typography} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {requestUpdateById} from "@/api/project";
import {Order, Project} from "@/types/insights-web";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import UserGroup from "@/pages/user/common/groups";
import DepartmentLabel from "@/pages/department/common/depart";

export default function ProjectApplyModal({projectInfo,onClose}) {

    const t = useLocale(locale);
    const [loading, setLoading] = useState(false);
    const formRef = useRef(null);

    async function handlerSubmit() {
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        console.log("handler submit,values:" + JSON.stringify(values));
        console.log("projectInfo:" + JSON.stringify(projectInfo));
        const order:Order = {
            orderType:1,
            // desc:values.desc,
            extendConfig:{
                projectId:projectInfo.id,
            }
        }
        console.log("order is:" + JSON.stringify(order));
        // requestCreateApply(order).then((result) => {
        //     console.log("result:" + JSON.stringify(result));
        //     if(result.code === '0'){
        //         Message.success(t['projectCreate.form.submit.success']);
        //         // setTimeout(() => {
        //         //     window.location.href = "/project/list";
        //         // },3000)
        //     }else{
        //         Message.error(result.message || t['system.error']);
        //     }
        // }).catch((error) => {
        //     console.log(error);
        //     Message.error(t['system.error'])
        // }).finally(() => {
        //     setLoading(false);
        // })
    }


    const data = [
        {
            label: t['projectApply.column.title'],
            value: projectInfo?.title,
        },
        {
            label: t['projectApply.column.department'],
            value: <DepartmentLabel departmentId={projectInfo?.departmentId}/> ,
        },
        {
            label: t['projectApply.column.admins'],
            value: <UserGroup users={projectInfo?.admins} />,
        },
        {
            label: t['projectApply.column.description'],
            value: projectInfo?.desc,
        },
    ];

    return (
        <Modal
            title= {t['projectApply.modal.title']}
            style={{ width:'750px',top:'20px' }}
            visible={true}
            onOk={handlerSubmit}
            onCancel={onClose}>
            <Typography.Title
                style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
            >
                {t['projectApply.projectInfo']}
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
                    {t['projectApply.reason']}
                </Typography.Title>
                <Form.Item  field="desc" rules={[
                    {required: true ,message:t['projectCreate.form.description.errMsg'],validateTrigger : ['onSubmit']}
                ]}>
                    <Input.TextArea placeholder='Please enter the reason.' style={{ minHeight: 64}} maxLength={150} showWordLimit={true}/>
                </Form.Item>
            </Form>
        </Modal>
    );
}