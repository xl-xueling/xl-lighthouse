import axios, {AxiosRequestConfig, AxiosResponseHeaders} from 'axios'
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
        const token = window.localStorage.getItem('token') || window.sessionStorage.getItem('token');
        if (token) {
            config.headers['token'] = token;
        }
        return config;
    }, (error) => {
        console.log('error info:', error.response);
        throw error;
    })

    http.interceptors.response.use((res) => {
        window.localStorage.setItem("token", res.data.token);
        return res;
    }, (error) => {
        console.log('error', error.response);
        throw error;
    })
    return (await http(config)).data;
}




