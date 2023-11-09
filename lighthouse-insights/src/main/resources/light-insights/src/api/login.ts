import {request} from "@/utils/request";

export async function loginRequest(data)  {
    return request({
        url:'/user/login',
        method:'POST',
        data,
    })
}