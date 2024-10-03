import React, {useEffect, useState} from 'react';
import cs from 'classnames';
import {Button, Card, Descriptions, Popconfirm, Space, Tag, Typography} from '@arco-design/web-react';
import {
    IconCamera,
    IconFaceSmileFill,
    IconPenFill,
    IconPushpin, IconStar,
    IconStarFill,
    IconSunFill,
    IconThumbUpFill,
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {MetricSet} from "@/types/insights-web";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import {useHistory,Link} from 'react-router-dom';
import {getRandomString} from "@/utils/util";
import {getLockIcon, getTreeResourceIcon} from "@/pages/common/desc/base";
import {PermissionEnum} from "@/types/insights-common";
import {useSelector} from "react-redux";
import { HiMiniStar } from "react-icons/hi2";
import { FaRegUser } from "react-icons/fa";
import {TbBrandVisualStudio} from "react-icons/tb";
import {LuLayers} from "react-icons/lu";

const { Meta } = Card;


interface CardBlockType {
  item: MetricSet;
  loading?: boolean;
  from?:string;
  callback;
  size?:string;
}

const IconList = [
    IconStarFill,
    LuLayers,
].map((Tag, index) => <Tag size={13} style={{marginBottom:'0.6px',marginTop:'4px'}} key={index} />);

const { Paragraph } = Typography;

function CardBlock(props: CardBlockType) {
  const {item ,callback,size,from } = props;
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(props.loading);
  const history = useHistory();

  const t = useLocale(locale);
  const staredMetricInfo = useSelector((state: {staredMetricInfo:Array<MetricSet>}) => state.staredMetricInfo);

  useEffect(() => {
    setLoading(props.loading);
  }, [props.loading]);

  const getContent = () => {
    return (
      <Descriptions
        column={2}
        data={[
          { label: '', value: item?.desc },
        ]}
      />
    );
  };


  const className = cs(styles['card-block']);

  const handleClick = (e) => {
      e.stopPropagation();
      e.preventDefault();
      history.push(`/metricset/preview/${item?.id}`);
  };

  return (
      <Link to={`/metricset/preview/${item.id}`} style={{ textDecoration: 'none'}}>
          <Card
              bordered={true}
              className={className}
              size="small"
              style={{ cursor: 'pointer', height: size === 'small' ? '150px' : '190px' }}
              actions={
                  item.permissions.includes(PermissionEnum.AccessAble) ? [
                      <span key={3} className='icon-hover' onClick={handleClick}>
                            <Button type={"secondary"} size={"mini"}>{t['basic.form.button.preview']}</Button>
                        </span>,
                  ] : null
              }
              title={
                  <div>
                      <div className={cs(styles.title, {
                          [styles['title-more']]: visible,
                      })}>
                            <span onClick={(e) => {e.stopPropagation();e.preventDefault(); }}>
                                {staredMetricInfo.map(z => z.id).includes(item.id) ?
                                    <Popconfirm

                                        position={"bl"}
                                        title='Confirm'
                                        content={t['metricSetList.operations.unstar.confirm']}
                                        onOk={async (e) => {await callback('unstar',item)}}
                                    >
                                        <Button type={"primary"} icon={<LuLayers style={{marginTop:'3px'}} size={13}/>} shape={"circle"} size={"mini"} style={{marginRight:'8px'}}/>
                                    </Popconfirm>
                                    :
                                    <Popconfirm

                                        position={"bl"}
                                        title='Confirm'
                                        content={t['metricSetList.operations.star.confirm']}
                                        onOk={async (e) => {await callback('star',item)}}
                                    >
                                        <Button icon={<LuLayers style={{marginTop:'3px'}} size={13}/>} shape={"circle"} size={"mini"} style={{marginRight:'8px'}}/>
                                    </Popconfirm>
                                }
                            </span>
                          <span>
                                <span style={{ display: "inline-flex", alignItems: "center" }}>{item.title}{getLockIcon(t, item.privateType, item.permissions)}</span>
                            </span>
                          {/*<div onClick={(e) => { e.stopPropagation(); e.preventDefault();}} className={styles.more}>*/}
                          {/*    {staredMetricInfo.map(z => z.id).includes(item.id) ?*/}
                          {/*        <Popconfirm*/}
                          {/*            focusLock*/}
                          {/*            position={"br"}*/}
                          {/*            title='Confirm'*/}
                          {/*            content={t['metricSetList.operations.unstar.confirm']}*/}
                          {/*            onOk={async (e) => { await callback('unstar', item); }}*/}
                          {/*            onVisibleChange={setVisible}*/}
                          {/*        >*/}
                          {/*            <IconStarFill/>*/}
                          {/*        </Popconfirm>*/}
                          {/*        :*/}
                          {/*        <Popconfirm*/}
                          {/*            focusLock*/}
                          {/*            position={"br"}*/}
                          {/*            title='Confirm'*/}
                          {/*            content={t['metricSetList.operations.star.confirm']}*/}
                          {/*            onOk={async (e) => { await callback('star', item); }}*/}
                          {/*            onVisibleChange={setVisible}*/}
                          {/*        >*/}
                          {/*            <IconStar />*/}
                          {/*        </Popconfirm>*/}
                          {/*    }*/}
                          {/*</div>*/}
                      </div>
                      <div className={styles.time}>{formatTimeStamp(item.createTime, DateTimeFormat)}</div>
                  </div>
              }
          >
              <div style={{ height: size === 'small' ? '25px' : '60px' }} className={styles.content}>
                  {getContent()}
              </div>
              <Meta
                  avatar={
                      <Space style={{ fontSize: '12px' }}>
                          <FaRegUser size={9} />{item.createUser?.username}
                      </Space>
                  }
              />
          </Card>
      </Link>
  );
}

export default CardBlock;
