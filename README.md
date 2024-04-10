<br>
<div align="center">
	<img src="https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/26.jpg" width="220px;">
</div>

<p align="center">
A general-purpose streaming big data statistics system.<br>
Easier to use, supports a larger amount of data, and can complete more statistical indicators faster.
</p>

[![LICENSE](https://img.shields.io/github/license/xl-xueling/xl-lighthouse.svg)](https://github.com/xl-xueling/xl-lighthouse/blob/master/LICENSE)
[![Language](https://img.shields.io/badge/language-Java-blue.svg)](https://www.java.com)
[![repository](https://img.shields.io/badge/build-passing-blue.svg)](https://github.com/xl-xueling/xl-lighthouse)
[![website](https://img.shields.io/badge/website-dtstep.com-blue)](https://dtstep.com)
[![GitHub release](https://img.shields.io/github/tag/xl-xueling/xl-lighthouse.svg?label=release)](https://github.com/xl-xueling/xl-lighthouse/releases)
[![GitHub release date](https://img.shields.io/github/release-date/xl-xueling/xl-lighthouse.svg)](https://github.com/xl-xueling/xl-lighthouse/releases)

<p align="center"><font size="4">一键部署，一行代码接入，无需大数据研发运维经验，轻松驾驭海量数据实时统计。</font></p>
<p align="center"><font size="4">支撑百万量级数据指标，帮助企业低成本搭建数据化运营体系。</font></p>

### 概述

* XL-LightHouse是针对繁杂的数据统计需求而开发的一套集成了数据写入、数据运算和数据可视化等一系列功能，支持超大数据量，支持超高并发的【通用型流式大数据统计系统】。
* XL-LightHouse目前已涵盖了各种流式数据统计场景，包括count、sum、max、min、avg、distinct、topN/lastN等多种运算，支持多维度计算，支持分钟级、小时级、天级多个时间粒度的统计，支持自定义统计周期的配置。
* XL-LightHouse内置丰富的转化类函数、支持表达式解析，可以满足各种复杂的条件筛选和逻辑判断。
* XL-LightHouse提供了完善的可视化查询功能，对外提供API查询接口，此外还包括数据指标管理、权限管理、统计限流等多种功能。
* XL-LightHouse支持时序性数据的存储和查询。

### 项目特点

通用型流式数据统计或许是唯一一种有可能支撑百万量级数据指标，而成本仍可控制在企业可承受范围之内的技术。目前业内广泛采用的以实时计算、离线计算、OLAP为主的技术方案都太过于臃肿和笨重，如果替换为以通用型流式数据统计为主，以其他技术方案为辅可大幅降低企业成本。XL-LightHouse期望使用更为轻巧和实用的技术方案应对繁琐的数据统计问题。

+  XL-LightHouse可以短时间内快速实现庞大量级数据指标，而这是Flink、Spark、ClickHouse、Doris之类技术所不能比拟的；
+  一套系统三种用途，可作为：通用型流式大数据统计系统、数据指标管理系统和数据指标可视化系统。
+  对单个流式统计场景的数据量无限制，可以非常庞大，也可以非常稀少，既可以使用它完成十亿级用户量APP的DAU统计、十几万台服务器的运维监控、一线互联网大厂数据量级的日志统计、也可以用它来统计一天只有零星几次的接口调用量、耗时状况；
+  支持高并发查询统计结果；
+  一键部署、一行代码接入，普通工程人员即可轻松驾驭；

**有希望使用XL-LightHouse的用户，可添加本人微信，提供免费一对一技术支持。如果在部署、使用过程中遇到任何问题，请及时提issue，本人会第一时间处理。**

### XL-LightHouse与Flink和ClickHouse之类技术对比

-  [与Flink和ClickHouse之类技术对比](https://dtstep.com/zh/architecture/02.html)

###  一键部署

-  [一键部署](https://dtstep.com/zh/deploy/01.html)

###  Hello World

- [ICON点击数据统计](https://dtstep.com/zh/helloworld/01.html)
- [电商订单数据统计](https://dtstep.com/zh/helloworld/02.html)
- [订单支付状态数据统计](https://dtstep.com/zh/helloworld/03.html)

###  使用场景演示
- [即时通讯场景演示](https://dtstep.com/zh/scene/01.html)
- [技术类场景演示](https://dtstep.com/zh/scene/02.html)
- [电商类场景演示](https://dtstep.com/zh/scene/03.html)
- [资讯类场景演示](https://dtstep.com/zh/scene/04.html)


###  Web端演示站点

演示站点：http://119.91.203.220:19232/     测试账号：admin，密码：123456

###  Web端部分功能预览

![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/5.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/22.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/23.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/7.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/8.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/9.jpg?t=2)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/11.jpg)
