import {Project} from "@/types/insights-web";


export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: t['callerAuthList.column.label.id'],
            dataIndex: 'resourceId',
        },
        {
            title: t['callerAuthList.column.label.resourceType'],
            dataIndex: 'resourceType',
        },
        {
            title: t['callerAuthList.column.label.Department'],
            dataIndex: 'address',
        },
        {
            title: t['callerAuthList.column.label.admins'],
            dataIndex: 'email',
        },
    ];

}