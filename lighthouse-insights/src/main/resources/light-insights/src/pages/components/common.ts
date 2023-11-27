
export function validateNode(node,values) {
    if(Array.isArray(node)){
        for(let i=0;i<node.length;i++){
            if(!validateNode(node[i],values)){
                return false;
            }
        }
    }else{
        if (typeof node !== 'object') {
            console.log("--------1")
            return false;
        }
        if (!('label' in node) || !('value' in node) || !isValidValue(node.value,values) || (node.children && !Array.isArray(node.children))) {
            console.log("--------2")
            return false;
        }
        if (node.label === '' || node.value === '') {
            console.log("--------3")
            return false;
        }
        if(node.children){
            for (const child of node.children) {
                if (!validateNode(child,values)) {
                    console.log("--------4")
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