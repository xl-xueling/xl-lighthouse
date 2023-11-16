import {Department} from "@/types/insights-web";
import {queryAll as queryDepartmentAll} from "@/api/department";
import {Message} from "@arco-design/web-react";

export async function fetchAllData(): Promise<Array<Department>> {
    let result = null;
    try {
        await queryDepartmentAll().then((res:any) => {
            const {code, msg} = res;
            const data = res.data;
            if (code === '0' && data) {
                result = data;
            }else{
                Message.error("System Error,fetch department data failed!")
            }
        });
    } catch (error) {
        console.error("error is:" + error);
        Message.error("System Error,fetch department data failed!")
    }
    return result;
}