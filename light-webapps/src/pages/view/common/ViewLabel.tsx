import React, {useEffect, useRef, useState} from 'react';
import {Department, TreeNode, User} from "@/types/insights-web";
import {Descriptions, Popover} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/view/common/locale";

export default function ViewLabel({viewInfo}) {

    const t = useLocale(locale);

    const getLabel = () => {
        return (
            <Popover trigger={"click"} content={
                <Descriptions
                    size={"mini"}
                    colon={":"}
                    column={1}
                    data={[
                        {
                            label: t['viewLabel.label.id'],
                            value: viewInfo?.id,
                        },
                        {
                            label: t['viewLabel.label.title'],
                            value: viewInfo?.title,
                        },
                    ]}
                    labelStyle={{ textAlign: 'right', paddingRight: 15}}
                />
            }>
                <span>{viewInfo?.title}</span>
            </Popover>
        );
    }

    return (
        <>
            {
                getLabel()
            }
        </>
    )
}