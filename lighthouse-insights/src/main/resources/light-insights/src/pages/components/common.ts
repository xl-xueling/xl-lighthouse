
export function validateNode(node,values) {
    if(Array.isArray(node)){
        for(let i=0;i<node.length;i++){
            if(!validateNode(node[i],values)){
                return false;
            }
        }
    }else{
        if (typeof node !== 'object') {
            return false;
        }
        if (!('label' in node) || !('value' in node) || !isValidValue(node.value,values) || (node.children && !Array.isArray(node.children))) {
            return false;
        }
        if (node.label === '' || node.value === '') {
            return false;
        }
        if(node.children){
            for (const child of node.children) {
                if (!validateNode(child,values)) {
                    return false;
                }
            }
        }
    }
    return true;
}


function isValidValue(value, values) {
    if (values.has(value)) {
        return false;
    }
    values.add(value);
    return true;
}