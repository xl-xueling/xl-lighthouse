import React, {useEffect, useRef, useState} from 'react';
import {Department, User} from "@/types/insights-web";
import {Descriptions, Popover} from "@arco-design/web-react";
import {useSelector} from "react-redux";
import {getCascadeDepartment, getDepartment} from "@/pages/department/common";

export interface Props {
    users: Array<User>;
}

export default function UserGroup(props:Props) {
    const { users } = props;
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);

    return (
        <>
        {
            users?.map((option,index) => {
                const currentDepartment = getDepartment(option?.departmentId,allDepartInfo);
                const cascadeDepartments:Department[] = getCascadeDepartment(currentDepartment,allDepartInfo);
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
                                        label: 'user',
                                        value: option?.username,
                                    },
                                    {
                                        label: 'email',
                                        value: option?.email,
                                    },
                                    {
                                        label: 'phone',
                                        value: option?.phone,
                                    },
                                    {
                                        label: 'department',
                                        value: cascadeDepartments.map(z => z?.name).join(" > "),
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