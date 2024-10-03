import React, {useEffect, useState} from 'react';
import cs from 'classnames';
import {Button, Card, Descriptions, Link, Popconfirm, Space, Tag, Typography} from '@arco-design/web-react';
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
import {LuLayers} from "react-icons/lu";
import {PiDiamondsFour} from "react-icons/pi";

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

    const redirectPreview = (e) => {
        e.stopPropagation();
        e.preventDefault();
        if(from == 'list'){
            history.push(`/project/preview/${item?.id}`);
        }else{
            window.location.href = `/project/preview/${item?.id}`;
        }
    };

    const redirectManage = (e) => {
        e.stopPropagation();
        e.preventDefault();
        if(from == 'list'){
            history.push(`/project/manage/${item?.id}`);
        }else{
            window.location.href = `/project/manage/${item?.id}`;
        }
    };

  return (
      <Link to={`/project/preview/${item.id}`} onClick={(e) => {redirectPreview(e)}} style={{ textDecoration: 'none',width:'100%'}} >
        <Card
          bordered={true}
          className={className}
          size="small"
          style={{cursor:'pointer',height:size == 'small'?'150px':'190px'}}
          actions={
              [
              item.permissions.includes(PermissionEnum.AccessAble)?
                  <span key={3} className='icon-hover' onClick={redirectPreview}>
                     <Button type={"secondary"} size={"mini"}>{t['basic.form.button.preview']}</Button>
                  </span>:null,
                  item.permissions.includes(PermissionEnum.ManageAble)?
                      <span key={3} className='icon-hover' onClick={redirectManage}>
                     <Button type={"primary"} size={"mini"}>{t['basic.form.button.manage']}</Button>
                  </span>:null,
            ]}

          title={
              <div>
                  <div className={cs(styles.title, {
                      [styles['title-more']]: visible,
                  })}>
                      <span onClick={(e) => {e.stopPropagation();e.preventDefault();}}>
                      {
                          staredProjectInfo?.map(z => z.id).includes(item.id)?
                              <Popconfirm

                                          position={"bl"}
                                          title='Confirm'
                                          content={t['projectList.operations.unstar.confirm']}
                                          onOk={async (e) => {await callback('unstar',item)}}
                              >
                                  <Button type={"primary"} icon={<PiDiamondsFour style={{marginTop:'3px'}} size={13}/>} shape={"circle"} size={"mini"} style={{marginRight:'8px'}}/>
                              </Popconfirm>
                              :
                              <Popconfirm

                                  position={"bl"}
                                  title='Confirm'
                                  content={t['projectList.operations.unstar.confirm']}
                                  onOk={async (e) => {await callback('unstar',item)}}
                              >
                                  <Button icon={<PiDiamondsFour style={{marginTop:'3px'}} size={13}/>} shape={"circle"} size={"mini"} style={{marginRight:'8px'}}/>
                              </Popconfirm>
                      }
                          </span>
                      <span onClick={redirectPreview}>
                          <span style={{display:"inline-flex",alignItems:"center"}}>{item.title}{getLockIcon(t,item.privateType,item.permissions)}</span>
                      </span>
                      {/*<div onClick={(e) => {e.stopPropagation();e.preventDefault();}} className={styles.more}>*/}
                      {/*{*/}
                      {/*    staredProjectInfo?.map(z => z.id).includes(item.id) ? null:*/}
                      {/*        <Popconfirm*/}
                      {/*                    focusLock*/}
                      {/*                    position={"br"}*/}
                      {/*                    title='Confirm'*/}
                      {/*                    content={t['projectList.operations.star.confirm']}*/}
                      {/*                    onOk={async (e) => {await callback('star',item)}}*/}
                      {/*        >*/}
                      {/*          <IconStar />*/}
                      {/*        </Popconfirm>*/}
                      {/*}*/}
                      {/*</div>*/}
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
      </Link>
  );
}

export default CardBlock;
