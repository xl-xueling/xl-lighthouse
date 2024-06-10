import React from 'react';
import {ComponentTypeEnum, RenderFilterConfig} from "@/types/insights-common";
import {Input, TreeSelect} from "@arco-design/web-react";
import {translateToCascadeTreeNodes} from "@/pages/department/base";

export function getFilterRender(renderFilterConfig:RenderFilterConfig,size:string = "default") {
    if(renderFilterConfig.componentType == ComponentTypeEnum.FILTER_INPUT){
        return (
            <Input size={"small"} allowClear={true} placeholder={size == 'mini' ? renderFilterConfig.label : "Search Value"}  autoComplete={'off'}/>
    )
    }if(renderFilterConfig.componentType == ComponentTypeEnum.FILTER_SELECT){
        return (
            <TreeSelect size={"small"}
        placeholder={size == 'mini' ? renderFilterConfig.label : "Please Select"}
        multiple={true}
        treeCheckable={true}
        treeProps={{
            height: 200,
                renderTitle: (props) => {
                return (
                    <span style={{ whiteSpace: 'nowrap', }} >
                {props.title}
                </span>
            );
            },
        }}
        treeCheckStrictly={false}
        allowClear={true}
        showSearch={true}
        treeData={translateToCascadeTreeNodes(renderFilterConfig.configData)} />
    )
    }else{
        return (
            <TreeSelect size={"small"}
        placeholder={size == 'mini' ? renderFilterConfig.label : "Please Select"}
        multiple={true}
        treeCheckable={true}
        treeProps={{
            height: 200,
                renderTitle: (props) => {
                return (
                    <span style={{ whiteSpace: 'nowrap', }} >
                {props.title}
                </span>
            );
            },
        }}
        treeCheckStrictly={false}
        allowClear={true}
        showSearch={true}
        treeData={translateToCascadeTreeNodes(renderFilterConfig.configData)} />
    )
    }
}

export default function Constants() {
    return <>{/* nothing */}</>;
}