import React, {useEffect, useRef, useState} from 'react';
import {
    Button,
    Grid,
    Message,
    Modal,
    Popconfirm,
    Select,
    Space,
    Table,
    TableColumnProps,
    Tabs,
    Typography,
    Notification, Spin,
} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale/index";
import {getRandomString} from "@/utils/util";
import {
    requestClearDimensValue,
    requestDeleteDimensValue,
    requestQueryDimensList,
    requestQueryDimensValueList
} from "@/api/group";
import {Simulate} from "react-dom/test-utils";
import {useUpdateEffect} from "ahooks";

const Option = Select.Option;
const options = ['Beijing', 'Shanghai', 'Guangzhou', 'Disabled'];
const { Col, Row } = Grid;

export function DimensManageModal({groupInfo,onClose}){

    const t = useLocale(locale);
    const TabPane = Tabs.TabPane;
    const [loading,setLoading] = useState<boolean>(false);
    const [dimensList,setDimensList] = useState<Array<string>>([]);
    const [initialFlag,setInitialFlag] = useState<boolean>(false);
    const [currentDimens,setCurrentDimens] = useState<string>(null);
    const [dimensValueList,setDimensValueList] = useState<Array<any>>([]);
    const [reloadTime,setReloadTime] = useState<number>(Date.now());

    const fetchQueryDimensList = async () => {
        setLoading(true);
        await requestQueryDimensList({id:groupInfo?.id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setDimensList(data);
                if(data && data.length > 0){
                    setCurrentDimens(data[0])
                }
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setInitialFlag(true);
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const fetchDimensValueList = async () => {
        if(!currentDimens){
            return;
        }
        setLoading(true);
        const requestParam = {
            groupId:groupInfo?.id,
            dimens:currentDimens,
        };
        await requestQueryDimensValueList(requestParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                const a = data.map(z => {return {dimensValue:z}});
                setDimensValueList(a);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const handlerDeleteDimensValue = async (dimensValue) => {
        const requestParam = {
            groupId:groupInfo?.id,
            dimens:currentDimens,
            dimensValue:dimensValue,
        };
        await requestDeleteDimensValue([requestParam]).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setDimensValueList(dimensValueList.filter(x => x.dimensValue !== dimensValue));
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['dimensManage.tabs.label.singleValueDelete.operations.delete.success']});
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        })
    }

    const handlerClearDimensValue = async () => {
        await requestClearDimensValue({id:groupInfo?.id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setReloadTime(Date.now());
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['dimensManage.tabs.label.batchValueDelete.success']});
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        })
    }

    useUpdateEffect(() => {
        fetchDimensValueList().then();
    },[currentDimens,reloadTime])

    useEffect(() => {
        fetchQueryDimensList().then();
    },[reloadTime])

    const columns: TableColumnProps[] = [
        {
            title: t['dimensManage.label.dimensValue'],
            dataIndex: 'dimensValue',
        },
        {
            title: t['dimensManage.label.operations'],
            dataIndex: 'operations',
            headerCellStyle: { paddingLeft: '15px',width:'150px' },
            render: (value, record) => {
                let deleteButton;
                deleteButton = <Popconfirm key={getRandomString()} focusLock position={"tr"} title='Confirm' content={t['dimensManage.tabs.label.singleValueDelete.operations.delete.confirm']} onOk={function () { return handlerDeleteDimensValue(record.dimensValue); }}>
                            <Button type="text" size="mini">{t['dimensManage.tabs.label.singleValueDelete.operations.delete']}</Button>
                    </Popconfirm>
                return <Space key={getRandomString()} size={0} direction="horizontal">{[deleteButton]}</Space>;
            }
        },
    ];

    const changeCurrentDimens = (v) => {
        setCurrentDimens(v);
    }

    return (
        <Modal
            title= {t['dimensManage.modal.title']}
            alignCenter={false}
            style={{width:'1180px',maxWidth:'80%', top: '150px',minHeight:'400px' }}
            visible={true}
            onCancel={onClose}
            footer={null}>
            <Spin loading={loading} style={{ display: 'block'}}>
                <Tabs destroyOnHide={true} size={'small'} defaultActiveTab='1' tabPosition={'top'} type={'card-gutter'}>
                    <TabPane key='1' title={t['dimensManage.tabs.label.singleValueDelete']} style={{padding:'6px'}}>
                            {initialFlag && <Space size={10} direction={"vertical"} style={{width:'100%'}}>
                                <Row>
                                    <Col span={17}>
                                        <Select
                                            size={'mini'}
                                            placeholder='Select Dimension'
                                            style={{ width: 300 }}
                                            onChange={changeCurrentDimens}
                                            defaultValue={currentDimens}>
                                            {dimensList.map((option, index) => (
                                                <Option key={option} value={option}>
                                                    {option}
                                                </Option>
                                            ))}
                                        </Select>
                                    </Col>
                                </Row>
                                <Table rowKey={'dimensValue'} size={'mini'} columns={columns} data={dimensValueList} />
                            </Space>}
                    </TabPane>
                    <TabPane key='2' title={t['dimensManage.tabs.label.batchValueDelete']} style={{padding:'10px'}}>
                        <Popconfirm key={getRandomString()} focusLock position={"tr"} title='Confirm' content={t['dimensManage.tabs.label.batchValueDelete.confirm']} onOk={function () { return handlerClearDimensValue(); }}>
                            <Button size={'mini'}>{t['dimensManage.tabs.label.batchValueDelete.button']}</Button>
                        </Popconfirm>
                        <Typography.Text style={{fontSize:12}}>
                            {t['dimensManage.tabs.label.batchValueDelete.button.tips']}
                        </Typography.Text>
                    </TabPane>
                </Tabs>
            </Spin>
        </Modal>
    );
}