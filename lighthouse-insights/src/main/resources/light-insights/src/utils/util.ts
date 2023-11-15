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



