import React from "react";

import Markdown from "markdown-to-jsx";
import Login from "@/pages/login";
import {Space} from "@arco-design/web-react";

function License() {

    const chineseLicense = `
# 使用协议
   为保障创作者的合法权益以及支持XL-LightHouse项目的发展，本项目在Apache2.0开源协议的基础上，增加如下补充条款，如果以下条款与Apache2.0协议内容有所冲突，以该补充条款为准。   
   -   1、企业或机构内部使用XL-LightHouse源程序不受任何限制，但不可删除程序中的版权声明、原作者邮箱以及项目网址等信息。  
   -   2、企业、机构或个人如有以下行为需要得到本人许可并支付一定比例的授权费用。   
        - a、销售直接或间接包含XL-LightHouse源程序（超过1000行源码）的软硬件产品或服务。  
        - b、销售直接或间接借鉴XL-LightHouse系统设计方法的软硬件产品或服务。   
        - c、销售直接或间接包含XL-Formula设计标准（包括在此基础上修改演变而来的标准）的软硬件产品或服务。   
        - d、销售直接或间接依赖XL-LightHouse或XL-Formula相关的数据指标可视化产品或服务，包括但不限于插件、终端等任何形式的产品或服务。   
   -    如上所述的“服务”指为购买者提供统计数据类服务或提供相应产品的技术支持维护服务，为避免不必要的版权纠纷， 在销售相关产品或服务前请务必查阅【<a href="https://dtstep.com/zh/copyright/01.html" target="_blank">版权声明</a>】，本协议最终解释权归原作者所有。    
`;

    const englishLicense = `
# License
  In order to protect the legitimate rights and interests of creators and support the development of the XL-LightHouse project, this project adds the following supplementary terms on the basis of the Apache2.0 open source agreement. If the following terms conflict with the contents of the Apache2.0 agreement, the supplementary terms prevail.     
   -   1. There are no restrictions on the use of XL-LightHouse source programs within an enterprise or institution, but the copyright statement, author email address, and project website address in the program cannot be deleted.  
   -   2. Enterprises, institutions or individuals need to obtain their permission and pay a certain percentage of authorization fees for the following acts.   
        - a. Sales of software and hardware products or services that directly or indirectly contain XL-LightHouse source code (more than 1,000 lines of source code).  
        - b. Sales of software and hardware products or services that directly or indirectly refer to the XL-LightHouse system design method.   
        - c. Sales of software and hardware products or services that directly or indirectly include the XL-Formula design standard (including standards modified and evolved on this basis).   
        - d. Sales of data indicator visualization products or services that directly or indirectly rely on XL-LightHouse/XL-Formula, including but not limited to any form of products or services such as plug-ins and terminals.  
   -     The "service" mentioned above refers to providing statistical data services for buyers or providing technical support and maintenance services for corresponding products. In order to avoid unnecessary copyright disputes, please be sure to read the [<a href="https://dtstep.com/zh/copyright/01.html" target="_blank">Copyright Statement</a>] before selling related products or services. This The final interpretation right of the agreement belongs to the original author.
`;

    return (
        <>
            <Space style={{paddingLeft:'20px'}}>
                <Markdown>
                    {chineseLicense}
                </Markdown>
            </Space>
            <Space style={{paddingLeft:'20px'}}>
                <Markdown>
                    {englishLicense}
                </Markdown>
            </Space>
        </>
    )
}

License.displayName = 'LicensePage';

export default License;