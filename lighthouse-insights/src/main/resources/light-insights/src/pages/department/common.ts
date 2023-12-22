import {Department, ArcoTreeNode, ArcoFlatNode} from "@/types/insights-web";
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

export const translateToMapStruct = (list:Array<Department>):Map<string,Department> => {
    const result = new Map();
    function traverse(nodes) {
        nodes.forEach(node => {
            const { id, name, children } = node;
            result.set(id, {id,name});
            if (children && children.length > 0) {
                traverse(children);
            }
        });
    }
    traverse(list);
    return result;
}

export const translateToFlatStruct = (list):Array<ArcoFlatNode> => {
    const nodeArr = new Array<ArcoFlatNode>();
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
            const d:ArcoFlatNode = {
                "key":item.id,
                "title":title,
            }
            nodeArr.push(d);
        })
    }
    return nodeArr;
}