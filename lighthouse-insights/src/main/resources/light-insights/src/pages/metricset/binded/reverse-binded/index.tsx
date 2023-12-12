import {Form, Grid, Message, Modal, Spin, Tabs, TreeSelect} from '@arco-design/web-react';
import {IconStar} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import {GoGitMerge, GoStack} from "react-icons/go";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {requestBinded, requestPinList} from "@/api/metricset";
import {ArcoTreeNode, Department, MetricSet, MetricSetPagination, PrivilegeEnum} from "@/types/insights-web";
import locale from "./locale";
import {requestPrivilegeCheck} from "@/api/privilege";

const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;

export default function ReverseBindedPanel({projectId = 0,statId = 0,onClose}) {

    const t = useLocale(locale);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const [treeData, setTreeData] = useState([]);
    const [loading,setLoading] = useState(true);
    const [confirmLoading,setConfirmLoading] = useState(false);
    const formRef = useRef(null);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const pinMetricsInfo = useSelector((state: {pinMetricsInfo:Array<MetricSet>}) => state.pinMetricsInfo);

    function translate(item, level = 1) {
        if (!item) {
            return null;
        }
        const newNode:ArcoTreeNode = {"key":String(item.key),"title":item.title,"icon":(level == 0)?<GoGitMerge/>:<GoStack/>};
        if (level >= 2) {
            newNode.children = [];
        } else {
            if (item.children && item.children.length > 0) {
                item.children = item.children.map((child) =>
                    translate(child, level + 1)
                );
            }
        }
        return newNode;
    }

    async function handlerSubmit(){
        await formRef.current.validate();
        const bindItems = formRef.current.getFieldValue("bindItems");
        let metricId;
        let nodeId;
        if (bindItems.includes('-')) {
            const arr = bindItems.split('-');
            metricId = Number(arr[0]);
            nodeId = Number(arr[1]);
        } else {
            metricId = Number(bindItems);
            nodeId = 0;
        }
        setLoading(true);
        requestBinded({"projectIds":[projectId],"metricId":metricId,"nodeId":nodeId}).then((result) => {
            if(result.code === '0'){
                Message.success(t['reverseBinded.form.submit.success']);
                setTimeout(() => {
                    setLoading(false);
                    onClose();
                },1000)
            }else{
                Message.error(result.message || t['system.error']);
            }
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error'])
        })
    }

    const fetchPrivilegeInfo = async(ids) => {
        return new Promise<Record<number,PrivilegeEnum[]>>((resolve,reject) => {
            requestPrivilegeCheck({type:"metric",ids:ids}).then((response) => {
                console.log("data is:" + JSON.stringify(response.data));
                resolve(response.data);
            }).catch((error) => {
                reject(error);
            })
        })
    }

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        // const fetchPinMerticsData:Promise<Array<MetricSet>> = new Promise<Array<MetricSet>>((resolve,reject) => {
        //     const proc = async () => {
        //         const result = await requestPinList();
        //         resolve(result.data);
        //     }
        //     proc().then();
        // })
        //
        // const result = await Promise.all([fetchPinMerticsData]);
        //
        const ids = pinMetricsInfo.map(z => z.id);
        console.log("ids is:" + JSON.stringify(ids));
        Promise.all([fetchPrivilegeInfo(ids)])
            .then(([r1]) => {
                const treeData = pinMetricsInfo.reduce((result:ArcoTreeNode[],item:MetricSet) => {
                    const metricInfo = pinMetricsInfo.find(x => String(x.id) == String(item.id));
                    const combinedItem = { ...item, ...{"key":item.id,"permissions":r1[item.id]}};
                    const v = translate(item.structure)
                    result.push(v);
                    return result;
                },[]);
                setTreeData(treeData);
                setLoading(false);
            }).catch((error) => {
                console.log(error);
            })
    };

    useEffect(() => {
        fetchData().then();
    },[])

    return (
        <Modal
            title={t['reverseBinded.modal.title']}
            visible={true}
            style={{ width:'600px',minHeight:'250px'}}
            onOk={handlerSubmit}
            onCancel={onClose}
        >
            <Spin loading={loading} size={20} style={{ display: 'block' }}>
                <Form ref={formRef} wrapperCol={{ span: 24 }}>
                    <Form.Item field="bindItems"
                               rules={[
                                   { required: true, message: t['reverseBinded.form.bindeditem.errMsg'] , validateTrigger : ['onBlur']},
                               ]}>
                        <TreeSelect
                            renderFormat={(nodeProps, value) => {
                                return <div><IconStar />value</div>;
                            }}
                            placeholder={"Select MetricSet"}
                            multiple={false}
                            showSearch={true}
                            allowClear={true}
                            treeData={treeData}
                        />
                    </Form.Item>
                </Form>
            </Spin>
        </Modal>
    );
}