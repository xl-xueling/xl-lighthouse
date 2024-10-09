import React, {useEffect, useRef, useState} from 'react';
import {Department, TreeNode, User} from "@/types/insights-web";
import {Descriptions, Popover} from "@arco-design/web-react";
import {Props} from "@/pages/department/common/depart";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/common/locale";


export default function ProjectLabel({projectInfo}) {

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
                            label: t['projectLabel.label.id'],
                            value: projectInfo?.id,
                        },
                        {
                            label: t['projectLabel.label.title'],
                            value: projectInfo?.title,
                        },
                    ]}
                    labelStyle={{ textAlign: 'right', paddingRight: 15}}
                />
            }>
                <span>{projectInfo?.title}</span>
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