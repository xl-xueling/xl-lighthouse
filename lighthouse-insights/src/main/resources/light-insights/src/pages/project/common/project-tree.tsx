import React, {useEffect, useRef, useState} from 'react';
import {Button, Input, Message, Space, Spin, Tree} from '@arco-design/web-react';
import {
    IconFile,
    IconFolder,
    IconMinus,
    IconPen,
    IconPenFill,
    IconPlus, IconPlusCircle,
    IconPlusCircleFill
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from '../manage/locale';
import styles from '../manage/style/index.module.less';
import {requestStructure} from "@/api/project";
import {ArcoTreeNode} from "@/types/insights-common";

export default function ProjectTree({projectId,editEnable=true}) {

  const t = useLocale(locale);
  const [treeData, setTreeData] = useState<Array<ArcoTreeNode>>([]);
  const [loading, setLoading] = useState(false);
  const [expandKeys,setExpandKeys] = useState<Array<string>>([]);
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
                    setExpandKeys(result.data.list.map(x => x.key))
                    setTreeData(result.data.list);
                }
            }else{
                Message.error(result.message || t['system.error']);
            }
        }).catch((error) => {
            console.log("error is:" + error);
            Message.error(t['system.error']);
        }).finally(() => {
            setLoading(false);
        })
    }

    const generatorTreeNodes = (treeData,level = 1) => {
        return treeData.map((item) => {
            const { children, key, ...ret} = item;
            return (
                <Tree.Node icon={children || item.key == "0" ? <IconFolder /> : <IconFile/> }
                           key={key} {...ret} dataRef={item}>
                    {children ? generatorTreeNodes(item.children,level++) : null}
                </Tree.Node>
            );
        });
    };

    return (
        <Spin loading={loading} size={20} delay={500}  style={{ display: 'block' }}>
          <div className={styles['chat-panel']} style={{ display:`${visible ? 'block' : 'none'}` }}>
            <Tree
                ref={treeRef}
                expandedKeys={expandKeys}
                draggable={false}
                multiple={false}
                renderExtra={(node) => {
                    if(!editEnable){
                        return ;
                    }
                    return (
                        <div>
                            <IconPlus
                                style={{
                                    position: 'absolute',
                                    right: 50,
                                    fontSize: 13,
                                    top: 10,
                                    color: 'rgb(132 160 224)',
                                }}
                            />
                            {node._level != 0  &&  (
                            <IconPen
                                style={{
                                    display:`${node.dataRef.key != "0" ? 'block' : 'none'}`,
                                    position: 'absolute',
                                    right: 29,
                                    fontSize: 13,
                                    top: 10,
                                    color: 'rgb(132 160 224)',
                                }} />
                                )}

                            {node._level != 0  &&  (
                            <IconMinus
                                style={{
                                    display:`${node.dataRef.key != "0" ? 'block' : 'none'}`,
                                    position: 'absolute',
                                    right: 8,
                                    fontSize: 13,
                                    top: 10,
                                    color: 'rgb(132 160 224)',
                                }}
                            />)}

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
