import {request} from "@/utils/request";

export async function registerRequest(data)  {
    return request({
        url:'/user/register',
        method:'POST',
        data,
    })
}