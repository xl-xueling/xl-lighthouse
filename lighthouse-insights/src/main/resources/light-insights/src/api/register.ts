import {request} from "@/utils/request";

export async function requestRegister(data)  {
    return request({
        url:'/user/register',
        method:'POST',
        data,
    })
}