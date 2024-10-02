import React, { useState, useCallback } from "react";
import KeepAlive from "react-activation";
import { Button } from "@arco-design/web-react";

export default function TempIndex() {
    const [show, setShow] = useState(true);

    const toggle = () => setShow((prev) => !prev);

    // 使用 useCallback 确保 Test 组件引用稳定
    const Test = useCallback(() => {
        const [count, setCount] = useState(0);
        return (
            <div>
                <p>count: {count}</p>
                <Button onClick={() => setCount((count) => count + 1)} type="primary">
                    Add
                </Button>
            </div>
        );
    }, []);

    return (
        <>
            {show && (
                <KeepAlive name="Test" cacheKey={"TestCacheKey"} id={"TestId"} when={true} autoFreeze={true}>
                    <Test />
                </KeepAlive>
            )}
            <Button onClick={toggle}>显示/隐藏</Button>
        </>
    );
}
