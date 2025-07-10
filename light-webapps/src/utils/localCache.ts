export const getDataWithLocalCache = async <T>(key: string, seconds: number, callback: () => Promise<T>, storageType = 'sessionStorage'): Promise<T> => {
    const storage = localStorage;
    let result;
    const cachedData = storage.getItem(key);
    if (cachedData) {
        const {data, timestamp} = JSON.parse(cachedData);
        const now = Date.now();
        if (now - timestamp <= seconds * 1000) {
            result = data;
        } else {
            storage.removeItem(key);
        }
    }
    if(!result){
        result = await callback();
        const cachedData = {
            data:result,
            timestamp: Date.now()
        };
        storage.setItem(key, JSON.stringify(cachedData));
    }
    return result;
}

export const clearLocalCache = (key:string):void => {
    localStorage.removeItem(key);
}

export function setLocalStorageWithExpiry(key: string, data: any, seconds: number) {
    const now = Date.now();
    const item = {
        value: data,
        expiry: now + seconds * 1000,
    };
    localStorage.setItem(key, JSON.stringify(item));
}

export function getLocalStorageWithExpiry<T = any>(key: string): T | null {
    const itemStr = localStorage.getItem(key);
    if (!itemStr) return null;
    try {
        const item = JSON.parse(itemStr);
        const now = Date.now();
        if (item.expiry && now > item.expiry) {
            localStorage.removeItem(key);
            return null;
        }
        return item.value as T;
    } catch {
        localStorage.removeItem(key);
        return null;
    }
}