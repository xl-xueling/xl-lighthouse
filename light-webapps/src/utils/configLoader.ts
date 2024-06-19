interface Config {
    REACT_APP_BASE_URL: string;
    AXIOS_TIMEOUT: number;
}

let config: Config | null = null;

export const loadGlobalConfig = async (): Promise<void> => {
    if (!config) {
        const response = await fetch('/config.json');
        config = await response.json();
    }
};

export const getGlobalConfig = async (): Promise<Config> => {
    if (!config) {
        await loadGlobalConfig();
        return config;
    }else{
        return config;
    }
};