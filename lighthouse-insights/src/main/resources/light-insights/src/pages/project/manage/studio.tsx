import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button
} from '@arco-design/web-react';
import {IconMinus, IconMinusCircleFill, IconMore, IconPlusCircleFill} from '@arco-design/web-react/icon';
import React from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';

interface StudioProps {
  userInfo: {
    name?: string;
    avatar?: string;
  };
}

const columns: TableColumnProps[] = [
    {
        title: 'Column',
        dataIndex: 'name',
    },
    {
        title: 'Type',
        dataIndex: 'salary',
    },
    {
        title: 'Desc',
        dataIndex: 'address',
    },
    {
        title: 'Operation',
        dataIndex: 'email',
        render: (_, record) => (
            <Space size={16} direction="horizontal" style={{ textAlign:"center" }}>
                {/*<Popconfirm*/}
                {/*    focusLock*/}
                {/*    title='Confirm'*/}
                {/*    content='Are you sure to reset this user password?'*/}
                {/*    onCancel={() => {*/}
                {/*        Message.error({*/}
                {/*            content: 'cancel',*/}
                {/*        });*/}
                {/*    }}*/}
                {/*>*/}
                {/*    <Button*/}
                {/*        type="secondary"*/}
                {/*        size="mini">*/}
                {/*        {'删除'}*/}
                {/*    </Button>*/}
                {/*</Popconfirm>*/}
                <IconMinusCircleFill />
            </Space>
        ),
    },
];
const data = [
    {
        key: '1',
        name: 'Jane Doe',
        salary: 23000,
        address: '32 Park Road, London',
        email: 'jane.doe@example.com',
    },
    {
        key: '2',
        name: 'Alisa Ross',
        salary: 25000,
        address: '35 Park Road, London',
        email: 'alisa.ross@example.com',
    },
    {
        key: '3',
        name: 'Kevin Sandra',
        salary: 22000,
        address: '31 Park Road, London',
        email: 'kevin.sandra@example.com',
    },
    {
        key: '4',
        name: 'Ed Hellen',
        salary: 17000,
        address: '42 Park Road, London',
        email: 'ed.hellen@example.com',
    },
    {
        key: '5',
        name: 'William Smith',
        salary: 27000,
        address: '62 Park Road, London',
        email: 'william.smith@example.com',
    },
];

export default function Studio(props: StudioProps) {
  const t = useLocale(locale);
  const { userInfo } = props;
  return (
      <Card>
          <Grid.Row>
                   <Grid.Col span={16}>
                     <Typography.Title
                      style={{ marginTop: 0, marginBottom: 16 }}
                      heading={6}
                    >
                      {'字段信息'}
                    </Typography.Title>
                  </Grid.Col>
              <Grid.Col span={8} style={{ textAlign: 'right' }}>
                  <IconPlusCircleFill fontSize={15} />
              </Grid.Col>

          </Grid.Row>
          <div className={styles['studio-wrapper']}>

              <Table size={"small"} columns={columns} pagination={false} data={data} />
          </div>
      </Card>
  );
}
