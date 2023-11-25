import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button, Form, Input, InputTag, Select, Skeleton, Spin, Tag, Icon, Tabs
} from '@arco-design/web-react';
import {
    IconDashboard, IconFile,
    IconMinus,
    IconMinusCircleFill,
    IconMore,
    IconPen, IconPenFill,
    IconPlus,
    IconPlusCircleFill, IconTag, IconThunderbolt
} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import AceEditor from "react-ace";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import GroupStatistics from "@/pages/project/manage/statistic-list";
import {
    Column,
    Department,
    Group,
    PrivilegeEnum,
    Project,
    ProjectPagination,
    Stat,
    StatPagination,
    User
} from "@/types/insights-web";
import {requestQueryById} from "@/api/group";
import {requestQueryByGroupId} from "@/api/stat";
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/components/edittable/EditTable";
import StatEditPanel from "@/pages/project/manage/stat_edit";
import GroupEditPanel from "@/pages/project/manage/group_edit";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import GroupBasicPanel from "@/pages/group/basic";
import {ResultData} from "@/types/insights-common";
import {requestPrivilegeCheck} from "@/api/privilege";


export default function GroupManagePanel({groupId = 1,onClose}) {

    const TabPane = Tabs.TabPane;

    const [groupInfo,setGroupInfo] = useState<Group>(null);

    const [statsInfo,setStatsInfo] = useState<Array<Stat>>([]);

    const [loading,setLoading] = useState<boolean>(true);

    const t = useLocale(locale);

    useEffect(() => {
        if(groupId){
            setLoading(true);
            const promiseFetchGroupInfo:Promise<Group> = new Promise<Group>((resolve, reject) => {
                console.log("start to Fetch Group Info with id:" + groupId);
                let result:Group;
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

            // const fetchPrivilegeData = async ({type,items}):Promise<ResultData> => {
            //     return new Promise((resolve) => {
            //         const proc = async () => {
            //             const result = await requestPrivilegeCheck({type:type,items:items});
            //             resolve(result);
            //         }
            //         proc();
            //     })
            // }
            //
            // const combinePaginationData = (p1:Array<Stat>,p2:Record<string, Array<number>>) => {
            //     return  p1.reduce((result:StatPagination[],item:Project) => {
            //         const combinedItem = { ...item, ...{"permissions":p2[item.id],"key":item.id}};
            //         result.push(combinedItem);
            //         return result;
            //     },[])
            // }

            //组合列表权限数据
            const combinePrivilegeData = async(stats:Stat[]):Promise<Record<number,Array<PrivilegeEnum>>> => {
                return new Promise((resolve) => {
                    const proc = async () => {
                        const statIds = stats!.map(z => z.id);
                        const privilegeData = await requestPrivilegeCheck({type:"stat",items:statIds})
                        //resolve(privilegeData);
                        resolve(null);
                    }
                    proc();
                })
            }

            //组合工程数据
            const combineProjectData = async(stats:Stat[]):Promise<Record<number,Project>> => {
                return new Promise((resove) => {
                    const proc = async () => {
                        resove([]);
                    }
                })
            }

            //组合统计组数据
            const combineGroupData = async(stats:Stat[]):Promise<Record<number,Group>> => {
                return new Promise((resove) => {
                    const proc = async () => {
                        resove([]);
                    }
                })
            }

            //组合部门数据
            const combineDepartmentData = async(stats:Stat[]):Promise<Record<number,Department>> => {
                return new Promise((resove) => {
                    const proc = async () => {
                        resove([]);
                    }
                })
            }

            const promiseFetchStatsInfo:Promise<Array<Stat>> = new Promise<Array<Stat>>((resolve, reject) => {
                const proc = async () => {
                    const response = await requestQueryByGroupId(groupId);
                    if(response.code != '0'){
                        reject(new Error(response.message));
                    }
                    // const statIds = response.data.list!.map(z => z.id);
                    // const privilegeData = await fetchPrivilegeData({type:"stat",items:statIds});
                    // const listData = combinePaginationData(response.data.list,privilegeData.data);
                    resolve(response.data.list);
                }
                proc().then();
            })

            const promiseAll:Promise<[Group,Array<Stat>]> = Promise.all([
                promiseFetchGroupInfo,
                promiseFetchStatsInfo,
            ])

            promiseAll.then((results) => {
                // console.log("groupInfo:" + JSON.stringify(results[0]))
                // console.log("statsInfo:" + JSON.stringify(results[1]))
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

    return (
    <div className={styles['layout-content']}>
        <div className={styles['manage-panel']}>
            <Tabs type={"line"} defaultActiveTab='1'>
                <TabPane
                    key='1'
                    title={
                        <span>
                <IconThunderbolt style={{ marginRight: 6 }} />
                    Statistic Items
              </span>
                    }
                >

                    <Tabs
                        style={{ marginBottom:'8px' }}
                        type="rounded"
                        extra={
                            <Input.Search
                                style={{ width: '240px' }}
                            />
                        }
                    >
                    </Tabs>
                    <StatisticalListPanel  statsInfo={statsInfo}/>
                </TabPane>
                <TabPane
                    key='3'
                    title={
                        <span>
                <IconTag style={{ marginRight: 6 }} />
                Group Info
              </span>
                    }
                >
                    <GroupBasicPanel groupId={123}/>
                </TabPane>
            </Tabs>
        </div>
    </div>);

}