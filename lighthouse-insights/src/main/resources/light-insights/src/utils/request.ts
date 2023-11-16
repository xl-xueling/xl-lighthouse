import axios, {AxiosRequestConfig, AxiosResponseHeaders} from 'axios'
import {ResultCode} from "@/types/insights-common";


export const request = async (config): Promise<ResultCode> => {
    const http = axios.create({
        baseURL: '/api/v1',
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




