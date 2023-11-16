export const getDataWithLocalCache = async <T>(key: string, seconds: number, callback: () => Promise<T>): Promise<T> => {
    let result;
    const cachedData = sessionStorage.getItem(key);
    if (cachedData) {
        const {data, timestamp} = JSON.parse(cachedData);
        const now = Date.now();
        if (now - timestamp <= seconds * 1000) {
            result = data;
        } else {
            sessionStorage.removeItem(key);
        }
    }
    if(!result){
        result = await callback();
        const cachedData = {
            result,
            timestamp: Date.now()
        };
        sessionStorage.setItem(key, JSON.stringify(cachedData));
    }
    return result;
}

const clearLocalCache = (key:string):void => {
    sessionStorage.removeItem(key);
}