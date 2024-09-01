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
    if(!treeData){
        return result;
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
            const { key, label, value, children } = treeData[i];
            const fullName = parentName ? parentName + " > " + label:label;
            result.push({key:key,title:fullName})
            if (children && children.length > 0) {
                const childResult = flattenTreeData(children, fullName);
                result = result.concat(childResult);
            }
        }
        return result;
    }
   return flattenTreeData(list);
}


export const treeCheckContainsNode = (list:Array<TreeNode>,targetValue:string,targetType:string) => {
    let containsKey = false;
    const checkContainsKey = (node: TreeNode) => {
        if (node.value === targetValue && node.type === targetType) {
            containsKey = true;
            return;
        }
        if (node.children && node.children.length > 0) {
            for (const childNode of node.children) {
                checkContainsKey(childNode);
            }
        }
    };
    for (const rootNode of list) {
        checkContainsKey(rootNode);
    }
    return containsKey;
}

export const translateToTreeNodes = (list):Array<ArcoTreeNode> => {
    const nodeArr = new Array<ArcoTreeNode>();
    list?.forEach(item => {
        const nodeItem:ArcoTreeNode = {"key":item.value,"title":item.label};
        if(item.children){
            nodeItem.children = translateToTreeNodes(item.children);
        }
        nodeArr.push(nodeItem)
    })
    return nodeArr;
}

export const translateToCascadeTreeNodes = (list,parentKey = ""):Array<ArcoTreeNode> => {
    const nodeArr = new Array<ArcoTreeNode>();
    list?.forEach(item => {
        let value;
        if(parentKey == ''){
            value = item.value + "," + item.label;
        }else{
            value = parentKey + ";" + item.value+"," + item.label;
        }
        const nodeItem:ArcoTreeNode = {"key":value,"title":item.label};
        if(item.children){
            nodeItem.children = translateToCascadeTreeNodes(item.children,value);
        }
        nodeArr.push(nodeItem)
    })
    return nodeArr;
}


export const countNodesByType = (list:Array<TreeNode>, targetType:string) => {
    let count = 0;
    function traverse(node) {
        if (node.type === targetType) {
            count++;
        }
        if (node.children && node.children.length > 0) {
            for (const child of node.children) {
                traverse(child);
            }
        }
    }
    for (const rootNode of list) {
        traverse(rootNode);
    }
    return count;
}


export const countNodesByTypes = (list:Array<TreeNode>, targetTypes:Array<string>) => {
    let count = 0;
    function traverse(node) {
        if (targetTypes.includes(node.type)) {
            count++;
        }
        if (node.children && node.children.length > 0) {
            for (const child of node.children) {
                traverse(child);
            }
        }
    }
    for (const rootNode of list) {
        traverse(rootNode);
    }
    return count;
}
