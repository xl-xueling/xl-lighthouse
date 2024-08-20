import {useCallback, useRef} from 'react';

const useSingleAndDoubleClick = (onSingleClick, onDoubleClick,delay = 300) => {
    const clickTimeout = useRef<any>(null);
    const lastClickTime = useRef(0);

    return useCallback((event) => {
        const currentTime = new Date().getTime();
        const timeDifference = currentTime - lastClickTime.current;
        if (timeDifference < delay) {
            clearTimeout(clickTimeout.current);
            onDoubleClick(event);
            event.stopPropagation();
        } else {
            clickTimeout.current = setTimeout(() => {
                onSingleClick(event);
            }, delay);
        }

        lastClickTime.current = currentTime;
    }, [onSingleClick, onDoubleClick, delay]);
};

export default useSingleAndDoubleClick;
