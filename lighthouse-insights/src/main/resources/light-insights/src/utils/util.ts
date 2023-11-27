import {ArcoTreeNode} from "@/types/insights-common";
import {DepartmentArcoTreeNode} from "@/types/insights-web";

export function stringifyObj(obj): string {
    let cache = [];
    const str = JSON.stringify(obj, function(key, value) {
        if (typeof value === "object" && value !== null) {
            if (cache.indexOf(value) !== -1) {
                return;
            }
            cache.push(value);
        }
        return value;
    });
    cache = null;
    return str;
}

export function stringifyMap(map): string {
    return JSON.stringify(Array.from(map.entries()));
}

export function isJSON(str):boolean {
    if (typeof str == 'string') {
        try {
            const obj = JSON.parse(str);
            return !!(typeof obj == 'object' && obj);
        } catch(e) {
            return false;
        }
    }
}


export function getTextBlenLength(str){
    let len = 0;
    for(let i=0;i<str.length;i++){
        if(str.charAt(i).match(/[\u4e00-\u9fa5]/g) != null) len+=2;
        else len += 1;
    }
    return len;
}




