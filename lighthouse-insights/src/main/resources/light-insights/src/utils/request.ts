import axios, {AxiosRequestConfig, AxiosResponseHeaders} from 'axios'

export const request = (config) => {
    const http = axios.create({
        baseURL:'/api/v1',
        timeout:5000,
    })
    http.interceptors.request.use((config) => {
        return config;
    },(error) => {
        console.log('error',error.response);
    })

    http.interceptors.response.use((res) => {
        return res.data;
    },(error) => {
        console.log('error',error.response);
    })
    return http(config);
}