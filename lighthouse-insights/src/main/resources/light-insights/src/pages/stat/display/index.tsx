import React, {useEffect} from 'react';
import {useParams} from "react-router-dom";
import FiltersForm from "./filters_form";
import {Card, Grid} from "@arco-design/web-react";
import styles from "./style/index.module.less";
import {useSelector} from "react-redux";
import {Department} from "@/types/insights-web";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
const { Row, Col } = Grid;

export default function StatDisplay({statId}) {

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const t = useLocale(locale);
    const { id } = useParams();

    useEffect(() => {
        console.log("stat display")
    },[])

    return(
        <Card>
            <div className={styles['search-form-wrapper']}>
                <FiltersForm />
            </div>
        </Card>
    );
}