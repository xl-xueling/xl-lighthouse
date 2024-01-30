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

export function blockMainThread(seconds) {
    const delay = seconds * 1000;
    const start = Date.now();
    while (Date.now() - start < delay) {}
}


export function formatString(format, ...args) {
    return format.replace(/%s/g, function() {
        return args.shift();
    });
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

export function validateWithRegex(regex, input) {
    return regex.test(input);
}

export function getRandomString(len = 32) {
    const $chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890';
    const maxPos = $chars.length;
    let text = '';
    for (let index = 0; index < len; index++) {
        text += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return text;
}

export function formatTimeStampBackUp(timestamp) {
    if(!timestamp){
        return null;
    }
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}


export function areJsonObjectsEqual(obj1, obj2) {
    if (typeof obj1 !== 'object' || typeof obj2 !== 'object') {
        console.log("------1")
        return false;
    }
    const keys1 = Object.keys(obj1);
    const keys2 = Object.keys(obj2);
    if (keys1.length !== keys2.length) {
        console.log("------2,key1s is:" + JSON.stringify(keys1) + ",key2s is:" + JSON.stringify(keys2));
        console.log("-----2,obj1 is:" + JSON.stringify(obj1) + ",obj2 is:" + JSON.stringify(obj2));
        return false;
    }
    for (const key of keys1) {
        if (!obj2.hasOwnProperty(key)) {
            console.log("------3")
            return false;
        }
        if (!areValuesEqual(obj1[key], obj2[key])) {
            // console.log("------4,key1:" + JSON.stringify(obj1[key]) + ",key2:"+JSON.stringify(obj2[key]))
            return false;
        }
    }
    return true;
}

function areValuesEqual(value1, value2) {
    if (typeof value1 !== typeof value2) {
        return false;
    }
    if (typeof value1 === 'object' && value1 !== null) {
        return areJsonObjectsEqual(value1, value2);
    } else {
        return value1 === value2;
    }
}



