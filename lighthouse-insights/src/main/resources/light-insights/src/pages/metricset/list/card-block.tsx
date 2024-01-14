import React, { useEffect, useState } from 'react';
import cs from 'classnames';
import {
  Button,
  Switch,
  Tag,
  Card,
  Descriptions,
  Typography,
  Skeleton,
} from '@arco-design/web-react';
import {
    IconStarFill,
    IconThumbUpFill,
    IconSunFill,
    IconFaceSmileFill,
    IconPenFill,
    IconMore, IconThumbUp, IconShareInternal, IconPushpin, IconDelete, IconEdit,
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {MetricSet} from "@/types/insights-web";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import {CiLock} from "react-icons/ci";
import { useHistory } from 'react-router-dom';

interface CardBlockType {
  item: MetricSet;
  loading?: boolean;
  callback;
}

const IconList = [
  IconStarFill,
  IconThumbUpFill,
  IconSunFill,
  IconFaceSmileFill,
  IconPenFill,
].map((Tag, index) => <Tag key={index} />);

const { Paragraph } = Typography;

function CardBlock(props: CardBlockType) {
  const {item ,callback } = props;
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(props.loading);
  const history = useHistory();

  const t = useLocale(locale);
  const changeStatus = async () => {
    setLoading(true);
    await new Promise((resolve) =>
      setTimeout(() => {
        resolve(null);
      }, 1000)
    ).finally(() => setLoading(false));
  };

  useEffect(() => {
    setLoading(props.loading);
  }, [props.loading]);


  const getContent = () => {
    if (loading) {
      return (
        <Skeleton
          text={{ rows: 3 }}
          animation
          className={styles['card-block-skeleton']}
        />
      );
    }
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
        window.open('/metricset/preview/' + item?.id, '_blank');
    };

  return (
    <Card
      bordered={true}
      className={className}
      size="small"
      style={{cursor:'pointer'}}
      onClick={handleClick}
      actions={[
          <span key={3} className='icon-hover' onClick={() => callback('update',item)}>
          <IconEdit />
        </span>,
        <span key={4} className='icon-hover' onClick={() => callback('delete',item)}>
          <IconDelete />
        </span>,
      ]}

      title={
        loading ? (
          <Skeleton
            animation
            text={{ rows: 1, width: ['100%'] }}
            style={{ width: '120px', height: '24px' }}
            className={styles['card-block-skeleton']}
          />
        ) : (
          <>
            <div
              className={cs(styles.title, {
                [styles['title-more']]: visible,
              })}
            >
              {item.title}{item.privateType == 0 ? <CiLock style={{marginLeft:'5px'}}/>:null}
              <div className={styles.more} onClick={() => callback('fixed',item.id)}>
                <IconPushpin />
              </div>
            </div>
            <div className={styles.time}>{formatTimeStamp(item.createTime,DateTimeFormat)}</div>
          </>
        )
      }
    >
      <div className={styles.content}>{getContent()}</div>
    </Card>
  );
}

export default CardBlock;
