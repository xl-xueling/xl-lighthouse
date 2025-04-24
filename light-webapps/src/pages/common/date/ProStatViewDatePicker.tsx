import React, {forwardRef} from 'react';
import {DatePicker} from "@arco-design/web-react";
import dayjs from "dayjs";

const ProStatViewDatePicker = forwardRef((props: any, ref) => {

    const { statInfo,value, onChange } = props;

    const handleDateChange = (date: any) => {
        onChange(date);
    };

    const getDatePicker = () => {
        if(statInfo.timeparam.endsWith("second")){
            return <DatePicker.RangePicker
                                           shortcutsPlacementLeft
                                           shortcuts={[
                                               {
                                                   text: '今天',
                                                   value: () => [dayjs(), dayjs().add(2, 'day')],
                                               },
                                               {
                                                   text: '昨天',
                                                   value: () => [dayjs(), dayjs().add(1, 'week')],
                                               },
                                           ]}
                                           mode={"date"} style={{width:'100%'}} value={value ? value : null} format="YYYY-MM-DD" allowClear={false}
                                           disabledDate={(date) => dayjs(date).isAfter(dayjs())} onChange={handleDateChange}
            />;
        }else if(statInfo.timeparam.endsWith("minute")){
            return <DatePicker.RangePicker
                                           shortcutsPlacementLeft
                                           shortcuts={[
                                               {
                                                   text: '今天',
                                                   value: () => [dayjs(), dayjs().add(2, 'day')],
                                               },
                                               {
                                                   text: '昨天',
                                                   value: () => [dayjs(), dayjs().add(1, 'week')],
                                               },
                                               {
                                                   text: '最近3天',
                                                   value: () => [dayjs(), dayjs().add(1, 'month')],
                                               },
                                               {
                                                   text: '最近7天',
                                                   value: () => [dayjs(), dayjs().add(6, 'month')],
                                               },
                                           ]}
                                           mode={"date"} style={{width:'100%'}} value={value ? value : null} format="YYYY-MM-DD" allowClear={false}
                                           disabledDate={(date) => dayjs(date).isAfter(dayjs())} onChange={handleDateChange}
            />;
        }else if(statInfo.timeparam.endsWith("hour")){
            return <DatePicker.RangePicker
                                           shortcutsPlacementLeft={false}
                                           shortcuts={[
                                               {
                                                   text: '今天',
                                                   value: () => [dayjs(), dayjs()],
                                               },
                                               {
                                                   text: '昨天',
                                                   value: () => [dayjs().subtract(1, 'day'), dayjs().subtract(1, 'day')],
                                               },
                                               {
                                                   text: '最近3天',
                                                   value: () => [dayjs().subtract(2, 'day'), dayjs()],
                                               },
                                               {
                                                   text: '最近7天',
                                                   value: () => [dayjs().subtract(6, 'day'), dayjs()],
                                               },
                                               {
                                                   text: '最近14天',
                                                   value: () => [dayjs().subtract(13, 'day'), dayjs()],
                                               },
                                               {
                                                   text: '最近30天',
                                                   value: () => [dayjs().subtract(29, 'day'), dayjs()],
                                               },
                                           ]}
                                           mode={"date"} style={{width:'100%'}} value={value ? value : null} format="YYYY-MM-DD" allowClear={false}
                                           disabledDate={(date) => dayjs(date).isAfter(dayjs())} onChange={handleDateChange}
            />;
        }else if(statInfo.timeparam.endsWith("day")){
            return <DatePicker.RangePicker
                                           shortcuts={[
                                               {
                                                   text: '最近7天',
                                                   value: () => [dayjs().subtract(6, 'day'), dayjs()],
                                               },
                                               {
                                                   text: '最近14天',
                                                   value: () => [dayjs().subtract(13, 'day'), dayjs()],
                                               },
                                               {
                                                   text: '最近30天',
                                                   value: () => [dayjs().subtract(29, 'day'), dayjs()],
                                               },
                                               {
                                                   text: '最近60天',
                                                   value: () => [dayjs().subtract(59, 'day'), dayjs()],
                                               },
                                           ]}
                                           style={{width:'100%'}} format="YYYY-MM-DD" value={value ? value : null} allowClear={false}
                                           disabledDate={(date) => dayjs(date).isAfter(dayjs())} onChange={handleDateChange}
            />;
        }else if(statInfo.timeparam.endsWith("month")){
            return <DatePicker.RangePicker
                                           shortcutsPlacementLeft
                                           mode={"month"} style={{width:'100%'}} value={value ? value : null} allowClear={false}
                                           disabledDate={(date) => dayjs(date).isAfter(dayjs())} onChange={handleDateChange}
            />;
        }else if(statInfo.timeparam.endsWith("year")){
            return <DatePicker.RangePicker
                                           mode={"year"} style={{width:'100%'}} value={value ? value : null} allowClear={false}
                                           disabledDate={(date) => dayjs(date).isAfter(dayjs())} onChange={handleDateChange}
            />;
        }
    }

    return (
        <>
            {getDatePicker()}
        </>
    );
});

ProStatViewDatePicker.displayName = 'ProStatViewDatePicker';
export default ProStatViewDatePicker;