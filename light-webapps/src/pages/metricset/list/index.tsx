import React, { useEffect, useState } from 'react';
import {
    Tabs,
    Card,
    Input,
    Typography,
    Grid,
    PaginationProps,
    Pagination,
    Breadcrumb, Notification, Spin, Space, Modal
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import CardBlock from './card-block';
import AddCard from './card-add';
import MetricSetAddPanel from "@/pages/metricset/create";
import {MetricSet, Project, TreeNode} from "@/types/insights-web";
import {requestStarById, requestList, requestUnStarById} from "@/api/metricset";
import {IconHome} from "@arco-design/web-react/icon";
import {requestDeleteById} from "@/api/metricset";
import {useDispatch,useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {updateStoreStaredMetricInfo} from "@/store";
import MetricSetCardBox from "@/pages/metricset/list/MetricSetCardBox";
import {getRandomString} from "@/utils/util";
import {useUpdateEffect} from "ahooks";
import MetricListPanel from "@/pages/metricset/list/list";
import {KeepAlive,useActivate, useAliveController, useUnactivate} from "react-activation";
const { Title } = Typography;
const { Row, Col } = Grid;
import {useHistory } from 'react-router-dom';


export default function Index() {

  const t = useLocale(locale);
    const history = useHistory();
    const { refreshScope } = useAliveController();

    useEffect(() => {
        const action = history.action;
        if(action == 'PUSH'){
            refreshScope('MetricListKeepAlive').then();
        }
    },[])

  return (
      <>
          <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
              <Breadcrumb.Item>
                  <IconHome />
              </Breadcrumb.Item>
              <Breadcrumb.Item style={{fontWeight:20}}>{t['metricSetList.breadcrumb.title']}</Breadcrumb.Item>
          </Breadcrumb>
          <KeepAlive name="MetricListKeepAlive" cacheKey={"MetricListKeepAlive"} id={"MetricListKeepAlive"} autoFreeze={true} when={() => {
              const targetPath = history.location?.pathname;
              return targetPath && (targetPath.startsWith("/metricset/preview"));
          }}>
            <MetricListPanel />
          </KeepAlive>
      </>
  );
}
