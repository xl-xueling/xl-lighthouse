import axios, {AxiosResponse} from 'axios'
import {ResultData} from "@/types/insights-common";

export const request = async <T>(config): Promise<ResultData<T>> => {
    let baseURL;
    if(process.env.REACT_APP_ENV == "production"){
        baseURL = process.env.REACT_APP_BASE_URL_PRODUCTION;
    }else if(process.env.REACT_APP_ENV == "development"){
        baseURL = process.env.REACT_APP_BASE_URL_DEVELOPMENT;
    }else if(process.env.REACT_APP_ENV == "simulation"){
        baseURL = process.env.REACT_APP_BASE_URL_SIMULATION;
    }else{
        baseURL = process.env.REACT_APP_BASE_URL_PRODUCTION;
    }

    const http = axios.create({
        baseURL: baseURL + '/api/v1',
        timeout: 5000,
    })

    http.interceptors.request.use((config) => {
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
        if(error.response.status == 401){
            const refreshKey = localStorage.getItem('refreshKey')
            const refreshResponse = await axios.get(baseURL+'/api/v1/refreshKey',{
                headers: {
                    'refreshKey': refreshKey,
                }
            })
            const refreshResult = refreshResponse.data;
            if(refreshResult.code != '0'){
                window.location.href = "/login";
            }else{
                localStorage.setItem('accessKey',refreshResult.data.accessKey);
            }
            const dataResponse = await http.request(config);
            result = dataResponse.data;
        }
    }
    return result;
}




