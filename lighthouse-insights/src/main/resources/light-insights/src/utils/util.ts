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

export function formatTimeStamp(timestamp) {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}



