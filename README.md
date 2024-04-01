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
[![Language](https://img.shields.io/badge/build-passing-blue.svg)](https://github.com/xl-xueling/xl-lighthouse)
[![GitHub release](https://img.shields.io/github/tag/xl-xueling/xl-lighthouse.svg?label=release)](https://github.com/xl-xueling/xl-lighthouse/releases)
[![GitHub release date](https://img.shields.io/github/release-date/xl-xueling/xl-lighthouse.svg)](https://github.com/xl-xueling/xl-lighthouse/releases)

<p align="center"><font size="4">一键部署，一行代码接入，无需大数据研发运维经验，轻松驾驭海量数据实时统计。</font></p>
<p align="center"><font size="4">支撑百万量级数据指标，帮助企业搭建遍布全身的数据化运营体系。</font></p>

### 使用XL-LightHouse后：

* 1、再也不需要用Flink、Spark、ClickHouse或者基于Redis这种臃肿笨重的方案跑数了；
* 2、再也不需要疲于应付对个人价值提升没有多大益处的数据统计需求了，能够帮助您从琐碎反复的数据统计需求中抽身出来，从而专注于对个人提升、对企业发展更有价值的事情；
* 3、轻松帮您实现任意细粒度的监控指标，是您监控服务运行状况，排查各类业务数据波动、指标异常类问题的好帮手；
* 4、培养数据思维，辅助您将所从事的工作建立数据指标体系，量化工作产出，做专业严谨的职场人，创造更大的个人价值；

### 概述

* XL-LightHouse是针对互联网领域繁杂的流式数据统计需求而开发的一套集成了数据写入、数据运算、数据存储和数据可视化等一系列功能，支持超大数据量，支持超高并发的【通用型流式大数据统计平台】。
* XL-LightHouse目前已涵盖了各种流式数据统计场景，包括count、sum、max、min、avg、distinct、topN/lastN等多种运算，支持多维度计算，支持分钟级、小时级、天级多个时间粒度的统计，支持自定义统计周期的配置。
* XL-LightHouse内置丰富的转化类函数、支持表达式解析，可以满足各种复杂的条件筛选和逻辑判断。
* XL-LightHouse是一套功能完备的流式大数据统计领域的数据治理解决方案，它提供了比较友好和完善的可视化查询功能，并对外提供API查询接口，此外还包括数据指标管理、权限管理、统计限流等多种功能。
* XL-LightHouse支持时序性数据的存储和查询。

### 产品优势

+  XL-LightHouse面向企业繁杂的流式数据统计需求，可以帮助企业在极短时间内快速实现数以万计、数十万计的数据指标，而这是Flink、Spark、ClickHouse、Doris之类技术所远不能比拟的，XL-LightHouse帮助企业低成本实现数据化运营，数据指标体系可遍布企业运转的方方面面；
+  一套系统三种用途，可作为：通用型流式大数据统计系统、数据指标管理系统和数据指标可视化系统。
+  对单个流式统计场景的数据量无限制，可以非常庞大，也可以非常稀少，您既可以使用它完成十亿级用户量APP的DAU统计、十几万台服务器的运维监控、一线互联网大厂数据量级的日志统计、也可以用它来统计一天只有零星几次的接口调用量、耗时状况；
+  支持高并发查询统计结果；
+  一键部署、一行代码接入，无需专业的大数据研发人员，普通工程人员就可以轻松驾驭；
+  有完善的数据指标可视化以及数据指标管理维护等功能；

### XL-LightHouse与Flink和ClickHouse之类技术对比

-  [与Flink和ClickHouse之类技术对比](https://dtstep.com/zh/%E7%B3%BB%E7%BB%9F%E8%AE%BE%E8%AE%A1/%E5%BC%80%E6%BA%90XL-LightHouse%E4%B8%8EFlink%E3%80%81ClickHouse%E4%B9%8B%E7%B1%BB%E6%8A%80%E6%9C%AF%E7%9B%B8%E6%AF%94%E6%9C%89%E4%BB%80%E4%B9%88%E4%BC%98%E5%8A%BF.html)

###  一键部署

-  [一键部署](https://dtstep.com/zh/%E5%AE%89%E8%A3%85%E9%83%A8%E7%BD%B2/%E4%B8%80%E9%94%AE%E9%83%A8%E7%BD%B2.html)

###  演示站点

演示站点：http://119.91.203.220:19232/     测试账号：admin，密码：123456

###  Web端部分功能预览

![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/5.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/22.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/23.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/7.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/8.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/9.jpg?t=2)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/11.jpg)
