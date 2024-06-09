import {ResultData} from "@/types/insights-common";
import {request} from "@/utils/request";

export async function requestExportStat(data):Promise<ResultData> {
    return request({
        url:'/export/stat',
        method:'POST',
        data,
    })
}