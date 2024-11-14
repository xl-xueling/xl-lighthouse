import React from 'react';
import { Layout } from '@arco-design/web-react';
import { FooterProps } from '@arco-design/web-react/es/Layout/interface';
import cs from 'classnames';
import styles from './style/index.module.less';
import {getDateFormat, YearFormat} from "@/utils/date";
import {windowRedirect} from "@/utils/util";

function Footer(props: FooterProps = {}) {
  const { className, ...restProps } = props;
  return (
    <Layout.Footer className={cs(styles.footer, className)} {...restProps}>
        <span style={{cursor:"pointer"}} onClick={() => windowRedirect('/license')}>Copyright &copy; {getDateFormat(YearFormat)} XueLing All Rights Reserved.</span>
      <span style={{marginLeft:'10px'}}>Version:2.3.0</span>
    </Layout.Footer>
  );
}

export default Footer;
