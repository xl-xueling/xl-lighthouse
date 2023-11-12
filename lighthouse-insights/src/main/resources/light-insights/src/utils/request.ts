import axios, {AxiosRequestConfig, AxiosResponseHeaders} from 'axios'

export const request = (config) => {
    const http = axios.create({
        baseURL:'/api/v1',
        timeout:5000,
    })
    http.interceptors.request.use((config) => {
        console.log("start to request,config:" + JSON.stringify(config));
        const token = window.localStorage.getItem('token') || window.sessionStorage.getItem('token');
        if (token) {
            config.headers['token'] = token;
        }
        return config;
    },(error) => {
        console.log('error info:',error.response);
        throw error;
    })

    http.interceptors.response.use((res) => {
        console.log("start to response,res:" + JSON.stringify(res));
        window.localStorage.setItem("token",res.data.token);
        return res.data;
    },(error) => {
        console.log('error',error.response);
        throw error;
    })
    return http(config);
}