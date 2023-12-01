import React from 'react';
import {Form, Input, Modal, Radio, TreeSelect} from "@arco-design/web-react";
import UserTermQuery from "@/pages/user/common/userTermQuery";
import {translate, translateToTreeStruct} from "@/pages/department/common";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";

function ProjectCreatePanel({onHide,allDepartInfo}){

    const t = useLocale(locale);

    function handlerSubmit(){
        console.log("create submit!")
    }

    return (
        <Modal
            title='创建工程'
            style={{ width:'650px' }}
            visible={true}
            className='modal-demo-without-content-spacing'
            onOk={handlerSubmit}
            onCancel={() => onHide()}
        >
            <div>
                <Form
                    autoComplete='off'
                    scrollToFirstError
                >
                    <Form.Item label='Name' field='name' rules={[{ required: true }]}>
                        <Input placeholder='please enter...' />
                    </Form.Item>
                    <Form.Item label={t['projectList.columns.department']} field="department" rules={[{ required: true }]}>
                        <TreeSelect
                            placeholder={"Please select"}
                            multiple={true}
                            allowClear={true}
                            treeData={translate(allDepartInfo)}
                        />
                    </Form.Item>
                    <Form.Item label={'Description'} field="desc" rules={[{ required: true }]}>
                        <Input.TextArea placeholder='Please enter ...' style={{ minHeight: 64}} />
                    </Form.Item>
                    <Form.Item label={'IsPrivate'} field="isPrivate" rules={[{ required: true }]}>
                        <Radio.Group defaultValue='a' style={{ marginBottom: 20 }}>
                            <Radio value='0'>Private</Radio>
                            <Radio value='1'>Public</Radio>
                        </Radio.Group>
                    </Form.Item>
                    <Form.Item label={'Admins'} field="admins" rules={[{ required: true }]}>
                        <UserTermQuery/>
                    </Form.Item>
                </Form>
            </div>
        </Modal>
    );
}

export default ProjectCreatePanel;