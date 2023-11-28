import React, {useEffect, useRef, useState} from 'react';
import {Button, Input, Message, Space, Spin, Tree} from '@arco-design/web-react';
import {IconFile, IconFolder, IconMinus, IconPen, IconPlus} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {queryAll, add, dragTo, deleteById, updateById} from "@/api/department";
import {ResultData} from "@/types/insights-common";

export default function DepartmentManagePanel() {
    const t = useLocale(locale);
    const [loading, setLoading] = useState(false);
    const [treeData, setTreeData] = useState([]);
    const treeRef = useRef<Tree>(null);
    const [selectedKeys, setSelectedKeys] = useState([]);
    const [checkedKeys, setCheckedKeys] = useState([]);
    const [expandedKeys, setExpandedKeys] = useState([]);

    async function fetchData() {
        setLoading(true);
        try {
            await queryAll().then((res:ResultData) => {
                const {code, message} = res;
                let data = res.data;
                if (code === '0') {
                    data = [{
                        "id":"0",
                        "name":t['department.enterprise.structure'],"children":data}] ;
                    setTreeData([...data]);
                }else{
                    Message.error(message || t['system.error'])
                }
            });
        } catch (error) {
            console.log(error);
            Message.error(t['system.error']);
        }finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        fetchData().then();
    }, []);


    async function addNode(pid, title) {
        setLoading(true);
        let id = "-1";
        try {
            await add({'pid': pid, 'title': title}).then((res: any) => {
                const {code, msg, data} = res;
                if (code === '0') {
                    id = data.id;
                } else {
                    Message.error(res.message || t['system.error'])
                }
            });
        } catch (error) {
            console.log(error);
            Message.error(t['system.error']);
        } finally {
            setLoading(false);
        }
        return id;
    }


    async function dragNodeTo(id, destPid) {
        setLoading(true);
        let result = "-1";
        try {
            await dragTo({'id': id, 'destPid': destPid}).then((res: any) => {
                console.log("drag node res:" + JSON.stringify(res));
                const {code, msg, data} = res;
                if (code === '0') {
                    result = code;
                } else {
                    Message.error(res.message || t['system.error'])
                }
            });
        } catch (error) {
            console.log(error);
            Message.error(t['system.error']);
        } finally {
            setLoading(false);
        }
        return result;
    }

    async function updateNode(id, title) {
        setLoading(true);
        let result = "-1";
        try {
            await updateById({'id': id, 'title': title}).then((res: any) => {
                const {code, message, data} = res;
                if (code === '0') {
                    result = code;
                } else {
                    Message.error(message || t['system.error']);
                }
            });
        } catch (error) {
            console.log(error);
            Message.error(t['system.error']);
        } finally {
            setLoading(false);
        }
        return result;
    }


    async function deleteNode(id) {
        setLoading(true);
        let result = "-1";
        try {
            await deleteById({'id': id}).then((res: any) => {
                const {code, message, data} = res;
                if (code === '0') {
                    result = code;
                } else {
                    Message.error(message || t['system.error']);
                }
            });
        } catch (error) {
            console.log(error);
            Message.error(t['system.error']);
        } finally {
            setLoading(false);
        }
        return result;
    }

    const generatorTreeNodes = (treeData) => {
        return treeData.map((item) => {
            const { children, key, ...ret} = item;
            return (
                <Tree.Node icon={children || item.id == "0" ? <IconFolder /> : <IconFile/> }
                           key={item.id} title={item.name}  {...ret} dataRef={item}>
                    {children ? generatorTreeNodes(item.children) : null}
                </Tree.Node>
            );
        });
    };

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

    return (
        <Spin loading={loading} size={20} style={{ display: 'block' }}>
          <div className={styles['chat-panel']}>
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
                    const result = await dragNodeTo(dragNode.props._key, destPid);
                    if(result == '-1'){
                        return;
                    }
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
                                  if(currentId == "-1"){
                                      return;
                                  }
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
                          {node._level != 0  &&  (
                              <IconPen
                                  style={{
                                      display:`${node.dataRef.key != "0" ? 'block' : 'none'}`,
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
                                      node.dataRef.title = <Input type={"text"} autoFocus={true}
                                                                  style={{
                                                                      width: 120,
                                                                      height: 19,
                                                                      paddingLeft:3,
                                                                      borderColor: 'rgb(132 160 224)',
                                                                      backgroundColor: "var(--color-fill-1)"
                                                                  }}
                                                                  defaultValue={node.title.valueOf() + ""}
                                                                  onBlur={async (ie) => {
                                                                      const len = getStringLength(ie.target.value);
                                                                      if (len > 20) {
                                                                          Message.error(t['department.manage.limitExceeded']);
                                                                          node.dataRef.title = originTitle;
                                                                      } else {
                                                                          const newTitle = ie.target.value;
                                                                          if(newTitle.length  > 0 && newTitle != originTitle){
                                                                              const result = await updateNode(node.dataRef._key, newTitle);
                                                                              if(result == "0"){
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
                                      const newArr = [...expandedKeys].filter(item => item !== node.dataRef.key);
                                      setExpandedKeys([...newArr, node.dataRef.key]);
                                  }}
                              />
                          )}
                          {node._level != 0 && (
                              <IconMinus
                                  style={{
                                      display:`${node.dataRef.key != "0" ? 'block' : 'none'}`,
                                      position: 'absolute',
                                      right: 8,
                                      fontSize: 13,
                                      top: 10,
                                      color: 'rgb(132 160 224)',
                                  }}
                                  onClick={async (e) => {
                                      const dataChildren = node.dataRef.children || [];
                                      if (dataChildren.length > 0) {
                                          Message.error('The node has child,delete child-node first!')
                                      } else {
                                          const result = await deleteNode(node.dataRef.key);
                                          if (result == "-1") {
                                              return;
                                          }
                                          const w = deleteNodeByKey([...treeData], node.dataRef.key)
                                          setTreeData([...w]);
                                      }
                                  }}
                              />
                          )}
                      </div>
                  );
                }}
            >
              {generatorTreeNodes(treeData)}
            </Tree>
          </div>
        </Spin>
  );
}
