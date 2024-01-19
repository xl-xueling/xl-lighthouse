import React, {useEffect, useRef, useState} from 'react';
import {Button, Card, Grid, Input, Notification, Popconfirm, Space, Spin, Tree} from '@arco-design/web-react';
import {
    IconDragDotVertical,
    IconFile,
    IconFolder, IconMindMapping,
    IconMinus,
    IconPen,
    IconPlus, IconRefresh, IconSearch,
    IconStorage,
    IconTag,
    IconTags
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import {ResultData} from "@/types/insights-common";
import {
    requestCreate,
    requestDelete,
    requestUpdateById
} from "@/api/department";
import {getRandomString, getTextBlenLength, stringifyObj, validateWithRegex} from "@/utils/util";
import {TEXT_BASE_PATTERN_2} from "@/utils/constants";
import {LuLayers} from "react-icons/lu";
import {getParentKey} from "@/pages/department/common";
import {RxCube} from "react-icons/rx";
import styles from "@/pages/metricset/structure/style/index.module.less";
import {MdOutlineDragIndicator} from "react-icons/md";
const { Row, Col } = Grid;
export default function StructurePanel({structure,menuCallback}) {
    const t = useLocale(locale);
    const [loading, setLoading] = useState(false);
    const [treeData, setTreeData] = useState([]);
    const treeRef = useRef<Tree>(null);
    const [selectedKeys, setSelectedKeys] = useState([]);
    const [checkedKeys, setCheckedKeys] = useState([]);
    const [expandedKeys, setExpandedKeys] = useState([]);

    const loadData = () => {
        console.log("structure is：" + JSON.stringify(structure));
        setTreeData(structure);
    }

    useEffect(() => {
        loadData();
    }, []);


    async function addNode(pid, title) {
        setLoading(true);
        let id = "-1";
        try {
            await requestCreate({'pid': pid, 'name': title}).then((response: ResultData) => {
                const {code, message, data} = response;
                if (code === '0') {
                    id = data;
                    sessionStorage.removeItem('cache_all_department');
                } else {
                    Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                }
            });
        } catch (error) {
            console.log(error);
            Notification.error({style: { width: 420 }, title: 'Error', content: t['system.error']});
        } finally {
            setLoading(false);
        }
        return id;
    }

    async function updateNode(id,pid,title) {
        setLoading(true);
        let result = "-1";
        try {
            await requestUpdateById({'id': id, 'pid':pid,'name': title}).then((response: ResultData) => {
                const {code, message, data} = response;
                if (code === '0') {
                    result = code;
                    sessionStorage.removeItem('cache_all_department');
                } else {
                    Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                }
            });
        } catch (error) {
            console.log(error);
            Notification.error({style: { width: 420 }, title: 'Error', content: t['system.error']});
        } finally {
            setLoading(false);
        }
        return result;
    }


    async function deleteNode(id) {
        setLoading(true);
        let result = "-1";
        try {
            await requestDelete({id}).then((response: ResultData) => {
                const {code, message, data} = response;
                if (code === '0') {
                    result = code;
                    sessionStorage.removeItem('cache_all_department');
                } else {
                    Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                }
            });
        } catch (error) {
            console.log(error);
            Notification.error({style: { width: 420 }, title: 'Error', content: t['system.error']});
        } finally {
            setLoading(false);
        }
        return result;
    }

    const getIcon= (type,level) => {
        if(type == 'stat'){
            return <IconTag style={{marginRight:'8px'}}/>
        }else if(level == 0){
            return <LuLayers style={{marginRight:'8px'}}/>
        }else if(level == 1){
            return <RxCube style={{marginRight:'8px'}}/>
        } else if(level == 2){
            return <IconMindMapping  style={{marginRight:'8px'}}/>
        } else if(level == 3){
            return <IconFile />
        }
    }

    const getIconByLevel = (level) => {
        if(level == 0){
            return <LuLayers style={{marginRight:'8px'}}/>
        }else if(level == 1){
            return <RxCube style={{marginRight:'8px'}}/>
        } else{
            return <IconMindMapping  style={{marginRight:'8px'}}/>
        }
    }

    const generatorTreeNodes = (list,parentKey = "0",level=0) => {
        return list.map((item) => {
            const { children, key, ...ret} = item;
            item.id = key;
            item.pid = parentKey;
            item.parentKey = parentKey;
            return (
                <Tree.Node
                        draggable={item.type == 'stat'}
                        icon={<span>{getIcon(item.type,level)}{item.type == 'stat'?<IconDragDotVertical style={{marginRight:'10px'}} />:null}</span>}
                        key={item.key}
                        title={item.label}
                     {...ret} dataRef={item}>
                    {children ? generatorTreeNodes(item.children,item.key,++level) : null}
                </Tree.Node>
            );
        });
    };


    function deleteNodeByKey(dataArray,inputValue) {
        const params = dataArray;
        const loop = (data) => {
            data.map((item,index) => {
                if(item.id === inputValue) {
                    data.splice(index,1);
                }
                if (item.children) {
                    loop(item.children);
                }
            });
        }
        loop(params);
        return params;
    }

    return (
        <div style={{width:'100%',minHeight:'450px',maxHeight:'1000px',overflow:'auto'}}>
            <Tree
                className={'disable-select'}
                ref={treeRef}
                draggable={true}
                multiple={false}
                checkedKeys={checkedKeys}
                selectedKeys={selectedKeys}
                expandedKeys={expandedKeys}
                onSelect={(keys, extra) => {
                    setSelectedKeys(keys);
                    if (extra.e.isTrusted && selectedKeys[0] === keys[0] && [...expandedKeys].find(item => item === keys[0])) {
                        const newArr = [...expandedKeys].filter(item => item !== keys[0]);
                        setExpandedKeys([...newArr]);
                    } else {
                        const newArr = [...expandedKeys].filter(item => item !== keys[0]);
                        setExpandedKeys([...newArr, ...keys]);
                    }
                    const key = keys[0];
                    console.log("key is:" + key)
                    const array = key.split("_");
                    if(array[0] == 'stat'){
                        console.log("click stat,key is:" + array[0] + ",v:" + array[1]);
                        menuCallback("clickStatMenu",array[1]);
                    }
                }}
                onCheck={(keys, extra) => {
                    setCheckedKeys(keys);
                }}
                onExpand={(keys, extra) => {
                    setExpandedKeys(keys);
                }}
                onDrop={async ({dragNode, dropNode, dropPosition}) => {
                    let destPid;
                    if(dropPosition === 0){
                        destPid = treeRef.current.getCacheNode([dropNode.props._key])[0].props.dataRef.id;
                    }else{
                        destPid = treeRef.current.getCacheNode([dropNode.props._key])[0].props.dataRef.pid;
                    }
                    const destRef = treeRef.current.getCacheNode([destPid])[0].props.dataRef;
                    if(destRef.type == 'stat'){
                        Notification.warning({style: { width: 420 }, title: 'Warning', content: '不支持移动元素到指标节点下'});
                        return;
                    }
                    const loop = (data, key, callback) => {
                        data.some((item, index, arr) => {
                            if (String(item.id) === String(key)) {
                                callback(item, index, arr);
                                return true;
                            }
                            if (item.children) {
                                return loop(item.children, key, callback);
                            }
                        });
                    };

                    const data = [...treeData];
                    let dragItem;
                    loop(data, dragNode.props._key, (item, index, arr) => {
                        arr.splice(index, 1);
                        dragItem = item;
                        dragItem.className = 'tree-node-dropover';
                    });

                    if (dropPosition === 0) {
                        loop(data, dropNode.props._key, (item, index, arr) => {
                            item.children = item.children || [];
                            item.children.push(dragItem);
                        });
                    } else {
                        loop(data, dropNode.props._key, (item, index, arr) => {
                            arr.splice(dropPosition < 0 ? index : index + 1, 0, dragItem);
                        });
                    }
                    setTreeData([...data]);
                    setTimeout(() => {
                        dragItem.className = '';
                        setTreeData([...data]);
                    }, 1000);
                }}
                renderExtra={(node) => {
                    return (
                        <div>
                            <IconPlus
                                style={{
                                    position: 'absolute',
                                    right: 41,
                                    fontSize: 13,
                                    top: 10,
                                    color: 'rgb(132 160 224)',
                                }}
                                onClick={async (e) => {
                                    const titleNode = e.currentTarget.parentElement.parentElement.querySelector(".arco-tree-node-title");
                                    const event = new Event('click', {
                                        bubbles: true,
                                        cancelable: true,
                                    });
                                    titleNode.dispatchEvent(event);
                                    const dataChildren = node.dataRef.children || [];
                                    if(node._level >= 2){
                                        Notification.warning({style: { width: 420 }, title: 'Warning', content: '超出最大层级限制！'});
                                        return;
                                    }
                                    const nodeTitle = "New Node_" + getRandomString(8);
                                    const currentId = getRandomString();
                                    if(currentId == "-1"){
                                        return;
                                    }
                                    dataChildren.push({
                                        key: currentId,
                                        parentKey:node.dataRef.id,
                                        name: nodeTitle,
                                        title:nodeTitle,
                                        id: currentId,
                                        pid: node.dataRef.id,
                                        icon:getIconByLevel(node._level + 1),
                                    });
                                    node.dataRef.children = dataChildren;
                                    setTreeData([...treeData]);
                                    const newArr = [...expandedKeys].filter(item => item !== node.dataRef.id);
                                    setExpandedKeys([...newArr, node.dataRef.id]);
                                }}
                            />
                            {node._level != 0  &&  (
                                <IconPen
                                    style={{
                                        display:`${node.dataRef.id != "0" ? 'block' : 'none'}`,
                                        position: 'absolute',
                                        right: 25,
                                        fontSize: 13,
                                        top: 10,
                                        color: 'rgb(132 160 224)',
                                    }}
                                    onClick={(e) => {
                                        const titleNode = e.currentTarget.parentElement.parentElement.querySelector(".arco-tree-node-title");
                                        const clickEvent = new Event('click', {
                                            bubbles: true,
                                            cancelable: true
                                        });
                                        titleNode.dispatchEvent(clickEvent);
                                        const originTitle = node.title;
                                        node.dataRef.title = <Input type={"text"} autoFocus={true}
                                                                    style={{
                                                                        width: 200,
                                                                        height: 19,
                                                                        paddingLeft:3,
                                                                        borderColor: 'rgb(132 160 224)',
                                                                        backgroundColor: "var(--color-fill-1)"
                                                                    }}
                                                                    defaultValue={originTitle + ""}
                                                                    onBlur={async (ie) => {
                                                                        const len = getTextBlenLength(ie.target.value);
                                                                        if (len < 3 || len > 30) {
                                                                            Notification.warning({style: { width: 420 }, title: 'Warning', content: t['department.manage.invalidLength']});
                                                                            node.dataRef.title = originTitle;
                                                                        } else if(!validateWithRegex(TEXT_BASE_PATTERN_2,ie.target.value)) {
                                                                            Notification.warning({style: { width: 420 }, title: 'Warning', content: t['department.manage.hasInvalidChars']});
                                                                            node.dataRef.title = originTitle;
                                                                        } else {
                                                                            const newTitle = ie.target.value;
                                                                            if(newTitle.length  > 0 && newTitle != originTitle){
                                                                                // const result = await updateNode(node.dataRef.id,node.dataRef.pid, newTitle);
                                                                                const result = '0';
                                                                                if(result == '0'){
                                                                                    node.dataRef.title = newTitle;
                                                                                }else{
                                                                                    node.dataRef.title = originTitle;
                                                                                }
                                                                            }else{
                                                                                node.dataRef.title = originTitle;
                                                                            }
                                                                        }
                                                                        setTreeData([...treeData]);
                                                                    }}
                                        />;
                                        setTreeData([...treeData]);
                                        const newArr = [...expandedKeys].filter(item => item !== node.dataRef.id);
                                        setExpandedKeys([...newArr, node.dataRef.id]);
                                    }}
                                />
                            )}
                            {node._level != 0 && (
                                <Popconfirm
                                    focusLock
                                    title='Confirm'
                                    content= {t['department.manage.deletePrompt']}
                                    onOk={async () => {
                                        const dataChildren = node.dataRef.children || [];
                                        if (dataChildren.length > 0) {
                                            Notification.warning({style: { width: 420 }, title: 'Warning', content: t['department.manage.deleteHasChild']});
                                        } else {
                                            // const result = await deleteNode(node.dataRef.id);
                                            // if (result == "-1") {
                                            //     return;
                                            // }
                                            const w = deleteNodeByKey([...treeData], node.dataRef.id)
                                            setTreeData([...w]);
                                        }
                                    }}
                                >
                                    <IconMinus
                                        style={{
                                            display:`${node.dataRef.id != "0" ? 'block' : 'none'}`,
                                            position: 'absolute',
                                            right: 8,
                                            fontSize: 13,
                                            top: 10,
                                            color: 'rgb(132 160 224)',
                                        }}
                                    />
                                </Popconfirm>
                            )}
                        </div>
                    );
                }}
            >
                {generatorTreeNodes(treeData)}
            </Tree>
            {/*<Grid.Row justify="end" style={{marginTop:'50px'}}>*/}
            {/*    <Grid.Col span={8}>*/}
            {/*        <Space className={styles.right} size={16} direction="horizontal">*/}
            {/*            <Button size={"small"} type="primary">确认</Button>*/}
            {/*            <Button size={"small"}>取消</Button>*/}
            {/*        </Space>*/}
            {/*    </Grid.Col>*/}
            {/*</Grid.Row>*/}
        </div>
  );

}
