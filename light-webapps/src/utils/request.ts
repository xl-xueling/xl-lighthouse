import axios, {AxiosResponse} from 'axios'
import {ResultData} from "@/types/insights-common";
import {removeLoginStatus} from "@/utils/checkLogin";
import {Message, Notification} from "@arco-design/web-react";

export const request = async <T>(config): Promise<ResultData<T>> => {
    let baseURL;
    if(process.env.REACT_APP_ENV == "simulation"){
        baseURL = 'http://119.91.203.220:9089'
    }else{
        baseURL = window['GlobalConfig'].REACT_APP_BASE_URL;
    }
    const http = axios.create({
        baseURL: baseURL + '/api/v1',
        timeout: 180000,
    })

    http.interceptors.request.use((config) => {
        const language = localStorage.getItem('arco-lang');
        if (language) {
            config.headers['Accept-Language'] = language;
        }else{
            config.headers['Accept-Language'] = 'en-US';
        }
        if(process.env.REACT_APP_ENV == "simulation"){
            if(config.url == '/data/stat'){
                config.url = '/test-data/stat'
            }
            if(config.url == '/data/limit'){
                config.url = '/test-data/limit'
            }
            if(config.url == '/stat/queryById'){
                config.url = '/stat/testQueryById'
            }
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




