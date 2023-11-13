import React, {useEffect, useRef, useState} from 'react';
import {Button, Input, Message, Space, Spin, Tree} from '@arco-design/web-react';
import {IconFile, IconFolder, IconMinus, IconPen, IconPlus} from '@arco-design/web-react/icon';
import axios from 'axios';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {queryList} from "@/api/project";
import {queryAll as queryDepartAll} from "@/api/department";

export default function TreeEditPanel() {
  const t = useLocale(locale);
  const [messageList, setMessageList] = useState([]);
  const [departmentList, setDepartmentList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [visible ,setVisible] = useState<boolean>(true);

    useEffect(() => {
        fetchDepartmentData();
    }, []);

    function tranListToTreeData(list, rootValue) {
        const arr = []
        list.forEach(item => {
            if (item.pid === rootValue) {
                arr.push(item)
                const children = tranListToTreeData(list, item.id)
                if (children.length) {
                    item.children = children
                }
            }
        })
        return arr
    }

    async function fetchDepartmentData() {
        setLoading(true);
        try {
            const a:any = await queryDepartAll().then((res:any) => {
                const {code, msg, data} = res;
                if (code === '0' && data) {
                    const arr = tranListToTreeData([...data],"0");
                    setTreeData([...arr]);
                }else{
                     Message.error("System Error,fetch department data failed!")
                }
            });
        } catch (error) {
            console.error("error is:" + error);
        }finally {
            setLoading(false);
        }
    }

    const generatorTreeNodes = (treeData) => {
        return treeData.map((item) => {
            const { children, id, name} = item;
            return (
                <Tree.Node icon={children ? <IconFolder /> : <IconFile/> }
                           key={id} title={name} dataRef={item}>
                    {children ? generatorTreeNodes(item.children) : null}
                </Tree.Node>
            );
        });
    };

    function stringify(obj) {
        let cache = [];
        const str = JSON.stringify(obj, function(key, value) {
            if (typeof value === "object" && value !== null) {
                if (cache.indexOf(value) !== -1) {
                    // Circular reference found, discard key
                    return;
                }
                // Store value in our collection
                cache.push(value);
            }
            return value;
        });
        cache = null; // reset the cache
        return str;
    }


    function getStringLength(str){
        let len = 0;
        for(let i=0;i<str.length;i++){
            if(str.charAt(i).match(/[\u4e00-\u9fa5]/g) != null) len+=2;
            else len += 1;
        }
        return len;
    }

    function deleteNodeByKey(dataArray,inputValue) {
        const params = dataArray;
        const loop = (data) => {
            data.map((item,index) => {
                if(item.key === inputValue) {
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

  const [treeData, setTreeData] = useState([]);
  const editRef = useRef(null);
  const treeRef = useRef(null);
    const [selectedKeys, setSelectedKeys] = useState([]);
    const [checkedKeys, setCheckedKeys] = useState([]);
    const [expandedKeys, setExpandedKeys] = useState([]);

    return (
        <Spin loading={loading} size={20} delay={500}  style={{ display: 'block' }}>
      <div className={styles['chat-panel']} style={{ display:`${visible ? 'block' : 'none'}` }}>

        <Tree
            ref={treeRef}
            draggable={true}
            multiple={false}
            checkedKeys={checkedKeys}
            selectedKeys={selectedKeys}
            expandedKeys={expandedKeys}
            onSelect={(keys, extra) => {
                console.log("------select event:" + keys)
                setSelectedKeys(keys);
                if (selectedKeys[0] === keys[0] && [...expandedKeys].find(item => item === keys[0])) {
                    const newArr = [...expandedKeys].filter(item => item !== keys[0]);
                    setExpandedKeys([...newArr]);
                } else {
                    setExpandedKeys([...expandedKeys, ...keys]);
                }
            }}
            onCheck={(keys, extra) => {
                console.log(keys, extra);
                setCheckedKeys(keys);
            }}
            onExpand={(keys, extra) => {
                console.log(keys, extra);
                setExpandedKeys(keys);
            }}
            onDrop={({ dragNode, dropNode, dropPosition }) => {
                const loop = (data, key, callback) => {
                    data.some((item, index, arr) => {
                        if (item.key === key) {
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
                          onClick={(e) => {
                              const titleNode = e.currentTarget.parentElement.parentElement.querySelector(".arco-tree-node-title");
                              const event = new Event('click', {
                                  bubbles: true,
                                  cancelable: true
                              });
                              titleNode.dispatchEvent(event);
                              const dataChildren = node.dataRef.children || [];
                              const ss =  node._key + '-' + (dataChildren.length + 1);
                              dataChildren.push({
                                  title: 'new node_' + ss,
                                  key: node._key + '-' + (dataChildren.length + 1),
                              });
                              node.dataRef.children = dataChildren;
                              setTreeData([...treeData]);
                              setExpandedKeys([...expandedKeys, node.dataRef.key]);
                          }}
                      />
                      <IconPen
                          style={{
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
                              const originTitle = node.dataRef.title;
                              node.dataRef.title = <Input type={"text"} ref={editRef} autoFocus={true}
                                                            style={{
                                                                width: 120,
                                                                height: 19,
                                                                paddingLeft:3,
                                                                borderColor: 'rgb(132 160 224)',
                                                                backgroundColor: "white"
                                                            }}
                                                            defaultValue={node.title.valueOf() + ""}
                                                            onBlur={(ie) => {
                                                                const len = getStringLength(ie.target.value);
                                                                if(len > 20){
                                                                    Message.error("节点名称长度不能超过20！");
                                                                    node.dataRef.title = originTitle;
                                                                }else{
                                                                    console.log("title:" + ie.target.value + ",len is:" + len)
                                                                    node.dataRef.title = ie.target.value;
                                                                }
                                                                setTreeData([...treeData]);
                                                            }}
                                />;
                              setTreeData([...treeData]);
                              setExpandedKeys([...expandedKeys, node.dataRef.key]);
                          }}
                      />
                      <IconMinus
                          style={{
                              position: 'absolute',
                              right: 8,
                              fontSize: 13,
                              top: 10,
                              color: 'rgb(132 160 224)',
                          }}
                          onClick={(e) => {
                              const dataChildren = node.dataRef.children || [];
                              if(dataChildren.length > 0){
                                  Message.error('The node has child,delete child-node first!')
                              }else{
                                  const w = deleteNodeByKey([...treeData],node.dataRef.key)
                                  setTreeData([...w]);
                              }
                          }}
                      />
                  </div>
              );
            }}>
          {generatorTreeNodes(treeData)}
        </Tree>

            <div style={{ textAlign:"right",marginTop:30 }}>
                <Space size={6} direction="vertical" style={{ width:80 }}>
                    <Button
                        size={"small"}
                        href={"/"}
                        type='primary'
                        long
                        className={styles['login-form-register-btn']}>
                        {'确认修改'}
                    </Button>
                </Space>
            </div>
      </div>
        </Spin>
  );
}
