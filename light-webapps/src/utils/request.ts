import axios, {AxiosResponse} from 'axios'
import {ResultData} from "@/types/insights-common";
import {removeLoginStatus} from "@/utils/checkLogin";
import {Notification} from "@arco-design/web-react";
import { getGlobalConfig } from './configLoader';

export const request = async <T>(config): Promise<ResultData<T>> => {
    const envConfig = await getGlobalConfig().then();
    let baseURL = envConfig.REACT_APP_BASE_URL;
    const http = axios.create({
        baseURL: baseURL + '/api/v1',
        timeout: envConfig.AXIOS_TIMEOUT,
    })
    http.interceptors.request.use((config) => {
        const language = localStorage.getItem('arco-lang');
        if (language) {
            config.headers['Accept-Language'] = language;
        }else{
            config.headers['Accept-Language'] = 'en-US';
        }
        config.headers['accessKey'] = window.localStorage.getItem('accessKey');
        return config;
    }, (error) => {
        console.log(error);
    })

    let result;
    try{
        const response: AxiosResponse = await http.request(config);
        result = response.data;
    }catch (error) {
        if(error.message == 'Network Error'){
            console.log("Remote BaseUrl:" + (baseURL + '/api/v1'));
            console.log(error);
            Notification.error({style: { width: 420 }, title: 'Warning', content:'Unable to connect to remote server!'});
            result = {
                code: error.response?error.response.status:'503',
            };
        }else if(error.response?.status == 401){
            const refreshKey = localStorage.getItem('refreshKey')
            if(!refreshKey){
                removeLoginStatus();
                window.location.href = "/login";
            }
            const refreshResponse = await axios.get(baseURL+'/api/v1/refreshKey',{
                headers: {
                    'refreshKey': refreshKey,
                }
            })
            const refreshResult = refreshResponse.data;
            if(refreshResult.code != '0'){
                removeLoginStatus();
                window.location.href = "/login";
            }else{
                localStorage.setItem('accessKey',refreshResult.data.accessKey);
            }
            const dataResponse = await http.request(config);
            console.log("refreshKey again,dataResponse:" + dataResponse);
            result = dataResponse.data;
        }else{
            result = {
                code: error.response?error.response.status:'500',
            };
        }
    }
    return result;
}

export interface CallerInfo {
    callerName?:string,
    callerKey?:string,
}

export interface EnvConfig {
    REACT_APP_BASE_URL?:string,
    AXIOS_TIMEOUT?:number,
}

export const callerRequest = async <T>(callerInfo:CallerInfo,envConfig:EnvConfig,config): Promise<ResultData<T>> => {
    let baseURL = envConfig.REACT_APP_BASE_URL;
    const http = axios.create({
        baseURL: baseURL + '/api/v1',
        timeout: envConfig.AXIOS_TIMEOUT,
    })

    http.interceptors.request.use((config) => {
        config.headers['Accept-Language'] = 'en-US';
        config.headers['Caller-Name'] = callerInfo.callerName;
        config.headers['Caller-Key'] = callerInfo.callerKey;
        return config;
    }, (error) => {
        console.log(error);
    })

    let result;
    try{
        const response: AxiosResponse = await http.request(config);
        result = response.data;
    }catch (error) {
        console.log("Error is:" + error);
    }
    return result;
}



