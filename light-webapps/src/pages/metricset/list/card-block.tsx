import React, {useEffect, useState} from 'react';
import cs from 'classnames';
import {Button, Card, Descriptions, Popconfirm, Space, Tag, Typography} from '@arco-design/web-react';
import {IconStar, IconStarFill,} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {MetricSet} from "@/types/insights-web";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import {getLockIcon} from "@/desc/base";
import {PermissionEnum} from "@/types/insights-common";
import {useSelector} from "react-redux";

const { Meta } = Card;


interface CardBlockType {
  item: MetricSet;
  loading?: boolean;
  callback;
  from?:string;
  size?:string;
}

const IconList = [
    IconStarFill,
].map((Tag, index) => <Tag style={{marginBottom:'0.6px'}} key={index} />);

const { Paragraph } = Typography;

function CardBlock(props: CardBlockType) {
  const {item ,callback,size,from } = props;
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(props.loading);

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

    const handleClick = () => {
        window.open('/metricset/preview/' + item?.id, from == "list" ? '_blank':'_self');
    };

    const getTitleIcon = (index) => {
        return (
            <div className={styles.icon}>
                {IconList[index]}
            </div>
        );
    };

    useEffect(() => {
        console.log("----From is:" + from);
    },[])

  return (
    <Card
        onClick={handleClick}
      bordered={true}
      className={className}
      size="small"
      style={{cursor:'pointer',height:size == 'small'?'150px':'190px'}}
        actions={
          item.permissions.includes(PermissionEnum.AccessAble)?
          [
          <span key={3} className='icon-hover' onClick={(e) => {e.stopPropagation();handleClick();}}>
             <Button type={"secondary"} size={"mini"}>{t['basic.form.button.preview']}</Button>
          </span>,
      ]:null}

      title={
          <div>
              <div
                  className={cs(styles.title, {
                      [styles['title-more']]: visible,
                  })}
              >
                  <span onClick={(e) => {e.stopPropagation();}}>
                  {
                      staredMetricInfo.map(z => z.id).includes(item.id)?
                          <Popconfirm

                                      position={"bl"}
                                      title='Confirm'
                                      content={t['metricSetList.operations.unstar.confirm']}
                                      onOk={async (e) => {await callback('unstar',item)}}
                          >
                          <span>{getTitleIcon(0)}</span>
                          </Popconfirm>
                          :null
                  }
                      </span>
                  <span onClick={handleClick}>
                      <span style={{display:"inline-flex",alignItems:"center"}}>{item.title}{getLockIcon(t,item.privateType,item.permissions)}</span>
                  </span>
                  <div onClick={(e) => {e.stopPropagation();}} className={styles.more}>
                  {
                      staredMetricInfo.map(z => z.id).includes(item.id) ? null:
                          <Popconfirm
                                      focusLock
                                      position={"br"}
                                      title='Confirm'
                                      content={t['metricSetList.operations.star.confirm']}
                                      onOk={async (e) => {await callback('star',item)}}
                          >
                            <IconStar />
                          </Popconfirm>
                  }
                  </div>
              </div>
              <div className={styles.time}>{formatTimeStamp(item.createTime,DateTimeFormat)}</div>
          </div>
      }
    >
      <div style={{height: size == 'small'?'25px':'60px'}} className={styles.content} onClick={handleClick}>{getContent()}</div>
        <Meta
            avatar={
                <Space>
                    {item.createUser?.username}
                </Space>
            }
        />
    </Card>
  );
}

export default CardBlock;
