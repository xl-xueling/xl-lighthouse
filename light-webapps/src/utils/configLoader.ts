interface Config {
    REACT_APP_BASE_URL: string;
    AXIOS_TIMEOUT: number;
}

let config: Config | null = null;

const loadGlobalConfig = async (configPath = null): Promise<void> => {
    if (!config) {
        if(configPath){
            const response = await fetch(configPath);
            config = await response.json();
        }else{
            const response = await fetch('/config.json');
            config = await response.json();
        }
    }
};

export const getGlobalConfig = async (configPath = null): Promise<Config> => {
    if (!config) {
        await loadGlobalConfig(configPath);
        return config;
    }else{
        return config;
    }
};