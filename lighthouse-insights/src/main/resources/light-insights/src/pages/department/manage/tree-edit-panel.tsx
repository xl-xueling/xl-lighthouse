import React, {useEffect, useRef, useState} from 'react';
import {Button, Input, Message, Space, Spin, Tree} from '@arco-design/web-react';
import {IconFile, IconFolder, IconMinus, IconPen, IconPlus} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import { queryAll,add } from "@/api/department";

export default function TreeEditPanel() {
  const t = useLocale(locale);
  const [messageList, setMessageList] = useState([]);
  const [departmentList, setDepartmentList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [visible ,setVisible] = useState<boolean>(true);

    useEffect(() => {
        fetchAllData();
    }, []);

    function translateData(list, rootPid) {
        const nodeArr = []
        list.forEach(item => {
            if (item.pid === rootPid) {
                item.key = item.id;
                item.title = item.name;
                nodeArr.push(item)
                const children = translateData(list, item.id)
                if (children.length) {
                    item.children = children
                }
            }
        })
        return nodeArr;
    }

    async function fetchAllData() {
        setLoading(true);
        try {
            const a:any = await queryAll().then((res:any) => {
                const {code, msg, data} = res;
                if (code === '0' && data) {
                    const arr = translateData([...data],"0");
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

    async function addNode(pid, title) {
        setLoading(true);
        let id = "-1";
        try {
            await add({'pid': pid, 'title': title}).then((res: any) => {
                console.log("add node res:" + JSON.stringify(res));
                const {code, msg, data} = res;
                if (code === '0' && data) {
                    id = data.id;
                } else {
                    Message.error("System Error,add department node failed!")
                }
            });
        } catch (error) {
            console.error("error is:" + error);
        } finally {
            setLoading(false);
        }
        return id;
    }

    const generatorTreeNodes = (treeData) => {
        return treeData.map((item) => {
            const { children, key, ...ret} = item;
            return (
                <Tree.Node icon={children ? <IconFolder /> : <IconFile/> }
                           key={key} {...ret} dataRef={item}>
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
                    return;
                }
                cache.push(value);
            }
            return value;
        });
        cache = null;
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
  const treeRef = useRef<Tree>(null);
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
                setSelectedKeys(keys);
                if (extra.e.isTrusted && selectedKeys[0] === keys[0] && [...expandedKeys].find(item => item === keys[0])) {
                    const newArr = [...expandedKeys].filter(item => item !== keys[0]);
                    setExpandedKeys([...newArr]);
                } else {
                    const newArr = [...expandedKeys].filter(item => item !== keys[0]);
                    setExpandedKeys([...newArr, ...keys]);
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
                          onClick={async (e) => {
                              const titleNode = e.currentTarget.parentElement.parentElement.querySelector(".arco-tree-node-title");
                              const event = new Event('click', {
                                  bubbles: true,
                                  cancelable: true,
                              });
                              titleNode.dispatchEvent(event);
                              const dataChildren = node.dataRef.children || [];
                              const title = node._key + '-' + (dataChildren.length + 1);
                              const currentId = await addNode( node.dataRef.key, title);
                              dataChildren.push({
                                  title: "New Node_" + currentId,
                                  key: currentId,
                              });
                              node.dataRef.children = dataChildren;
                              setTreeData([...treeData]);
                              const newArr = [...expandedKeys].filter(item => item !== node.dataRef.key);
                              setExpandedKeys([...newArr, node.dataRef.key]);
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
                                                                backgroundColor: "var(--color-fill-1)"
                                                            }}
                                                            defaultValue={node.title.valueOf() + ""}
                                                            onBlur={(ie) => {
                                                                const len = getStringLength(ie.target.value);
                                                                if(len > 20){
                                                                    Message.error("节点名称长度不能超过20！");
                                                                    node.dataRef.title = originTitle;
                                                                }else{
                                                                    node.dataRef.title = ie.target.value;
                                                                }
                                                                setTreeData([...treeData]);
                                                            }}
                                />;
                              setTreeData([...treeData]);
                              const newArr = [...expandedKeys].filter(item => item !== node.dataRef.key);
                              setExpandedKeys([...newArr, node.dataRef.key]);
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
      </div>
        </Spin>
  );
}
