import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button, Form, Input, InputTag, Select, Skeleton
} from '@arco-design/web-react';
import {
    IconMinus,
    IconMinusCircleFill,
    IconMore,
    IconPen,
    IconPlus,
    IconPlusCircleFill
} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import AceEditor from "react-ace";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import GroupStatistics from "@/pages/project/manage/statistic-list";
import {Column, Department, Group, Stat, User} from "@/types/insights-web";
import {requestQueryById} from "@/api/group";
import {requestQueryByGroupId} from "@/api/stat";
import EditTable, {EditTableColumn, EditTableColumnProps} from "@/pages/components/edittable/EditTable";


export default function GroupBasicInfo(props:{groupId?}) {

    const t = useLocale(locale);

    const groupId = props.groupId;

    const editTableRef= useRef(null);

    const [loading,setLoading] = useState<boolean>(true);

    const [groupInfo,setGroupInfo] = useState<Group>(null);

    const [statsInfo,setStatsInfo] = useState<Array<Stat>>([]);

    const [formInstance] = Form.useForm();

    useEffect(() => {
        if(groupId){
            setLoading(true);
            const promiseFetchGroupInfo:Promise<Group> = new Promise<Group>((resolve, reject) => {
                console.log("start to Fetch Group Info with id:" + groupId);
                let result;
                const proc = async () => {
                    const response = await requestQueryById(groupId);
                    if(response.code != '0'){
                        reject(new Error(response.message));
                    }
                    result = response.data;
                    resolve(result);
                }
                proc().then();
            })

            const promiseFetchStatsInfo:Promise<Array<Stat>> = new Promise<Array<Stat>>((resolve, reject) => {
                let result;
                const proc = async () => {
                    const response = await requestQueryByGroupId(groupId);
                    console.log("response is:" + JSON.stringify(response));
                    if(response.code != '0'){
                        reject(new Error(response.message));
                    }
                    result = response.data;
                    resolve(result);
                }
                proc().then();
            })

            const promiseAll:Promise<[Group,Array<Stat>]> = Promise.all([
                promiseFetchGroupInfo,
                promiseFetchStatsInfo,
            ])

            promiseAll.then((results) => {
                setGroupInfo(results[0]);
                setStatsInfo(results[1]);
            }).catch(error => {
                console.log(error);
                Message.error(t['system.error']);
            }).finally(() => {
                setLoading(false);
            });
        }
    },[groupId])

    const [initData,setInitData] = useState(null);

    useEffect(() => {
        if(groupInfo && groupInfo.columns){
            const columnArr:Array<EditTableColumn> = [];
            for(let i=0;i<groupInfo.columns.length;i++){
                const columnInfo = groupInfo.columns[i];
                columnArr.push({...columnInfo,"key":i})
            }
            setInitData(columnArr);
        }
    },[groupInfo])


    const loadingNode = (rows = 1) => {
        return (
            <Skeleton
                text={{
                    rows,
                    width: new Array(rows).fill('100%'),
                }}
                animation
            />
        );
    };

    const data = [
        {
            key:2,
            cover:
                'http://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/c788fc704d32cf3b1136c7d45afc2669.png~tplv-uwbnlip3yd-webp.webp',
            name: '视频直播',
            duration: '00:05:19',
            id: '123',
            timeparam:2,
            tsss:3,
            template:'<stat-item title="每分钟_各省份_uv统计" stat="bitcount(userId)" dimens="province"/>',
            status: -1,
        },
    ];



    const columnsProps: EditTableColumnProps[]  = [
        {
            title: 'Name',
            dataIndex: 'name',
            editable: true,
            isSelect:false,
            headerCellStyle: { width:'15%'},
        },
        {
            title: 'Type',
            dataIndex: 'type',
            editable: true,
            isSelect:true,
            headerCellStyle: { width:'12%'},
            render:(k,v) => (
                <Select size={"mini"} placeholder='Please select'
                        onChange={editTableRef.current.cellValueChangeHandler}
                        defaultValue={k}
                >
                    <Select.Option key={1}  value={1}>
                        String
                    </Select.Option>
                    <Select.Option key={2}  value={2}>
                        Number
                    </Select.Option>
                </Select>
            )
        },
        {
            title: 'Description',
            dataIndex: 'desc',
            isSelect:false,
            editable: true,
        },
        {
            title: 'Operate',
            dataIndex: 'operate',
            isSelect:false,
            headerCellStyle: { width:'15%'},
            render: (_, record) => (
                <Space size={24} direction="vertical" style={{ textAlign:"center",width:'100%',paddingTop:'5px' }}>
                    <IconMinusCircleFill style={{ cursor:"pointer"}} onClick={() => editTableRef.current.removeRow(record.key)}/>
                </Space>
            ),
        },
    ];


    return (
      <Card>
          <Form
              form={formInstance}
              className={styles['search-form']}
              layout={"vertical"}
          >
              <Typography.Title
                  style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
              >
                  {'Token'}
              </Typography.Title>
              <Form.Item field="token">
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
                            <Button type={"secondary"} size={"mini"} onClick={() => editTableRef.current.addRow()}>添加</Button>
                      </Grid.Col>
                  </Grid.Row>


                  <EditTable ref={editTableRef} columns={columnsProps} initData={initData}/>
              </Form.Item>
              <Form.Item>
                  <Grid.Row>
                      <Grid.Col span={16}>
                          <Typography.Title
                              style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                          >
                              {'Templates'}
                          </Typography.Title>
                      </Grid.Col>
                      <Grid.Col span={8} style={{ textAlign: 'right' }}>
                          <Button type={"secondary"} size={"mini"}>添加</Button>
                      </Grid.Col>
                  </Grid.Row>



                  <GroupStatistics statsInfo={data}/>
              </Form.Item>
              <Form.Item>
                  <Grid.Row>
                      <Grid.Col span={24} style={{ textAlign: 'right' }}>
                          <Button type={"primary"} size={"small"}>提交</Button>
                      </Grid.Col>
                  </Grid.Row>
              </Form.Item>
          </Form>
      </Card>

  );
}
