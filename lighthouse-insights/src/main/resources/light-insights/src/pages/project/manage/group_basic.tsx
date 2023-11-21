import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button, Form, Input, InputTag, Select
} from '@arco-design/web-react';
import {
    IconMinus,
    IconMinusCircleFill,
    IconMore,
    IconPen,
    IconPlus,
    IconPlusCircleFill
} from '@arco-design/web-react/icon';
import React from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import AceEditor from "react-ace";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";

interface StudioProps {
  userInfo?: {
    name?: string;
    avatar?: string;
  };
}




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

export default function GroupBasicInfo(props: StudioProps) {
  const t = useLocale(locale);
    const settings = useSelector((state: GlobalState) => state.settings);

    const columns: TableColumnProps[] = [
        {
            title: 'Column',
            dataIndex: 'name',
            headerCellStyle: { width:'18%'},
            className:'columnNameClass',
            render:(_,record) => (
                <Input />
            )
        },
        {
            title: 'Type',
            dataIndex: 'salary',
            className:'columnNameClass',
            headerCellStyle: { width:'18%' },
            render:(_,record) => (
                <Select size={"mini"} placeholder='Please select' style={{ }} defaultValue={1} >
                        <Select.Option key={1}  value={1}>
                            String
                        </Select.Option>
                    <Select.Option key={2}  value={2}>
                        Numberic
                    </Select.Option>
                </Select>
            )
        },
        {
            title: 'Description',
            dataIndex: 'address',
            className:'columnNameClass',
            headerCellStyle: {},
            render:(_,record) => (
                <Input />
            )
        },
        {
            title: 'Operation',
            dataIndex: 'email',
            className:'columnNameClass',
            headerCellStyle: { width:'14%'},
            render: (_, record) => (
                <Space size={24} direction="vertical" style={{ textAlign:"center",width:'100%',paddingTop:'5px' }}>
                    <IconMinusCircleFill />
                </Space>
            ),
        },
    ];

  return (
      <Card>
          <Form
              className={styles['search-form']}
              layout={"vertical"}
          >
              <Form.Item  field="name">
                  <Typography.Title
                      style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                  >
                      {'Group Token'}
                  </Typography.Title>
                  <Input
                      allowClear
                      placeholder={'Please Input Token'}
                  />
              </Form.Item>
              <Form.Item>
                  <Grid.Row>
                      <Grid.Col span={16}>
                          <Typography.Title
                              style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                          >
                              {'Columns'}
                          </Typography.Title>
                      </Grid.Col>
                      <Grid.Col span={8} style={{ textAlign: 'right' }}>
                          {/*<IconPlusCircleFill fontSize={15} />*/}
                            <Button type={"secondary"} size={"mini"}>添加</Button>
                      </Grid.Col>
                  </Grid.Row>
                  <Table className={"group-basic-panel"} size={"mini"} columns={columns} pagination={false} data={data} />
              </Form.Item>
          </Form>

      </Card>
  );
}
