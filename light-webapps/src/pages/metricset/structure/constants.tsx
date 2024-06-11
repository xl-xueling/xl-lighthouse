import {TreeNode} from "@/types/insights-web";
import {getResourceTypeDescription} from "@/desc/base";
import {ResourceTypeEnum} from "@/types/insights-common";
import {treeCheckContainsNode} from "@/pages/department/base";
import {Button, Space, Typography} from "@arco-design/web-react";
import {getRandomString} from "@/utils/util";
import React from "react";
const Text = Typography.Text;

const getColumns = (t: any,listNodes:TreeNode[], callback: (record: Record<string, any>, type: string) => Promise<void>) => {
    return [
        {
            title: t['repositoryModal.column.label.id'],
            dataIndex: 'resourceId',
            render: (value, record) => {
                return <Text>{value}</Text>
            },
        },
        {
            title: t['repositoryModal.column.label.title'],
            dataIndex: 'title',
            render: (value, record) => {
                return <Text>{value}</Text>
            },
        },
        {
            title: t['repositoryModal.column.label.resourceType'],
            dataIndex: 'resourceType',
            render: (value, record) => {
                return getResourceTypeDescription(t,value);
            },
        },
        {
            title: t['repositoryModal.column.label.relationShip'],
            dataIndex: 'fullTitle',
            render: (value, record) => {
                if(record.resourceType == ResourceTypeEnum.Stat){
                    const array = value.split(";");
                    return array[0] +  '  >  ' + array[1];
                }
            },
        },
        {
            title: t['repositoryModal.column.label.operations'],
            dataIndex: 'operations',
            render: (value, record) => {
                let button;
                let type;
                if(record.resourceType == ResourceTypeEnum.Stat){
                    type = 'stat';
                }else if(record.resourceType == ResourceTypeEnum.Project){
                    type = 'project';
                }
                if(!treeCheckContainsNode(listNodes,record.resourceId,type)){
                    button = <Button key={getRandomString()}
                                     type="text"
                                     onClick={() => callback(record,'add')}
                                     size="mini">
                        {t['repositoryModal.column.label.operations.add']}
                    </Button>;
                }else{
                    button = <Button disabled={true} key={getRandomString()}
                                     type="secondary"
                                     size="mini">
                        {t['repositoryModal.column.label.operations.added']}
                    </Button>;
                }
                return  <Space key={getRandomString()} size={0} direction="horizontal">{[button]}</Space>;
            }
        }
    ];
}

export default getColumns;