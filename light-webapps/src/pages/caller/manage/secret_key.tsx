import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button, Form, Input, InputTag, Select, Skeleton, Spin, Tag, Icon, Link, Modal, Notification
} from '@arco-design/web-react';
import React, {useEffect, useRef, useState} from 'react';
const { Title } = Typography;
import locale from './locale';
const { Row, Col } = Grid;
const { Text } = Typography;
import styles from './style/index.module.less';
import {requestGetSecretKey} from "@/api/caller";
import useLocale from "@/utils/useLocale";
export default function SecretKeyModal({callerId,onClose}) {

    const t = useLocale(locale);
    const [secretKey,setSecretKey] = useState<string>(null);
    const [loading,setLoading] = useState<boolean>(false);

    const fetchData = async () => {
        setLoading(true);
        await requestGetSecretKey({id:callerId}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setSecretKey(data);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }


    useEffect(()=> {
        fetchData().then();
    },[callerId])

    return (
        <Modal
            title={'调用方秘钥'}
            visible={true}
            footer={null}
            alignCenter={false}
            style={{ width:'750px',top:'250px',display:"grid"}}
            onCancel={onClose}>
            <Spin loading={loading} style={{ width:'100%',display: 'block' }}>
                <Space size={4} direction="vertical" style={{ width: '100%' }}>
                    {secretKey && <div style={{textAlign:"center"}}><Text copyable>{secretKey}</Text></div>}
                </Space>
            </Spin>

        </Modal>
    )
}