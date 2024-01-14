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
    // if (type !== 'quality') {
    //   return <Paragraph>{item.description}</Paragraph>;
    // }
    return (
      <Descriptions
        column={2}
        colon=' :'
        data={[
          { label: '总点击数', value: "--" },
          { label: '总点赞数', value: "--" },
        ]}
      />
    );

  };

  const className = cs(styles['card-block']);

  return (
    <Card
      bordered={true}
      className={className}
      size="small"
      actions={[
        <span key={1} className='icon-hover' onClick={() => callback('like',item.id)}>
          <IconThumbUp/>
        </span>,
        <span key={2} className='icon-hover' onClick={() => callback('share',item.id)}>
          <IconShareInternal />
        </span>,
          <span key={3} className='icon-hover' onClick={() => callback('update',item.id)}>
          <IconEdit />
        </span>,
        <span key={4} className='icon-hover' onClick={() => callback('delete',item.id)}>
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
              {/*{getTitleIcon()}*/}
              {item.title}
              <div className={styles.more} onClick={() => callback('fixed',item.id)}>
                <IconPushpin />
              </div>
            </div>
            <div className={styles.time}>{item.createTime}</div>
          </>
        )
      }
    >
      <div className={styles.content}>{getContent()}</div>
    </Card>
  );
}

export default CardBlock;
