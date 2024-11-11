import React, {useEffect, useRef, useState} from 'react';
import {Department, TreeNode, User} from "@/types/insights-web";
import {Descriptions, Popover, Typography} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/stat/common/locale";


export default function StatLabel({statInfo,size = 'default'}) {

    const t = useLocale(locale);

    const getLabel = () => {
        return (
            <Popover
                style={{maxWidth:'500px'}}
                trigger={"click"} content={
                    <Descriptions
                        size={"mini"}
                        colon={":"}
                        column={1}
                        data={[
                            {
                                label: 'ID',
                                value: statInfo?.id,
                            },
                            {
                                label: t['statLabel.label.project'],
                                value: statInfo?.projectTitle,
                            },
                            {
                                label: t['statLabel.label.group'],
                                value: statInfo?.token,
                            },
                        ]}
                        labelStyle={{ textAlign: 'right', paddingRight: 15}}
                    />
            }>
                <Typography.Text style={{fontSize:size == 'mini'? 12 : 14}}>{statInfo?.title}</Typography.Text>
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