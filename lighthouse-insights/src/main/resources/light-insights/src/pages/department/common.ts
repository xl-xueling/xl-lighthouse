import {Department, ArcoTreeNode, ArcoFlatNode, ResponseTreeNode} from "@/types/insights-web";
import {requestQueryAll as queryDepartmentAll} from "@/api/department";
import {Message} from "@arco-design/web-react";

export async function fetchAllDepartmentData(): Promise<Array<Department>> {
    let result = null;
    await queryDepartmentAll().then((response) => {
        const {code,message,data} = response;
        if (code === '0') {
            result = data;
        }else{
            Message.error(message)
        }
    });
    return result;
}


export const translateToTreeStruct = (list, rootPid):Array<ArcoTreeNode> => {
    const nodeArr = new Array<ArcoTreeNode>();
    if(list){
        list.forEach(item => {
            if (item.pid === rootPid) {
                const children = translateToTreeStruct(list, item.id)
                const d:ArcoTreeNode = {
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

export const translate = (list:Array<Department>):Array<ArcoTreeNode> => {
    const nodeArr = new Array<ArcoTreeNode>();
    list?.forEach(item => {
        const nodeItem:ArcoTreeNode = {"key":String(item.id),"title":item.name};
        if(item.children){
            nodeItem.children = translate(item.children);
        }
        nodeArr.push(nodeItem)
    })
    return nodeArr;
}

export const translateResponse = (list:Array<ResponseTreeNode>):Array<ArcoTreeNode> => {
    const nodeArr = new Array<ArcoTreeNode>();
    list?.forEach(item => {
        const nodeItem:ArcoTreeNode = {"key":String(item.value),"title":item.label};
        if(item.children){
            nodeItem.children = translateResponse(item.children);
        }
        nodeArr.push(nodeItem)
    })
    return nodeArr;
}

export const translateToFlatStruct = (list):Array<ArcoFlatNode> => {
    function flattenTreeData(treeData, parentName = "") {
        let result = [];
        for (let i = 0; i < treeData.length; i++) {
            const { id, pid, name, children } = treeData[i];
            const fullName = parentName ? parentName + " > " + name : name;
            result.push({key:id,title:fullName})
            if (children && children.length > 0) {
                const childResult = flattenTreeData(children, fullName);
                result = result.concat(childResult);
            }
        }
        return result;
    }
   return flattenTreeData(list);
}


export const translateToTreeNodes = (list):Array<ArcoTreeNode> => {
    const nodeArr = new Array<ArcoTreeNode>();
    list?.forEach(item => {
        const nodeItem:ArcoTreeNode = {"key":String(item.value),"title":item.label};
        if(item.children){
            nodeItem.children = translate(item.children);
        }
        nodeArr.push(nodeItem)
    })
    return nodeArr;
}


export const getDepartment = (id:number,allDepartsInfo) : Department => {
    const getInfo = (id,arrays) => {
        for (let i = 0; i < arrays.length; i++) {
            const node = arrays[i];
            if (String(node.id) === String(id)) {
                return node;
            }
            if (node.children && node.children.length > 0) {
                const result = getInfo(id,node.children);
                if (result) {
                    return result;
                }
            }
        }
    }
    return getInfo(id,allDepartsInfo);
}

export const getCascadeDepartment = (department:Department,allDepartsInfo) : Array<Department> => {
    const array:Department[] = [];
    if(!department){
        return array;
    }
    array.unshift(department);
    let pid = department.pid;
    while (pid != 0){
        const parent = getDepartment(department.pid,allDepartsInfo);
        array.unshift(parent);
        pid = parent.pid;
    }
    return array;
}