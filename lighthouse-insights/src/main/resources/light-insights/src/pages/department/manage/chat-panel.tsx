import React, {useEffect, useRef, useState} from 'react';
import {Input, Tree} from '@arco-design/web-react';
import {IconFile, IconFolder, IconMinus, IconPen, IconPlus} from '@arco-design/web-react/icon';
import axios from 'axios';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';

export default function ChatPanel() {
  const t = useLocale(locale);
  const [messageList, setMessageList] = useState([]);
  const [loading, setLoading] = useState(false);

  function fetchMessageList() {
    setLoading(true);
    axios
      .get('/api/chatList')
      .then((res) => {
        setMessageList(res.data || []);
      })
      .finally(() => {
        setLoading(false);
      });
  }

  useEffect(() => {
    fetchMessageList();
  }, []);


    const TreeNode = Tree.Node; // 从treedata 生成 treenode

    const generatorTreeNodes = (treeData) => {
        return treeData.map((item) => {
            const { children, key, ...rest } = item;
            return (
                <Tree.Node icon={children ? <IconFolder /> : <IconFile/> }
                           key={key} {...rest} dataRef={item}>
                    {children ? generatorTreeNodes(item.children) : null}
                </Tree.Node>
            );
        });
    };

    const TreeData = [
        {
            title: 'Trunk',
            key: '0-0',
            children: [
                {
                    title: 'Leaf',
                    key: '0-0-1',
                },
                {
                    title: 'Branch',
                    key: '0-0-2',
                    children: [
                        {
                            title: 'Leaf',
                            key: '0-0-2-1',
                        },
                    ],
                },
            ],
        },
        {
            title: 'Trunk',
            key: '0-1',
            children: [
                {
                    title: 'Branch',
                    key: '0-1-1',
                    children: [
                        {
                            title: 'Leaf',
                            key: '0-1-1-1',
                        },
                        {
                            title: 'Leaf',
                            key: '0-1-1-2',
                        },
                    ],
                },
                {
                    title: 'Leaf',
                    key: '0-1-2',
                },
            ],
        },
    ];

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

  const [treeData, setTreeData] = useState(TreeData);
  const editRef = useRef(null);
  return (
      <div className={styles['chat-panel']}>
        <Tree
            autoExpandParent
            blockNode={true}
            onSelect={(nodeId,extra) => {
                console.log("select!")
                // const node = extra.node.props.dataRef;
                // extra.node.props.dataRef.title = <Input
                //     style={{ width:110,height:20 }}
                //     defaultValue={ node.title.valueOf()+"" }
                //     onBlur={ (event) => {
                //         extra.node.props.dataRef.title = event.target.value;
                //         setTreeData([...treeData]);
                //     }}
                // />
                // setTreeData([...treeData]);
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
                          onClick={() => {
                              const dataChildren = node.dataRef.children || [];
                              dataChildren.push({
                                  title: 'new node',
                                  key: node._key + '-' + (dataChildren.length + 1),
                              });
                              node.dataRef.children = dataChildren;
                              setTreeData([...treeData]);
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
                          onClick={() => {
                              node.dataRef.title = <Input type={"text"} ref={editRef} autoFocus={true}
                                                            style={{
                                                                width: 110,
                                                                height: 20,
                                                                borderColor: "blue",
                                                                backgroundColor: "white"
                                                            }}
                                                            defaultValue={node.title.valueOf() + ""}
                                                            onBlur={(event) => {
                                                                node.dataRef.title = event.target.value;
                                                                setTreeData([...treeData]);
                                                            }}
                                />;
                              setTreeData([...treeData]);
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
                          onClick={() => {
                              const dataChildren = node.dataRef.children || [];
                              dataChildren.push({
                                  title: 'new tree node',
                                  key: node._key + '-' + (dataChildren.length + 1),
                              });
                              node.dataRef.children = dataChildren;
                              setTreeData([...treeData]);
                          }}
                      />
                  </div>

              );
            }}
        >
          {generatorTreeNodes(treeData)}
        </Tree>
      </div>
  );

  // return (
  //   <div className={styles['chat-panel']}>
  //     <div className={styles['chat-panel-header']}>
  //       <Typography.Title
  //         style={{ marginTop: 0, marginBottom: 16 }}
  //         heading={6}
  //       >
  //         {t['monitor.title.chatPanel']}
  //       </Typography.Title>
  //       <Space size={8}>
  //         <Select style={{ width: 80 }} defaultValue="all">
  //           <Select.Option value="all">
  //             {t['monitor.chat.options.all']}
  //           </Select.Option>
  //         </Select>
  //         <Input.Search
  //           placeholder={t['monitor.chat.placeholder.searchCategory']}
  //         />
  //         <Button type="text" iconOnly>
  //           <IconDownload />
  //         </Button>
  //       </Space>
  //     </div>
  //     <div className={styles['chat-panel-content']}>
  //       <Spin loading={loading} style={{ width: '100%' }}>
  //         <MessageList data={messageList} />
  //       </Spin>
  //     </div>
  //     <div className={styles['chat-panel-footer']}>
  //       <Space size={8}>
  //         <Input suffix={<IconFaceSmileFill />} />
  //         <Button type="primary">{t['monitor.chat.update']}</Button>
  //       </Space>
  //     </div>
  //   </div>
  // );
}
