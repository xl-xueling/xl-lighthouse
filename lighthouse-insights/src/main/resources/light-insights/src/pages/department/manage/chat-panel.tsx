import React, { useEffect, useState } from 'react';
import {
    Space,
    Select,
    Input,
    Button,
    Typography,
    Spin, Tree,Checkbox
} from '@arco-design/web-react';
import {
    IconDownload,
    IconFaceSmileFill,
    IconFile,
    IconFolder, IconFolderAdd,
    IconIdcard,
    IconMinus,
    IconStar
} from '@arco-design/web-react/icon';
import axios from 'axios';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import MessageList from './message-list';
import styles from './style/index.module.less';
import { IconPlus } from '@arco-design/web-react/icon';

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
            console.log("rest is:" + JSON.stringify(rest));
            return (
                <Tree.Node icon={children ? <IconFolder /> : <IconFile/> } key={key} {...rest} dataRef={item}>
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

  const [treeData, setTreeData] = useState(TreeData);
  return (
      <div className={styles['chat-panel']}>
        <Tree
            blockNode={true}
            onSelect={(node) => {
                console.log(node)
            }}
            renderExtra={(node) => {
              return (
                  <div>
                <IconPlus
                    style={{
                        position: 'absolute',
                        right: 8,
                        fontSize: 12,
                        top: 10,
                        color: '#3370ff',
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
                      <IconMinus
                          style={{
                              position: 'absolute',
                              right: 25,
                              fontSize: 12,
                              top: 10,
                              color: '#3370ff',
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
