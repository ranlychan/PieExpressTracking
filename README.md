# 派查查 ：一个WearOS智能手表系统上的快递查询应用  ![GitHub release (latest by date)](https://img.shields.io/github/v/release/ranlychan/PieExpressTracking) ![GitHub all releases](https://img.shields.io/github/downloads/ranlychan/PieExpressTracking/total) 
![032110372196_080c1159b7fc0d53d96ae86e10cfbb1c3.jpg](https://s2.loli.net/2023/03/21/Rrsa8bZdC6Fyjkl.png)

## 前言 ##

​	在学习面向对象程序设计课程的时候，收获颇多，最后的课程报告选题选择了“Android开发”。但是安卓手机上好的应用数不胜数，自己不想重蹈覆辙做别人做过不知道多少遍的东西，就选择了当时相对Android手机软件开发比较小众的WearOS系统软件开发，做了TicWatch的问问商店里第一款腕上快递查询应用：派查查PieExpress。这也是我第一次做安卓开发，如有不足，还请海涵。



## 项目概述 ##

**运行环境：** Google WearOS 国内版

**软件类型：** 快递查询工具

**基本功能：**

- 快递查询：在手表上输入快递运单号后，联网查询快递的运输信息，包括运输状态、到达时间和地点等。
- 快递收藏：输入的运单号和查询的快递信息可以本地保存，方便下次查看，可以为运单号写备注，方便知道是哪个快递。
- 语音录入：输入运单号或运单备注时可以语音输入（调用TicWatch语音接口实现，小米手表等可能无法使用）

**运行截图：**

**项目地址：** https://github.com/ranlychan/PieExpressTracking


## 设计背景 ##

​	如今，人们在网络购物时常常需要关注物流信息，而用智能手机方便快捷地获取快递信息技术已经较为成熟，应用也已较为广泛。例如在Android应用市场中比较热门的“菜鸟裹裹”，“快递100”等应用，但**使用Android智能可穿戴设备获取快递信息的服务或应用却较为少见**。以Android智能手表为例，因为在中国大陆无法连接到Google Play商店，国内基于Wear OS的智能手表的应用商店往往都是另起炉灶，多而不全，基本无法找到适配手表的快递查询应用。而基于Watch OS的Apple Watch因为有着成熟的Appstore生态支持，有较多的手表端物流查询应用以及依赖智能手机的查询应用，例如淘宝针对Apple Watch推出的客户端目前具有的待收货功能可追踪在淘宝购买的商品物流信息；Appstore收费应用“追快递专业版“支持IOS客户端添加和查询多家快递公司物流信息并实时推送到Apple Watch上。针对上述Wear OS快递查询应用少的情况，本文将着手介绍基于Wear OS的快递查询追踪应用的设计与实现。



## 系统架构 ##

快递物流信息查询系统将使用C/S(Client/Server)模式开发，因为初步来说，基于智能手表的Client发送获取物流信息的请求，由物流公司Server或专门整合多家物流信息的中间服务器通过API等形式提供所需物流信息，然后再由智能手表Client呈现给用户。这意味着有两条获取物流信息的方式：

​	1)   直接通信：与需要查询的物流公司服务器直接通信，直接从物流公司获取所需物流信息，如从顺丰速递等获取。

​	2)   间接通信：由整合多家物流信息的物流信息公司，如快递鸟，充当中间服务器的角色，Client与其通信相当于间接与多家其它物流公司Server通信。

其一，由于一般来说，对于用户而言，往往需要查询多家快递公司的物流信息，所以如果选择直接通信的方式，Client需要同时对接多家物流公司接口，而选择间接通信的方式，Client只需要对接中间服务器接口就能查询上百家物流公司的快递信息。

其二，物流公司的服务接口有可能发生变动，同时维护多家物流公司提供的接口所需维护工作量显然远大于只维护中间服务器提供的接口。

此外，考虑到搭载Wear OS的设备一般是Android智能手表，运算性能储存空间等都较为有限，对接多家物流公司的接口至少在代码量上就远大于只对接中间服务器接口，会使程序体积变大，不利于在智能手表这类设备上运行。综上考虑，本系统将选择间接通信的方式获取物流信息。



## 接口介绍 ##

分支release_1.2.2发布的v1.2.2版本使用快递鸟的即时查询API免费版。内容来自快递鸟API文档 [免费查询快递接口 物流即时查询API-快递鸟 (kdniao.com)](http://www.kdniao.com/api-track)

### 即时查询请求 ###

请求参数说明：

| 参数名称    | 类型   | 说明                                                         | 必须要求 |
| :---------- | :----- | :----------------------------------------------------------- | :------- |
| RequestData | String | 请求内容需进行URL(utf-8)编码。请求内容JSON格式，须和DataType一致。 | R        |
| EBusinessID | String | 商户ID，请在我的服务页面查看。                               | R        |
| RequestType | String | 请求指令类型：1002                                           | R        |
| DataSign    | String | 数据内容签名：把(请求内容(未编码)+AppKey)进行MD5加密，然后Base64编码，最后 进行URL(utf-8)编码。详细过程请查看Demo。 | R        |
| DataType    | String | 请求、返回数据类型：2-json；                                 | O        |

请求JSON示例：
```json
{
                "OrderCode": "",
                "ShipperCode": "SF",
                "LogisticCode": "118650888018"
                }
```



### 即时查询回复 ###

返回参数定义：

| 参数名称      | 类型   | 说明                               | 必须要求 |
| :------------ | :----- | :--------------------------------- | :------- |
| EBusinessID   | String | 用户ID                             | R        |
| OrderCode     | String | 订单编号                           | O        |
| ShipperCode   | String | 快递公司编码                       | R        |
| LogisticCode  | String | 物流运单号                         | O        |
| Success       | Bool   | 成功与否                           | R        |
| Reason        | String | 失败原因                           | O        |
| State         | String | 物流状态：2-在途中,3-签收,4-问题件 | R        |
| Traces        |        |                                    |          |
| AcceptTime    | String | 时间                               | R        |
| AcceptStation | String | 描述                               | R        |
| Remark        | String | 备注                               | O        |

返回JSON示例：

```json
 {
        "EBusinessID": "1109259",
        "OrderCode": "",
        "ShipperCode": "SF",
        "LogisticCode": "118461988807",
        "Success": true,
        "State": 3,
        "Reason": null,
        "Traces": [
        {
        "AcceptTime": "2014/06/25 08:05:37",
        "AcceptStation": "正在派件..(派件人:邓裕富,电话:18718866310)[深圳市]",
        "Remark": null
        },
        {
        "AcceptTime": "2014/06/25 04:01:28",
        "AcceptStation": "快件在 深圳集散中心 ,准备送往下一站 深圳 [深圳市]",
        "Remark": null
        },
        {
        "AcceptTime": "2014/06/25 01:41:06",
        "AcceptStation": "快件在 深圳集散中心 [深圳市]",
        "Remark": null
        },
        {
        "AcceptTime": "2014/06/24 20:18:58",
        "AcceptStation": "已收件[深圳市]",
        "Remark": null
        },
        {
        "AcceptTime": "2014/06/24 20:55:28",
        "AcceptStation": "快件在 深圳 ,准备送往下一站 深圳集散中心 [深圳市]",
        "Remark": null
        },
        {
        "AcceptTime": "2014/06/25 10:23:03",
        "AcceptStation": "派件已签收[深圳市]",
        "Remark": null
        },
        {
        "AcceptTime": "2014/06/25 10:23:03",
        "AcceptStation": "签收人是：已签收[深圳市]",
        "Remark": null
        }
        ] 
 } 

```

