import React, {useEffect, useRef, useState} from 'react';
import {Button, Input, Message, Space, Spin, Tree} from '@arco-design/web-react';
import {
    IconFile,
    IconFolder,
    IconPlus, IconTag,
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from '../manage/locale';
import styles from '../manage/style/index.module.less';
import {requestStructure} from "@/api/project";
import {ArcoTreeNode} from "@/types/insights-web";

export default function ProjectTree({projectId,editEnable= true
     ,filterTypes=new Array<number>()
     ,handlerProcess
}) {

  const t = useLocale(locale);
  const [treeData, setTreeData] = useState<Array<ArcoTreeNode>>([]);
  const [loading, setLoading] = useState(false);
  const [expandedKeys,setExpandedKeys] = useState<Array<string>>([]);
  const [selectedKeys,setSelectedKeys] = useState<Array<string>>([]);
  const [visible ,setVisible] = useState<boolean>(true);
  const treeRef = useRef<Tree>(null);

    useEffect(() => {
        fetchData().then();
    }, []);

    async function fetchData() {
        setLoading(true);
        await requestStructure(projectId).then((result) => {
            if(result.code == '0'){
                if(result.data.list){
                   setExpandedKeys( result.data.list.map(x => x.key))
                   setTreeData( result.data.list);
                }
            }else{
                Message.error(result.message || t['system.error']);
            }
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error']);
        }).finally(() => {
            setLoading(false);
        })
    }

    const generatorTreeNodes = (treeData,level = 0) => {
        return treeData.map((item) => {
            const { children, key, ...ret} = item;
            let icon;
            const curLevel = level;
            if(level == 0){
                icon = <IconFolder/>;
            }else if(level == 1){
                icon = <IconTag/>
            }else{
                icon = <IconFile/>;
            }
            if(filterTypes.length && item.type && !filterTypes.includes(item.type)){
                return ;
            }
            return (
                <Tree.Node icon={icon} key={key} {...ret} dataRef={item}>
                    {children ? generatorTreeNodes(item.children,curLevel + 1) : null}
                </Tree.Node>
            );
        });
    };

    return (
        <Spin loading={loading} size={20} delay={500}  style={{ display: 'block' }}>
          <div className={styles['chat-panel']} style={{ display:`${visible ? 'block' : 'none'}` }}>
            <Tree
                ref={treeRef}
                expandedKeys={expandedKeys}
                selectedKeys={selectedKeys}
                draggable={false}
                multiple={false}
                onSelect={(keys, extra) => {
                    setSelectedKeys(keys);
                    if (extra.e.isTrusted && selectedKeys[0] === keys[0] && [...expandedKeys].find(item => item === keys[0])) {
                        const newArr = [...expandedKeys].filter(item => item !== keys[0]);
                        setExpandedKeys([...newArr]);
                    } else {
                        const newArr = [...expandedKeys].filter(item => item !== keys[0]);
                        setExpandedKeys([...newArr, ...keys]);
                    }
                    const id = treeRef.current.getCacheNode([keys[0]])[0].props.dataRef.id
                    handlerProcess('group-manage', {"groupId":id});
                }}
                onExpand={(keys, extra) => {
                    setExpandedKeys(keys);
                }}
                renderExtra={(node) => {
                    if(!editEnable){
                        return ;
                    }
                    return (
                        <div>
                            {node._level == 0  &&  (
                            <IconPlus
                                style={{
                                    position: 'absolute',
                                    right: 8,
                                    fontSize: 14,
                                    strokeWidth: 6,
                                    top: 8,
                                    color: 'rgb(132 160 224)',
                                }}

                                onClick={async (e) => {
                                    await handlerProcess('group-add', null);
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
