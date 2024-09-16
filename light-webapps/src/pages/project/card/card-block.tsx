import React, {useEffect, useState} from 'react';
import cs from 'classnames';
import {Button, Card, Descriptions, Popconfirm, Space, Tag, Typography} from '@arco-design/web-react';
import {
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
import {MetricSet, Project} from "@/types/insights-web";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import {useHistory} from 'react-router-dom';
import {getRandomString} from "@/utils/util";
import {getLockIcon, getTreeResourceIcon} from "@/pages/common/desc/base";
import {PermissionEnum} from "@/types/insights-common";
import {useSelector} from "react-redux";
import { HiMiniStar } from "react-icons/hi2";

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
].map((Tag, index) => <Tag key={index} />);

const { Paragraph } = Typography;

function CardBlock(props: CardBlockType) {
  const {item ,callback,size,from} = props;
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(props.loading);
  const history = useHistory();

  const t = useLocale(locale);
    const staredProjectInfo = useSelector((state: {staredProjectInfo:Array<Project>}) => state.staredProjectInfo);

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

    const redirectPreview = () => {
        window.open('/project/preview/' + item?.id, from == "list" ? '_blank':'_self');
    };

    const redirectManage = () => {
        window.open('/project/manage/' + item?.id, from == "list" ? '_blank':'_self');
    };

    const getTitleIcon = (index) => {
        return (
            <div className={styles.icon}>
                {IconList[index]}
            </div>
        );
    };

  return (
    <Card
        onClick={redirectPreview}
      bordered={true}
      className={className}
      size="small"
      style={{cursor:'pointer'}}
        actions={
          [
          <span key={3} className='icon-hover' onClick={(e) => {e.stopPropagation();redirectPreview();}}>
             <Button type={"secondary"} size={"mini"}>{t['basic.form.button.preview']}</Button>
          </span>,
          item.permissions.includes(PermissionEnum.ManageAble)?
              <span key={3} className='icon-hover' onClick={(e) => {e.stopPropagation();redirectManage();}}>
             <Button type={"primary"} size={"mini"}>{t['basic.form.button.manage']}</Button>
          </span>:null,
        ]}

      title={
          <div>
              <div
                  className={cs(styles.title, {
                      [styles['title-more']]: visible,
                  })}
              >
                  <span onClick={(e) => {e.stopPropagation();}}>
                  {
                      staredProjectInfo?.map(z => z.id).includes(item.id)?
                          <Popconfirm

                                      position={"bl"}
                                      title='Confirm'
                                      content={t['projectList.operations.unstar.confirm']}
                                      onOk={async (e) => {await callback('unstar',item)}}
                          >
                          <span>{getTitleIcon(0)}</span>
                          </Popconfirm>
                          :null
                  }
                      </span>
                  <span onClick={redirectPreview}>
                      <span style={{display:"inline-flex",alignItems:"center"}}>{item.title}{getLockIcon(t,item.privateType,item.permissions)}</span>
                  </span>
                  <div onClick={(e) => {e.stopPropagation();}} className={styles.more}>
                  {
                      staredProjectInfo?.map(z => z.id).includes(item.id) ? null:
                          <Popconfirm
                                      focusLock
                                      position={"br"}
                                      title='Confirm'
                                      content={t['projectList.operations.star.confirm']}
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
      <div style={{height: size == 'small'?'25px':'60px'}} className={styles.content} onClick={redirectPreview}>{getContent()}</div>
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
