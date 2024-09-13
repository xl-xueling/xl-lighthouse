export enum RollbackTypeEnum{
    VISUALIZATION_DESIGN = 1,
}

export interface Rollback {
    id?:number;
    userId?:number;
    resourceId?:number;
    state?:number;
    dataType?:RollbackTypeEnum;
    config?:string;
    version?:number;
    createTime?:number;
}