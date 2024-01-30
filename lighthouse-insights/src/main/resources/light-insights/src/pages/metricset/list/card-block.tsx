import React, {useEffect, useState} from 'react';
import cs from 'classnames';
import {
    Button,
    Card,
    Descriptions,
    Message,
    Popconfirm,
    Skeleton,
    Space,
    Tag,
    Typography,
} from '@arco-design/web-react';
import {
    IconFaceSmileFill,
    IconPenFill,
    IconPushpin,
    IconStarFill,
    IconSunFill,
    IconThumbUpFill, IconUser,
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {MetricSet} from "@/types/insights-web";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import {useHistory} from 'react-router-dom';
import {getRandomString} from "@/utils/util";
import {getLockIcon} from "@/pages/common/desc/base";
import {PermissionEnum} from "@/types/insights-common";
import { GoShareAndroid } from "react-icons/go";
import { FiUser } from "react-icons/fi";

const { Meta } = Card;


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
        window.open('/metricset/preview/' + item?.id, '_blank');
    };

  return (
    <Card
      bordered={true}
      className={className}
      size="small"
      style={{cursor:'pointer'}}
      actions={
          item.permissions.includes(PermissionEnum.ManageAble)?
          [
          <span key={3} className='icon-hover' onClick={(e) => {e.stopPropagation();callback('update',item)}}>
            {/*<Button type={"secondary"} size={"mini"}>Like</Button>*/}
              <Button type={"primary"} size={"mini"} onClick={handleClick}>Preview</Button>
          </span>,
          <Popconfirm
               key={getRandomString()}
                      focusLock
                      position={"tr"}
                      title='Confirm'
                      content={t['metricSetList.operations.delete.confirm']}
                      onOk={async () => {
                        await callback('delete',item);
                      }}>
            <span key={4} className='icon-hover'>
                {/*<Button type={"secondary"} size={"mini"}>Share</Button>*/}
            </span>
          </Popconfirm>,
      ]:null}

      title={
          <div  onClick={handleClick}>
              <div
                  className={cs(styles.title, {
                      [styles['title-more']]: visible,
                  })}
              >
                  {item.title}{getLockIcon(t,item.privateType,item.permissions)}
                  <div className={styles.more} onClick={() => callback('fixed',item.id)}>
                      <IconPushpin />
                  </div>
              </div>
              <div className={styles.time}>{formatTimeStamp(item.createTime,DateTimeFormat)}</div>
          </div>
      }
    >
      <div className={styles.content} onClick={handleClick}>{getContent()}</div>
        <Meta
            avatar={
                item.permissions.includes(PermissionEnum.AccessAble) ?
                <Space>
                </Space>:null
            }
        />
    </Card>
  );
}

export default CardBlock;
