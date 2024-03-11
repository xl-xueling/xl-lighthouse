import React, {useEffect, useState} from 'react';
import {Card, Grid, Notification, Space} from '@arco-design/web-react';
import Overview from './overview';
import Shortcuts from './shortcuts';
import styles from './style/index.module.less';
import './mock';
import {useSelector} from "react-redux";
import {HomeData, MetricSet, Stat} from "@/types/insights-web";
import MetricSetCardBox from "@/pages/metricset/list/MetricSetCardBox";
import {requestQueryById} from "@/api/stat";
import {requestOverView} from "@/api/home";
import {ResultData} from "@/types/insights-common";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/dashboard/workplace/locale";
import StatPieChart from "@/pages/dashboard/workplace/StatPieChart";
import {getDataWithLocalCache} from "@/utils/localCache";
const { Row, Col } = Grid;
const gutter = 16;

export const HomePageContext = React.createContext(null)

function Workplace() {
    const t = useLocale(locale);
  const staredMetricInfo = useSelector((state: {staredMetricInfo:Array<MetricSet>}) => state.staredMetricInfo);

  const [listData,setListData] = useState<MetricSet[]>(staredMetricInfo.slice(0,8));

    const [homeData, setHomeData] = useState<HomeData>({});
    const [statInfo,setStatInfo] = useState<Stat>(null);
    const [loading, setLoading] = useState(true);

    async function actualFetchStatInfo():Promise<Stat> {
        return new Promise<Stat>((resolve,reject) => {
            requestQueryById({id:1011}).then((response) => {
                resolve(response.data);
            }).catch((error) => {
                reject(error);
            })
        })
    }

    async function fetchStatInfo() {
        const statInfo = await getDataWithLocalCache('cache_cluster_monitor_1011',300,actualFetchStatInfo);
        setStatInfo(statInfo);
    }

    const fetchHomeData = async () => {
        setLoading(true);
        await requestOverView().then((response:ResultData) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setHomeData(data)
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error)
        }).finally(() => {
            setLoading(false);
        })
    };

    useEffect(() => {
        fetchHomeData().then();
        fetchStatInfo().then();
    }, []);
  useEffect(() => {
      setListData(staredMetricInfo.slice(0,8))
  },[staredMetricInfo])

  return (
      <>
          <HomePageContext.Provider value={{homeData,statInfo}}>
    <div className={styles.wrapper}>
        <Space size={16} direction="vertical" className={styles.left}>
            <Overview />
        </Space>

        <Space size={16} direction="vertical" className={styles.right}>
            <Shortcuts />
            <Card style={{padding:0}}>
                <StatPieChart />
            </Card>
        </Space>
    </div>
      <Row gutter={gutter}>
          {listData.map((item, index) => (
              <Col span={6} key={index}>
                  <MetricSetCardBox key={index} item={item}/>
              </Col>
          ))}
      </Row>
          </HomePageContext.Provider>
      </>
  );
}

export default Workplace;
