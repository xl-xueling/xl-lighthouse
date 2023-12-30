import React, {useEffect, useRef, useState} from 'react';
import {User} from "@/types/insights-web";
import {Descriptions, Popover} from "@arco-design/web-react";

export interface Props {
    users: Array<User>;
}

export default function UserGroup(props:Props) {
    const { users } = props;
    return (
        <>
        {
            users?.map((option,index) => {
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
                                        value: option.username,
                                    },
                                    {
                                        label: 'email',
                                        value: option.email,
                                    },
                                    {
                                        label: 'phone',
                                        value: option.phone,
                                    }
                                ]}
                                labelStyle={{ textAlign: 'right', paddingRight: 15}}
                            />
                        }
                    >
                        <span key={index} style={{marginRight:'2px'}}>{option.username};</span>
                    </Popover>
                );
            })
        }
        </>
    );

}