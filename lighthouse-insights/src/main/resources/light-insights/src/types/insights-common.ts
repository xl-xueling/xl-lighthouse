export interface ResultData<S = any> {
    code: string;
    message?: string;
    data?:S ;
}