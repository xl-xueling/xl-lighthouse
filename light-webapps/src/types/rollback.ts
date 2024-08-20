export enum RollbackTypeEnum{
    VISUALIZATION_DESIGN = 1,
}

export interface Rollback {
    id?:number;
    userId?:number;
    resourceId?:number;
    dataType?:RollbackTypeEnum;
    config?:string;
    version?:number;
    baseFlag?:number;
    createTime?:number;
}