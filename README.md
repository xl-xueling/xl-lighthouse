<br>
<div align="center">
	<img src="https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/26.jpg" width="220px;">
</div>

<p align="center">
A general-purpose streaming big data statistics system.<br>
Easier to use, supports a larger amount of data, and can complete more statistical indicators faster.
</p>

<p align="center">
      <a href="https://github.com/xl-xueling/xl-lighthouse/blob/master/LICENSE">
        <img src="https://img.shields.io/github/license/xl-xueling/xl-lighthouse.svg" alt="LICENSE" />
      </a>
       <a href="https://www.java.com">
        <img src="https://img.shields.io/badge/language-Java-blue.svg" alt="Language" />
      </a>
      <a href="https://github.com/xl-xueling/xl-lighthouse">
        <img src="https://img.shields.io/badge/build-passing-blue.svg" alt="repository" />
      </a>
     <a href="https://dtstep.com">
        <img src="https://img.shields.io/badge/website-dtstep.com-blue" alt="WebSite" />
      </a>
    <a href="https://github.com/xl-xueling/xl-lighthouse/releases">
        <img src="https://img.shields.io/github/release-date/xl-xueling/xl-lighthouse.svg" alt="GitHub release date" />
      </a>
</p>

<p align="center"><font size="4">一键部署，一行代码接入，无需大数据研发运维经验，轻松驾驭海量数据实时统计。</font></p>
<p align="center"><font size="4">支撑百万量级数据指标，打造成本最低的数据化运营方案，帮助企业快速搭建数据化运营体系。</font></p>
<p align="center"><font size="4">流式统计运算性能超越Flink/Spark 100倍！</font></p>
<p align="center"><font size="4">开源、免费、可商用的自助式BI系统，除大数据版本外，同时支持单机版！</font></p>

### 概述

![XL-LightHouse](https://ldp-dtstep-1300542249.cos.ap-guangzhou.myqcloud.com/readme/01.jpg)

* XL-LightHouse是针对企业繁杂的数据统计需求而开发的一套集成了数据写入、数据运算和数据可视化等一系列功能，支持超大数据量，支持超高并发的【通用型流式大数据统计系统】。
* XL-LightHouse目前已涵盖了各种流式数据统计场景，包括count、sum、max、min、avg、distinct、topN/lastN等多种运算，支持多维度计算，支持分钟级、小时级、天级多种时间粒度的统计，支持自定义统计周期的配置。
* XL-LightHouse内置丰富的转化类函数、支持表达式解析，可以满足各种复杂的条件筛选和逻辑判断。
* XL-LightHouse提供了完善的可视化查询功能，对外提供API查询接口，此外还包括数据指标管理、权限管理、统计限流等多种功能。
* XL-LightHouse支持时序性数据的存储和查询。

### 项目特点

通用型流式数据统计或许是唯一一种有可能支撑百万量级数据指标，而成本仍可控制在企业可承受范围之内的技术。

XL-LightHouse是开源社区世界范围内第一个也是目前唯一一个通用型流式大数据统计系统。 目前业内广泛采用的以实时计算、离线计算、OLAP、时序数据库为主的技术方案都太过于臃肿和笨重，如果替换为以通用型流式数据统计为主，以其他技术方案为辅的实现方式可大幅降低企业成本。XL-LightHouse期望使用更为轻巧和实用的技术方案帮助企业应对繁琐的数据统计问题。

+  基于新一代流式统计运算标准XL-Formula实现，依据流式统计的运算特点而设计，并对每一种运算单元进行反复优化，使得每一种运算单元可以以非常低的成本，无限制复用；
+  可以短时间内快速实现庞大量级数据指标，而这是Flink、Spark、ClickHouse、Doris之类技术所远不能比拟的，可以支撑支撑百万量级数据指标，低成本触达每一个细微的产品模块，帮助企业搭建遍布全身的数据化运营体系
+  一套系统三种用途，可作为：通用型流式大数据统计系统、数据指标管理系统和数据指标可视化系统。
+  对单个流式统计场景的数据量无限制，可以非常庞大，也可以非常稀少，既可以使用它完成十亿级用户量APP的DAU统计、几十万台服务器的运维监控、一线互联网大厂数据量级的日志统计、一线电商企业的订单统计、全世界最大的社交类产品的消息量统计、也可以用它来统计一天只有零星几次的接口调用量、耗时状况；
+  与同领域很多其他技术方案不同，XL-Lighthouse擅长应对“繁杂”的数据统计需求，企业规模越大、数据指标需求越多，XL-LightHouse的优势就越明显；
+  有完善的API，支持高并发查询统计结果；
+  支持数据自动备份、可以一键导入历史数据、可以方便的执行集群扩容/缩容；
+  前端基于最新版ArcoDesign(React版本)开发，页面清爽大气，操作体验非常好；
+  支持自定义存储引擎；
+  所有代码100%开源，方便进行二次开发；
+  轻量级开箱即用，一键部署、系统运维和数据接入完全不需要大数据相关经验，普通工程人员即可轻松驾驭；

### 一个比喻

在数据化运营领域，目前业内广泛采用各类OLAP以及各种实时、离线计算引擎以及衍生类的数仓方案，我认为这不是一种良好的状态，更像是一种技术盲从和墨守成规，因为大多数的业务场景并不需要如此笨重的技术方案。拿OLAP来说，如果将OLAP比喻成“火车”，很多企业目前采用的技术方案就像用火车送快递一样不自然。
OLAP虽然适用场景广泛，但是它的问题在于：接入成本、维护成本、服务器运算成本、时间成本以及对技术人员的要求都太高了。

XL-LightHouse在业内究竟处于一个什么样的位置？

XL-LightHouse并不是要帮你解决所有的问题，而是要以极低的成本帮你解决大部分问题。如果站在一个更高的视角来看，所有的数据指标需求也像是一个金字塔型，其中较为复杂的只占一小部分，而大部分数据指标都可以基于流式统计来实现，随着企业数据化运营程度的加深，这个比例会越来越悬殊，并且很多看似复杂的数据统计场景其实也大多可以拆解成一些简单的问题，然后使用流式统计实现。

通用型流式统计更像是“汽车”，它的核心优势在于：使用便捷、成本极低。企业的数据指标需求越多，通用型流式统计的优势就越明显。它不能取代OLAP，但是它可以急剧的缩减OLAP集群的规模。

---
**截止到v2.2.7版本，经过两年多时间、二十多个版本的迭代，XL-LightHouse的功能已经比较稳定，不管是底层的运算性能、计算的准确性，还是Web系统的页面操作类功能都已经过反复测试，并已被一些企业采纳在线上长时间稳定运行！**

**现阶段侧重商业付费版本的功能开发，开源版本更新可能会略有延迟，本项目长期维护，敬请放心使用。**

---

### 可以用来做什么？

XL-LightHouse可应用在企业生产的众多环节，面向企业至上而下所有职能人员共同使用，可以帮助职场人从容应对大量琐碎、重复性的数据统计工作，减少不必要的时间浪费，提高工作效率。

以电商企业来说：
+ 可以为企业决策层提供其所关注的平台交易额、交易量、下单用户数、订单平均金额、人均消费金额等指标；
+ 可以为产品经理提供其所负责产品模块的pv、uv和点击率等指标；
+ 可以为运营人员提供关注的拉新用户量、各访问渠道用户量、站内各个广告位的点击量、点击用户数、点击收益等指标；
+ 可以为开发人员提供其关注的接口调用量、异常量、耗时情况等指标，可以辅助进行压力测试；
+ 可以为算法工程师提供其关注的模型训练时长、模型上线后的效果评测等指标，可以辅助进行ABTest；
+ 可以为运维人员提供其关注的是线上集群的CPU、内存、负载状况、IO、请求数、流量传输大小等监控指标；
+ 可以为UI设计师提供其关注的不同设计方案的点击转化对比情况；
+ 可以为数据分析师提供全面的数据指标更准确判断业务短板、业务走势、辅助决策层有针对性制定营销计划；
+ 可以轻松实现对各类复杂业务逻辑各主要环节的数据监控，及时发现问题并辅助问题排查。
+ 可以快速建立数据指标之间的交叉验证体系，轻松佐证数据指标的准确性。
+ 可以面向物联网及工业互联网场景实现各类设备上报数据相关指标统计和监控。

更多示例可参考：

- [即时通讯场景演示](https://dtstep.com/docs/110041/)
- [技术类场景演示](https://dtstep.com/docs/110042/)
- [电商类场景演示](https://dtstep.com/docs/110043/)
- [资讯类场景演示](https://dtstep.com/docs/110044/)

### 单机版本

XL-LightHouse除了大数据版本外，同时支持单机版。单机模式成本低廉，最低配置只需要一台4核8G的云服务器。

适用场景：
+ 面向中小企业或中小型业务团队使用；
+ 面向"用完即弃"的使用场景；

有些时候对数据指标的需求，往往只在某个特定阶段。比如：新接口上线要进行接口性能优化；线上业务出现数据异常问题需要排查；数据库读写压力突然暴涨，需要确定异常请求的来源等等，
对于此类问题的排查，流式统计可以起到至关重要的作用。但问题排查一般不需要持续很长时间，可能一两周甚至两三天。这种情况可以使用XL-LightHouse单机版。一键部署，轻量级使用，问题排查完，将XL-LightHouse删除即可。相信灵活的使用XL-LightHouse可以为您解决很多棘手的问题！

+ 用于初步体验XL-LightHouse或作为二次开发的联调测试环境；

### Hello World

- [创建第一个数据指标](https://dtstep.com/docs/110029/)
- [ICON点击数据统计](https://dtstep.com/docs/110030/)
- [电商订单数据统计](https://dtstep.com/docs/110031/)
- [订单支付状态数据统计](https://dtstep.com/docs/110032/)

### XL-LightHouse与Flink和ClickHouse之类技术对比

-  [系统介绍](https://dtstep.com/docs/110011/)
-  [与Flink和ClickHouse之类技术对比](https://dtstep.com/docs/110036/)

###  版本记录

-  [安装包下载](https://dtstep.com/docs/110027/)
-  [开源版本（最新：v2.2.9）](https://dtstep.com/docs/110027/)
-  [商业版本（最新：v2.2.9-pro.3）](https://dtstep.com/docs/110072/)

###  日常运维

-  [一键部署](https://dtstep.com/docs/110033/)
-  [一键升级](https://dtstep.com/docs/110048/)
-  [数据备份](https://dtstep.com/docs/110049/)
-  [日常运维](https://dtstep.com/docs/110047/)

###  版权声明

- 企业、机构内部使用或个人使用XL-LightHouse源程序不受任何限制，但不可删除程序中的Logo标识、版权声明等信息。
- 销售基于XL-LightHouse源码或相关设计方案的软硬件产品及服务，需要向开发者缴纳一定比例的授权费用(0.2%~2%)。

创作者权益受法律保护，为避免不必要的版权纠纷，在销售相关产品或服务前，请务必查阅：[版权声明](https://dtstep.com/docs/110073/)

###  开发者承诺

为保障XL-LightHouse项目更好的满足用户使用，开发者向所有使用者郑重承诺：

+ 对较为严重可能造成数据泄露或数据丢失风险的漏洞都会第一时间修复；
+ 影响基本功能使用的问题都会第一时间修复；
+ 所有程序100%开源，开发者不会在程序中主观刻意添加任何形式的"后门"或"漏洞"，开发者绝不会窃取使用方业务数据以及试图通过控制使用方自身服务器资源的方式获取利益。
+ 企业、机构内部使用或个人使用XL-LightHouse源程序、相关设计方案以及XL-Formula标准，无需授权并且永远不收取任何费用。
+ 本项目长期维护。

敬请大家放心使用，如有疑问，请随时联系开发者咨询~

###  开源版本Web端部分功能预览

商业版本是在开源版本的基础上额外新增功能，开源版本所有已提供的功能不会有任何使用层面的限制（比如不会限制数据量、集群规模、统计指标数量、Web系统访问人数等等）。

![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/32.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/5.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/34.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/23.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/7.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/8.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/33.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/9.jpg?t=2)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/24.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/11.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/37.jpg)

###  商业版本Web端部分功能预览

XL-LightHouse商业版提供更加强大和便利的数据统计分析功能，目前已支持折线图、面积图、柱状图、饼状图、漏斗图、表格等多种展示形式，支持各种日期筛选和维度筛选组件的搭配使用。每个数据指标可根据个人需求任意选择展示形式，不需要SQL开发，只需页面配置，即可轻松实现美观大方的数据展示效果。
此外XL-LightHouse向商业版用户提供功能完善、性能强大、业内领先的通用型监控告警实现方案。

商业版价格十分优惠，详见项目网站介绍，欢迎采购！

![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/38.jpg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/53.jpeg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/54.jpeg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/52.jpeg)
![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/screenshot_v2/51.jpeg)

### Contact

如果您有任何问题、意见或建议，请您添加以下微信。

![XL-LightHouse](https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/contact/contact_20240627_084143.jpg)
