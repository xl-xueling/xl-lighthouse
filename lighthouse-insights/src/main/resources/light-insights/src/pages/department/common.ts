import {Department, ArcoTreeNode, ArcoFlatNode, TreeNode} from "@/types/insights-web";
import {requestStructure as queryDepartmentAll} from "@/api/department";
import {Message} from "@arco-design/web-react";

export async function fetchAllDepartmentData(): Promise<Array<TreeNode>> {
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

export const getFullPathNodes = (id, treeData:Array<TreeNode>) => {
    let result = [];
    function findNodeAndParents(node, parents) {
        if (node.value === id) {
            result = parents.concat(node);
        } else {
            if (node.children && node.children.length > 0) {
                for (const child of node.children) {
                    findNodeAndParents(child, parents.concat(node));
                }
            }
        }
    }
    for (const node of treeData) {
        findNodeAndParents(node, []);
    }
    return result;
}

export const translate = (list:Array<TreeNode>):Array<ArcoTreeNode> => {
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

export const translateResponse = (list:Array<TreeNode>):Array<ArcoTreeNode> => {
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
            nodeItem.children = translateToTreeNodes(item.children);
        }
        nodeArr.push(nodeItem)
    })
    return nodeArr;
}

