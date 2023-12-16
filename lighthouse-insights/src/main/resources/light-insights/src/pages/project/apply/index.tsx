import React, {useCallback, useEffect, useMemo, useState} from 'react';
import {Descriptions, Form, Input, Modal, Typography} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/create/locale";

export default function ProjectApplyModal({onClose}) {

    const t = useLocale(locale);

    const data = [
        {
            label: 'Name',
            value: 'Socrates',
        },
        {
            label: 'Mobile',
            value: '123-1234-1234',
        },
        {
            label: 'Residence',
            value: 'Beijing',
        },
        {
            label: 'Hometown',
            value: 'Beijing',
        },
        {
            label: 'Address',
            value: 'Yingdu Building, Zhichun Road, Beijing',
        },
    ];

    return (
        <Modal
            title= '申请工程权限'
            style={{ width:'750px',top:'20px' }}
            visible={true}
            onCancel={onClose}>
            <Typography.Title
                style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
            >
                {'工程信息：'}
            </Typography.Title>
            <Descriptions
                column={1}
                data={data}
                style={{ marginBottom: 20 }}
                labelStyle={{ paddingRight: 36 }}
            />
            <Form
                colon={":"}
                layout={"vertical"}
            >
                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'申请原因：'}
                </Typography.Title>
                <Form.Item  field="desc" rules={[
                    {required: true ,message:t['projectCreate.form.description.errMsg'],validateTrigger : ['onSubmit']}
                ]}>
                    <Input.TextArea placeholder='Please enter description.' style={{ minHeight: 64}} maxLength={150} showWordLimit={true}/>
                </Form.Item>
            </Form>
        </Modal>
    );
}