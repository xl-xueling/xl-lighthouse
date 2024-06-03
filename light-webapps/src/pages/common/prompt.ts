import { useRouter } from 'next/router';
import {Notification} from "@arco-design/web-react";

export const handleWarningCode = (errorCode, message, history: any) => {
    const router = useRouter();
    if (errorCode === '403') {
        router.push('/exception/403');
    }else if (errorCode === '404') {
        router.push('/exception/404');
    }else if(errorCode === '500'){
        router.push('/exception/500');
    }else {
        Notification.warning({style: { width: 420 }, title: 'Warning', content: message});
    }
};