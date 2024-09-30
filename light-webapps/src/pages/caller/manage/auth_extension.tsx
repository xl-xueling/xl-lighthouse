import React, {useRef} from 'react';
import {Form, Input, Modal, Select, Spin} from "@arco-design/web-react";
import {AuthRecord} from "@/types/caller";
import {LabelValue, ResourceTypeEnum} from "@/types/insights-common";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/caller/manage/locale";
const FormItem = Form.Item;
const Option = Select.Option;
const TextArea = Input.TextArea;

export interface Props {
    authRecord?:AuthRecord;
    onClose?: () => void;
}

const AuthExtension:React.FC<Props> = ({
                                           authRecord,
                                           onClose = null}) => {

    const periodOptions:Array<LabelValue> = [{label:'一个月',value:2592000},{label:'三个月',value:7776000}
        ,{label:'六个月',value:15552000},{label:'一年',value:31104000}]

    const formRef = useRef(null);

    const t = useLocale(locale);

    const handleSubmit = () => {

    }

    const getFormItems = () => {
        if(authRecord.resourceType == ResourceTypeEnum.Project){
            const project = authRecord?.extend;
            return <Form ref={formRef} initialValues={{extension:2592000,project:authRecord.extend.id}}>
                <Form.Item field='project' label={'统计工程'} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select disabled={true}>
                        <Option key={authRecord.extend.id} value={authRecord.extend.id}>
                            {project?.title + ' [' +project?.id+ ']'}
                        </Option>
                    </Select>
                </Form.Item>
                <Form.Item field={'extension'} label={'续签时长'} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select
                        placeholder='Select period' defaultValue={periodOptions[0].value}>
                        {periodOptions.map((option, index) => (
                            <Option key={option.value} value={option.value}>
                                {option.label}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item field={'reason'} label={'续签原因'} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <TextArea placeholder='Enter something' style={{ minHeight: 64 }} />
                </Form.Item>
            </Form>
        }else if(authRecord.resourceType == ResourceTypeEnum.Stat){
            const stat = authRecord?.extend;
            return <Form ref={formRef} initialValues={{extension:2592000,project:authRecord.extend.id}}>
                <Form.Item field='project' label={'统计项'} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select disabled={true}>
                        <Option key={authRecord.extend.id} value={authRecord.extend.id}>
                            {stat?.projectTitle + ' > ' + stat?.token + ' > ' + stat?.title + " ["+stat?.id+"]"}
                        </Option>
                    </Select>
                </Form.Item>
                <Form.Item field={'extension'} label={'续签时长'} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select
                        placeholder='Select period' defaultValue={periodOptions[0].value}>
                        {periodOptions.map((option, index) => (
                            <Option key={option.value} value={option.value}>
                                {option.label}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item field={'reason'} label={'续签原因'} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <TextArea placeholder='Enter something' style={{ minHeight: 64 }} />
                </Form.Item>
            </Form>
        }else if(authRecord.resourceType == ResourceTypeEnum.View){
            const view = authRecord?.extend;
            return <Form ref={formRef} initialValues={{extension:2592000,project:authRecord.extend.id}}>
                <Form.Item field='view' label={'数据视图'} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select disabled={true}>
                        <Option key={view?.id} value={view?.id}>
                            {view?.title + ' [' +view?.id+ ']'}
                        </Option>
                    </Select>
                </Form.Item>
                <Form.Item field={'extension'} label={'续签时长'} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select
                        placeholder='Select period' defaultValue={periodOptions[0].value}>
                        {periodOptions.map((option, index) => (
                            <Option key={option.value} value={option.value}>
                                {option.label}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item field={'reason'} label={'续签原因'} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <TextArea placeholder='Enter something' style={{ minHeight: 64 }} />
                </Form.Item>
            </Form>
        }
    }

    return (
        <Modal
            visible={true}
            alignCenter={false}
            style={{ width:'1000px',maxWidth:'80%',verticalAlign:'top', top: '150px' }}
            onCancel={onClose}
            onOk={handleSubmit}
            title='授权续签'
            autoFocus={false}
            focusLock={true}
        >
            {getFormItems()}
        </Modal>
    );
}

export default AuthExtension;