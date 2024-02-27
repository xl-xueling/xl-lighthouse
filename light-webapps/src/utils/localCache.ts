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

const clearLocalCache = (key:string):void => {
    localStorage.removeItem(key);
}