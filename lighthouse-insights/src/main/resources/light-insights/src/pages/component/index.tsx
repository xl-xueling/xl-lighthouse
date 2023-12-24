import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department, PermissionsEnum, Project, Stat} from "@/types/insights-web";
import {requestQueryDimensValue} from "@/api/group";
import {Button, Form, Grid, Select, TreeSelect} from "@arco-design/web-react";
import {translate} from "@/pages/department/common";
import {useSelector} from "react-redux";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import styles from "@/pages/stat/display/style/index.module.less";
import {IconRefresh, IconSearch} from "@arco-design/web-react/icon";
import {DatePicker} from "@arco-design/web-react";
import {requestQueryByIds} from "@/api/project";
import {requestQueryByIds as requestQueryComponentsByIds} from "@/api/component";
import {requestPrivilegeCheck} from "@/api/privilege";

export default function SearchForm() {

    return (<div>sss</div>);
}