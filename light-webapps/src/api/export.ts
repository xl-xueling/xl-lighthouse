import {ResultData} from "@/types/insights-common";
import {request} from "@/utils/request";
import {ExportData} from "@/types/insights-web";

export async function requestExportStat(data):Promise<ResultData<ExportData>> {
    return request({
        url:'/export/stat',
        method:'POST',
        data,
    })
}