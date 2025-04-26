import React, {forwardRef} from 'react';
import {DatePicker} from "@arco-design/web-react";
import dayjs from "dayjs";

const StatViewDatePicker = forwardRef((props: any, ref) => {

    const { statInfo, value, onChange, size = 'small' } = props;

    const handleDateChange = (date: any) => {
        onChange(date);
    };

    const getDatePicker = () => {
        if(statInfo.timeparam.endsWith("second")){
            return <DatePicker.RangePicker mode={"date"} size={size} style={{width:'100%'}} value={value ? value : null} format="YYYY-MM-DD" allowClear={false}
                                           disabledDate={(date) => dayjs(date).isAfter(dayjs())} onChange={handleDateChange}
            />;
        }else if(statInfo.timeparam.endsWith("minute")){
            return <DatePicker.RangePicker mode={"date"} size={size} style={{width:'100%'}} value={value ? value : null} format="YYYY-MM-DD" allowClear={false}
                                           disabledDate={(date) => dayjs(date).isAfter(dayjs())} onChange={handleDateChange}
            />;
        }else if(statInfo.timeparam.endsWith("hour")){
            return <DatePicker.RangePicker mode={"date"} size={size} style={{width:'100%'}} value={value ? value : null} format="YYYY-MM-DD" allowClear={false}
                                           disabledDate={(date) => dayjs(date).isAfter(dayjs())} onChange={handleDateChange}
            />;
        }else if(statInfo.timeparam.endsWith("day")){
            return <DatePicker.RangePicker mode={"date"} size={size} style={{width:'100%'}} format="YYYY-MM-DD" value={value ? value : null} allowClear={false}
                                           disabledDate={(date) => dayjs(date).isAfter(dayjs())} onChange={handleDateChange}
            />;
        }else if(statInfo.timeparam.endsWith("month")){
            return <DatePicker.RangePicker mode={"month"} size={size} style={{width:'100%'}} value={value ? value : null} allowClear={false}
                                           disabledDate={(date) => dayjs(date).isAfter(dayjs())} onChange={handleDateChange}
            />;
        }else if(statInfo.timeparam.endsWith("year")){
            return <DatePicker.RangePicker mode={"year"} size={size} style={{width:'100%'}} value={value ? value : null} allowClear={false}
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

StatViewDatePicker.displayName = 'StatViewDatePicker';
export default StatViewDatePicker;