## **一键部署，轻松实现海量数据实时统计，使用XL-LightHouse后再也不需要用FlinkSQL、SparkSQL、ClickHouse这种臃肿笨重的方案跑数了！**
## **还有些为了统计PV、UV，在自己搭建Redis集群的小伙伴们，可以体验一下xl-lighthouse,或许更适合你~~**
##  集群最低配置只需要3台16核32G的云服务器（如果想初步体验一下，3台8核16G的云服务器依然可以运行，购买云服务厂商3台【8核16G服务器竞价实例只需要1块钱】，1块钱您就可以体验一下XL-LightHouse），成本低廉，中小企业的福音！

### 概述

* XL-LightHouse是针对互联网领域繁杂的流式数据统计需求而开发的一套集成了数据写入、数据运算、数据存储和数据可视化等一系列功能，支持超大数据量，支持超高并发的【通用型流式大数据统计平台】。
* XL-LightHouse目前已基本涵盖了常见的流式数据统计场景，包括count、sum、max、min、avg、bitcount(distinct)、topN/lastN等多种运算，支持多维度计算，支持分钟级、小时级、天级多个时间粒度的统计，支持自定义统计周期的配置。
* XL-LightHouse内置丰富的转化类函数、支持表达式解析，可以满足各种复杂的条件筛选和逻辑判断。
* XL-LightHouse是一套功能完备的流式大数据统计领域的数据治理解决方案，它提供了比较友好和完善的可视化查询功能，并对外提供API查询接口，此外还包括数据指标管理、权限管理、统计限流等多种功能。
* XL-LightHouse支持时序性数据的存储和查询。

### 补充说明
* XL-LightHouse的应用场景非常广泛，比较常见的比如PV、UV的统计，接口的调用量、耗时情况、异常量等，更多场景您可以查看本文下面部分的场景演示，而且可以灵活配置统计周期和任意的统计维度，有完善的可视化查询页面，一键接入，轻松使用！
* XL-LightHouse虽然依赖hadoop、hbase、spark这种"大块头"的服务，但从用户使用的角度来说，其实是一款【非常轻量级】的服务，就算您没有大数据相关的研发运维经验也依然可以很好的驾驭。
* XL-LightHouse项目架构支持超大规模的数据量和高并发数据写入，可以支撑一线互联网大厂的数据量，后续我会逐步完善相关的压测文档。

### 背景

以互联网行业来说，在移动互联网发展比较成熟的现在，流量见顶，红利消失，企业竞争日趋惨烈，获取新增用户的成本日益增高。很多企业开始意识到不能一味的通过补贴、价格战、广告投放这种简单粗暴的方式抢占市场，这样的运作模式很难长时间维系。而通过精细化和数据化运营来降低成本、提升效率、最大化单用户价值的理念逐渐被越来越多的企业所接受。精细化和数据化运营的前提是要建立起一套完善的数据指标体系，借助这个数据指标体系企业可以有多方面的用途:
* 1、排查问题：数据化运营是让企业业务进入到一种"可控"的状态，帮助企业在业务运转不正常的时候，能够快速的判断出问题所在。
* 2、业务洞察：数据化运营是让业务运转的各个环节更加透明，帮助企业更清晰的看到目前的"短板"是在什么地方，辅助产品的优化迭代。
* 3、明确方向：数据化运营是培养敏锐的嗅觉，让企业可以更加准确的判断出市场的走势、捕捉到其中具有业务价值的信息。
* 4、科学试错：在试错成本日益高企的今天，数据化运营是帮助企业改变以往靠"拍脑袋"来做决定的方式，打破过往的经验主义，辅助决策者思考，快速验证想法，让企业减少成本更加科学的"试错"。
  随着企业对数据化运营重视程度的日益增加，必然会衍生出大量的数据统计需求。而XL-LightHouse是以流式大数据统计为切入点，推动流式统计在诸多行业内的快速普及和大规模应用，定位是以一套服务使用较少的服务器资源同时支撑数以万计、数十万计的流式数据统计需求的大数据平台，致力于应对这种呈现"井喷"态势的流式数据统计需求所带来的一系列问题，寄希望于通过更加贴合场景、更具有实用价值的技术方案帮助企业降低数据化运营方面的成本。

### 收益
XL-LightHouse可以帮助企业更快速的搭建起一套较为完善的、稳定可靠的数据化运营体系，节省企业在数据化运营方面的投入，主要体现在以下几个方面：
* 减少企业在流式大数据统计方面的研发成本和数据维护成本。
* 帮助企业节省时间成本，辅助互联网产品的快速迭代。
* 为企业节省较为可观的服务器运算资源。
* 便于数据在企业内部的共享和互通。
  此外，XL-LightHouse对中小企业友好，它大大降低了中小企业使用流式大数据统计的技术门槛，通过简单的页面配置和数据接入即可应对繁杂的流式数据统计需求。
  
###  一键部署

-  https://dtstep.com/archives/4257.html

### Hello World 使用范例

完整版使用示例请查阅：[HelloWorld](https://dtstep.com/archives/4301.html)


#####  范例一：首页ICON区域用户行为数据统计
<img src="https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/4301-2/1.png"  width="300px" height="200px" />

该区域包含3个Tab，每个Tab有多个业务ICON图标，用户手动滑动可切换Tab，假设针对该ICON区域我们有如下数据指标需求：

```
点击量：
1、每5分钟_点击量
2、每5分钟_各ICON_点击量
3、每小时_点击量
4、每小时_各ICON_点击量
5、每天_总点击量
6、每天_各Tab_总点击量
7、每天_各ICON_总点击量

点击UV:
1、每5分钟_点击UV
2、每小时_点击UV
3、每小时_各ICON_点击UV
4、每天_总点击UV
5、每天_各ICON_总点击UV
```

+ 定义元数据结构：

| 字段 | 字段类型 | 描述 |  |
| --- | --- | --- | --- |
| user_id | string | 用户标识 |  |
| tab_id | string | Tab栏 | tab1、tab2、tab3 |
| icon_id | string | 美食团购、酒店民宿、休闲玩乐、打车 ...|  |

+ 上报元数据时机

用户点击ICON图标时上报相应埋点数据

+ 配置统计项

<img src="https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/4301-2/2.png"  width="800px" height="400px" />

+  查看统计结果

<img src="https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/4301-2/3.png"  width="800px" height="400px" />

#####  范例二：移动支付订单数据统计

##### 1、 支付成功订单数据统计

+ 统计需求梳理

```
订单量：
1、每10分钟_订单量
2、每10分钟_各商户_订单量
3、每10分钟_各省份_订单量
4、每10分钟_各城市_订单量
5、每小时_订单量
6、每天_订单量
7、每天_各商户_订单量
8、每天_各省份_订单量
9、每天_各城市_订单量
10、每天_各价格区间_订单量
11、每天_各应用场景_订单量

交易金额：
1、每10分钟_成交金额
2、每10分钟_各商户_成交金额top100
3、每10分钟_各省份_成交金额
4、每10分钟_各城市_成交金额
5、每小时_成交金额
6、每小时_各商户_成交金额
7、每天_成交金额
8、每天_各商户_成交金额
9、每天_各省份_成交金额
10、每天_各城市_成交金额
11、每天_各应用场景_成交金额

下单用户数：
1、每10分钟_下单用户数
2、每10分钟_各商户_下单用户数
3、每10分钟_各省份_下单用户数
4、每10分钟_各城市_下单用户数
5、每小时_下单用户数
6、每天_下单用户数
7、每天_各商户_下单用户数
8、每天_各省份_下单用户数
9、每天_各城市_下单用户数
10、每天_各价格区间_下单用户数
11、每天_各应用场景_下单用户数
```
+ 定义元数据


| 字段 | 字段类型 | 描述 |  |
| --- | --- | --- | --- |
| userId | string | 用户ID |  |
| orderId | string | 订单ID |  |
| province | string | 用户所在省份 |  |
| city | string | 用户所在城市 |  |
| dealerId | string | 商户ID |  |
| scene | string | 支付场景 | 电商、外卖、餐饮、娱乐、游戏 ... |
| amount | numeric | 订单金额 |  |

+ 消息上报时机

用户支付成功后上报原始消息数据。

+ 配置统计消息

<img src="https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/4301-2/5.png"  width="800px" height="450px" />


##### 2、 订单支付状态数据监控

我这里假设订单有四种状态：支付成功、支付失败、超时未支付、订单取消。

```
订单量：
1、每10分钟_各状态_订单量
2、每10分钟_各商户_各状态_订单量
1、每天_各状态_订单量
2、每天_各商户_各状态_订单量

订单异常率:
1、每10分钟_订单异常率
2、每10分钟_各商户_订单异常率
3、每小时_订单异常率
4、每天_订单异常率
5、每天_各商户_订单异常率

支付失败用户数统计:
1、每5分钟_支付失败用户数
```

+ 定义元数据

| 字段 | 字段类型 | 描述 |  |
| --- | --- | --- | --- |
| userId | string | 用户ID |  |
| province | string | 用户所在省份 |  |
| city | string | 用户所在城市 |  |
| dealerId | string | 商户ID |  |
| orderId | string | 订单ID |  |
| state | string | 订单支付状态 | 1:支付成功、2：支付失败、3：超时未支付 4：订单取消 |


+ 配置统计项

<img src="https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/4301-2/6.png"  width="800px" height="500px" />


+ 查看统计结果

<img src="https://lighthousedp-1300542249.cos.ap-nanjing.myqcloud.com/4301-2/7.png?t=1"  width="800px" height="420px" />

### 更多适用场景举例

- 资讯类场景使用演示 <a href="https://dtstep.com/archives/4262.html" target="_blank" rel="noopener">dtstep.com/archives/4262.html</a>
- 电商类场景使用演示 <a href="https://dtstep.com/archives/4286.html" target="_blank" rel="noopener">dtstep.com/archives/4286.html</a>
- 即时通讯类场景使用演示 <a href="https://dtstep.com/archives/4291.html" target="_blank" rel="noopener">dtstep.com/archives/4291.html</a>
- 技术类场景使用演示  <a href="https://dtstep.com/archives/4298.html" target="_blank" rel="noopener">dtstep.com/archives/4298.html</a>

### 版权声明

为保障创作者的合法权益以及支持XL-LightHouse项目的发展，本项目在Apache2.0开源协议的基础上，增加如下补充条款，如果以下条款与Apache2.0协议内容有所冲突，以该补充条款为准。
* 1、企业或机构内部使用XL-LightHouse源程序以及XL-Formula标准不受任何限制，但不可删除源程序中的版权声明、原作者邮箱以及项目网址等信息。
* 2、企业、机构或个人如有以下行为需要得到本人许可并支付一定比例的授权费用。

a、销售直接或间接包含XL-LightHouse源程序（超过1000行源码）的软硬件产品或服务。

b、销售直接或间接借鉴XL-LightHouse系统设计方法的软硬件产品或服务。

c、销售直接或间接包含XL-Formula设计标准（包括在此基础上修改演变而来的标准）的软硬件产品或服务。

d、销售直接或间接依赖XL-LightHouse或XL-Formula相关的数据指标可视化产品或服务，包括但不限于插件、终端等任何形式的产品或服务。
如上所述的“服务”指为购买者提供统计数据类服务或提供相应产品的技术支持维护服务，为避免不必要的版权纠纷， 在销售相关产品或服务前请务必查阅【<a href="https://dtstep.com/archives/4206.html" target="_blank" rel="noopener">版权声明</a>】，本协议最终解释权归原作者所有。

### 相关文档

##### 1、项目介绍

- <a href="https://dtstep.com/archives/4455.html" target="_blank" rel="noopener">dtstep.com/archives/4455.html</a>

##### 2、Git地址
- https://github.com/xl-xueling/xl-lighthouse.git
- https://gitee.com/mirrors/XL-LightHouse.git

##### 3、交流社区

- <a href="https://dtstep.com" target="_blank" rel="noopener">DTStep</a>

##### 4、项目设计

- <a href="https://dtstep.com/archives/4227.html" target="_blank" rel="noopener">dtstep.com/archives/4227.html</a>

##### 5、一键部署

- <a href="https://dtstep.com/archives/4257.html" target="_blank" rel="noopener">dtstep.com/archives/4257.html</a>

##### 6、XL-Formula使用

- <a href="https://dtstep.com/archives/4215.html" target="_blank" rel="noopener">dtstep.com/archives/4215.html</a>

##### 7、Web服务操作说明

- <a href="https://dtstep.com/archives/4233.html" target="_blank" rel="noopener">dtstep.com/archives/4233.html</a>

##### 8、Hello World

- <a href="https://dtstep.com/archives/4301.html" target="_blank" rel="noopener">dtstep.com/archives/4301.html</a>

##### 9、适用场景

- 资讯类场景使用演示 <a href="https://dtstep.com/archives/4262.html" target="_blank" rel="noopener">dtstep.com/archives/4262.html</a>
- 电商类场景使用演示 <a href="https://dtstep.com/archives/4286.html" target="_blank" rel="noopener">dtstep.com/archives/4286.html</a>
- 即时通讯类场景使用演示 <a href="https://dtstep.com/archives/4291.html" target="_blank" rel="noopener">dtstep.com/archives/4291.html</a>
- 技术类场景使用演示  <a href="https://dtstep.com/archives/4298.html" target="_blank" rel="noopener">dtstep.com/archives/4298.html</a>

##### 10、版权声明

- <a href="https://dtstep.com/archives/4206.html" target="_blank" rel="noopener">dtstep.com/archives/4206.html</a>

##### 11、使用反馈

- <a href="https://dtstep.com/community/ldp-issue" target="_blank" rel="noopener">dtstep.com/community/ldp-issue</a>

##### 12、依赖组件
- <a href="https://dtstep.com/archives/4445.html" target="_blank" rel="noopener">dtstep.com/archives/4445.html</a>
