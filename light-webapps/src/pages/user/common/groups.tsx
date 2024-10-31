import React, {useEffect, useRef, useState} from 'react';
import {Department, TreeNode, User} from "@/types/insights-web";
import {Descriptions, Popover} from "@arco-design/web-react";
import {useSelector} from "react-redux";
import {getFullPathNodes} from "@/pages/department/base";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/user/common/locale";

export interface Props {
    users: Array<User>;
}

export default function UserGroup(props:Props) {
    const { users } = props;
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const t = useLocale(locale);

    return (
        <>
        {
            users?.filter(x => x != null).map((option,index) => {
                const fullPathNodes:TreeNode[] = getFullPathNodes(option?.departmentId,allDepartInfo);
                return (
                    <Popover
                        key={index}
                        trigger='click'
                        content={
                            <Descriptions
                                size={"mini"}
                                colon={":"}
                                column={1}
                                data={[
                                    {
                                        label: t['userGroups.label.user'],
                                        value: option?.username,
                                    },
                                    {
                                        label: t['userGroups.label.email'],
                                        value: option?.email,
                                    },
                                    {
                                        label: t['userGroups.label.phone'],
                                        value: option?.phone ? option?.phone : '--',
                                    },
                                    {
                                        label: t['userGroups.label.department'],
                                        value: fullPathNodes.map(z => z?.label).join(" > "),
                                    }
                                ]}
                                labelStyle={{ textAlign: 'right', paddingRight: 15}}
                            />
                        }
                    >
                        <span key={index} style={{marginRight:'2px'}}>{option?.username};</span>
                    </Popover>
                );
            })
        }
        </>
    );

}