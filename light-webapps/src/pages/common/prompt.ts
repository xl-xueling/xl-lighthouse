import { useHistory } from 'react-router-dom';
import {ResultData} from "@/types/insights-common";
import {Message, Notification} from "@arco-design/web-react";

export const handleWarningCode = (errorCode, message, history: any) => {
    if (errorCode === '403') {
        history.push('/exception/403');
    }else if (errorCode === '404') {
        history.push('/exception/404');
    }else if(errorCode === '500'){
        history.push('/exception/500');
    }else {
        Notification.warning({style: { width: 420 }, title: 'Warning', content: message});
    }
};