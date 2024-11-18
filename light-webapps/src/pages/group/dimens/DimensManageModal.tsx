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
import {requestQueryDimensList, requestQueryDimensValueList} from "@/api/group";
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

    const fetchQueryDimensList = async () => {
        setLoading(true);
        await requestQueryDimensList({id:groupInfo?.id}).then((response) => {
            console.log("data is:" + JSON.stringify(response));
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

    useUpdateEffect(() => {
        fetchDimensValueList().then();
    },[currentDimens])

    useEffect(() => {
        fetchQueryDimensList().then();
    },[])

    const columns: TableColumnProps[] = [
        {
            title: 'DimensValue',
            dataIndex: 'dimensValue',
        },
        {
            title: 'Operations',
            dataIndex: 'operations',
            headerCellStyle: { paddingLeft: '15px',width:'150px' },
            render: (value, record) => {
                let deleteButton;
                deleteButton = <Button key={getRandomString()}
                        type="text"
                        size="mini">
                    {'删除'}
                </Button>
                return  <Space key={getRandomString()} size={0} direction="horizontal">{[deleteButton]}</Space>;
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
                <Tabs size={'small'} defaultActiveTab='1' tabPosition={'top'} type={'card-gutter'}>
                    <TabPane key='1' title='键值清理' style={{padding:'6px'}}>
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
                    <TabPane key='2' title='批量删除' style={{padding:'10px'}}>
                        <Button size={'mini'}>全部删除</Button>
                        <Typography.Text style={{fontSize:12}}>
                            （将清除当前统计组所有维度筛选参数，随着原始消息的后续接入会重新累积维度数据，只清除筛选参数，原统计结果不受影响！）
                        </Typography.Text>
                    </TabPane>
                </Tabs>
            </Spin>
        </Modal>
    );
}