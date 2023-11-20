import React, {useEffect, useRef, useState} from 'react';
import {Button, Input, Message, Space, Spin, Tree} from '@arco-design/web-react';
import {IconFile, IconFolder, IconMinus, IconPen, IconPlus} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from '../manage/locale';
import styles from '../manage/style/index.module.less';
import {requestStructure} from "@/api/project";
import {ArcoTreeNode} from "@/types/insights-common";

export default function ProjectTree({projectId}) {

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

    const generatorTreeNodes = (treeData) => {
        return treeData.map((item) => {
            const { children, key, ...ret} = item;
            return (
                <Tree.Node icon={children || item.key == "0" ? <IconFolder /> : <IconFile/> }
                           key={key} {...ret} dataRef={item}>
                    {children ? generatorTreeNodes(item.children) : null}
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
            >
              {generatorTreeNodes(treeData)}
            </Tree>
          </div>
        </Spin>
  );
}
