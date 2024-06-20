import React, {useContext, useEffect, useRef, useState} from 'react';
import dayjs from 'dayjs';
import {
    Form,
    Input,
    DatePicker,
    Button,
    Grid, TreeSelect, Drawer, Descriptions, Divider, Link,
} from '@arco-design/web-react';
import DepartmentLabel from "@/pages/department/common/depart";
import UserGroup from "@/pages/user/common/groups";
const { Row, Col } = Grid;
const { useForm } = Form;

export default function Detail({projectInfo,onClose}) {

    return <div>
        <Drawer
            width={500}
            title={<span>Project Information</span>}
            visible={true}
            onCancel={() => {
                onClose();
            }}
            footer={null}
        >
            <Descriptions
                colon=''
                title=''
                column={1}
                labelStyle={{ width: 100 }}
                data={[
                    {
                        label: 'Name',
                        value: projectInfo?.title,
                    },
                    {
                        label: 'Department',
                        value: <DepartmentLabel departmentId={projectInfo?.departmentId}/>,
                    },
                    {
                        label: 'Admins',
                        value: <UserGroup users={projectInfo?.admins}/>,
                    },
                    {
                        label: 'Description',
                        value: projectInfo?.desc,
                    },
                ]}
            />
        </Drawer>
    </div>
}