import React, {useEffect, useMemo, useState} from 'react';
import {
    Breadcrumb,
    Button,
    Card,
    Grid, Input,
    Message,
    Notification,
    PaginationProps,
    Radio,
    Space, Spin,
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import {IconHome} from "@arco-design/web-react/icon";
import locale from "./locale";
import CreateCallerModal from "@/pages/caller/create/CreateCallerModal";
const { Row, Col } = Grid;

export default function Index() {

    const t = useLocale(locale);
    const [showCreateModal,setShowCreateModal] = useState<boolean>(false);

    const handlerSearch = async () => {
        console.log("---")
    }

    const handlerCreate = async () => {
        setShowCreateModal(true);
    }

    return (
        <>
            <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
                <Breadcrumb.Item>
                    <IconHome />
                </Breadcrumb.Item>
                <Breadcrumb.Item style={{fontWeight:20}}>{t['callerList.breadcrumb.title']}</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
                <Grid.Row>
                    <Grid.Col span={16}>
                        <Input.Search
                            size={"small"}
                            style={{ width: '280px',paddingRight:'24px'}} allowClear={true} onSearch={handlerSearch}
                        />
                    </Grid.Col>
                    <Grid.Col span={8} style={{textAlign:"right"}}>
                        <Button type={"primary"} size={"mini"} onClick={handlerCreate}>创建</Button>
                    </Grid.Col>
                </Grid.Row>
            </Card>
            {showCreateModal && <CreateCallerModal onClose={() => setShowCreateModal(false)} />}
        </>
    );
}