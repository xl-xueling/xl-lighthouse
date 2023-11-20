import React, {useEffect, useRef, useState} from 'react';
import {Button, Input, Message, Space, Spin, Tree} from '@arco-design/web-react';
import {IconFile, IconFolder, IconMinus, IconPen, IconPlus} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {queryAll, add, dragTo, deleteById, updateById} from "@/api/department";
import {Simulate} from "react-dom/test-utils";
import {requestStructure} from "@/api/project";

export default function ProjectTree() {

  const t = useLocale(locale);
  const [treeData, setTreeData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [visible ,setVisible] = useState<boolean>(true);

    useEffect(() => {
        fetchAllData();
    }, []);

    async function fetchAllData() {
        setLoading(false);
        await requestStructure(1).then((result) => {
            setTreeData(result.data.list);
        })
    }

    async function addNode(pid, title) {
        setLoading(true);
        let id = "-1";
        try {
            await add({'pid': pid, 'title': title}).then((res: any) => {
                const {code, msg, data} = res;
                if (code === '0' && data) {
                    id = data.id;
                } else {
                    Message.error("System Error,add department node failed!")
                }
            });
        } catch (error) {
            console.error("error is:" + error);
            Message.error("System Error,add department node failed!")
        } finally {
            setLoading(false);
        }
        return id;
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
        >
          {generatorTreeNodes(treeData)}
        </Tree>
      </div>
        </Spin>
  );
}
