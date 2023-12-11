import {Form, Grid, Message, Modal, Spin, Tabs, TreeSelect} from '@arco-design/web-react';
import {IconStar} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import {GoGitMerge, GoStack} from "react-icons/go";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {requestBinded, requestPinList} from "@/api/metricset";
import {ArcoTreeNode, MetricSet, MetricSetPagination, PrivilegeEnum} from "@/types/insights-web";
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
        const relationId = formRef.current.getFieldValue("relationId");
        let metricId;
        let nodeId;
        if (relationId.includes('-')) {
            const arr = relationId.split('-');
            metricId = Number(arr[0]);
            nodeId = Number(arr[1]);
        } else {
            metricId = Number(relationId);
            nodeId = 0;
        }
        requestBinded({"projectIds":[projectId],"metricId":metricId,"nodeId":nodeId}).then((result) => {
            if(result.code === '0'){
                Message.success(t['projectCreate.form.submit.success']);
                setTimeout(() => {
                    window.location.href = "/project/list";
                },3000)
            }else{
                Message.error(result.message || t['system.error']);
            }
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error'])
        }).finally(() => {
            setLoading(false);
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
        const fetchPinMerticsData:Promise<Array<MetricSet>> = new Promise<Array<MetricSet>>((resolve,reject) => {
            const proc = async () => {
                const result = await requestPinList();
                resolve(result.data);
            }
            proc().then();
        })

        const result = await Promise.all([fetchPinMerticsData]);
        const metricsInfo = result[0];
        const ids = metricsInfo.map(z => z.id);
        console.log("ids is:" + JSON.stringify(ids));
        Promise.all([fetchPrivilegeInfo(ids)])
            .then(([r1]) => {
                const treeData = metricsInfo.reduce((result:ArcoTreeNode[],item:MetricSet) => {
                    const metricInfo = metricsInfo.find(x => String(x.id) == String(item.id));
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
                    <Form.Item field="relationId" >
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