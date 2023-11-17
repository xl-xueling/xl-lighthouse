import {Department, DepartmentArcoFlatNode, DepartmentArcoTreeNode} from "@/types/insights-web";
import {queryAll as queryDepartmentAll} from "@/api/department";
import {Message} from "@arco-design/web-react";

export async function fetchAllData(): Promise<Array<Department>> {
    let result = null;
    try {
        await queryDepartmentAll().then((res) => {
            const {code,data} = res;
            if (code === '0' && data) {
                result = data;
            }else{
                Message.error("System Error,fetch department data failed!")
            }
        });
    } catch (error) {
        console.error("error is:" + error);
        Message.error("System Error,fetch department data failed!")
    }
    return result;
}


export const translateToTreeStruct = (list, rootPid):Array<DepartmentArcoTreeNode> => {
    const nodeArr = new Array<DepartmentArcoTreeNode>();
    if(list){
        list.forEach(item => {
            if (item.pid === rootPid) {
                const children = translateToTreeStruct(list, item.id)
                const d:DepartmentArcoTreeNode = {
                    "key":item.id,
                    "title":item.name,
                    "children":children,
                }
                nodeArr.push(d);
            }
        })
    }
    return nodeArr;
}

export const translateToFlatStruct = (list):Array<DepartmentArcoFlatNode> => {
    const nodeArr = new Array<DepartmentArcoFlatNode>();
    if(list){
        const departMap = new Map<string,Department>();
        list.forEach(z => {
            departMap.set(z.id.toString(),z);
        })
        list.forEach(item => {
            let title = "";
            const parentArr = item.fullpath.split(",");
            for (let i = 0; i < parentArr.length; i++) {
                const curDepart = departMap.get(parentArr[i]);
                if(curDepart){
                    title += curDepart.name;
                    if(i !== parentArr.length - 1){
                        title += "_";
                    }
                }
            }
            const d:DepartmentArcoFlatNode = {
                "key":item.id,
                "title":title,
            }
            nodeArr.push(d);
        })
    }
    return nodeArr;
}