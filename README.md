DBExchange
==========

基于ActiveMQ的数据交换中间件

一、 工具介绍
DBExchange是一款适用于不同异构数据库进行数据传输的中间件。其主要功能如下：
1.	基于开源消息中间件ActiveMQ进行数据传输，保证数据的可靠性和稳定性；
2.	支持Oracel、SQL Server、MySQL等多种数据库数据传输；
3.	通过简单的数据映射文件配制方式，可实现复杂的数据传输逻辑，提供简单行级传输、Sql语句传输、Web Service调用等多种逻辑处理； 
4.	支持多终端大数据量传输，可识别来自不同的数据终端发起的数据传输任务；
5.	提供对数据源结构进行比对、转换、整合等数据加工。
二、工具特点
    DBExchange为提高产品品质，强化用户使用体验，具备以下特点：
1.	提供安全可靠的数据传输能力，保证数据的完整性、一致性，使传输的数据不丢失、不重复，次序不乱；
2.	除了采用一般的数据加密传输技术外，并采用SSL/TLS协议实现安全连接与传输；
3.	配置简单，操作便捷，“二步配置一步启动操作”即可实现全部功能；

三、使用说明
    参数配置：
1．	app-client.properties配置文件修改。
修改exchange-home\conf目录下的app-client.properties文件内容，主要包括：
（1）配本地数据源：在文件提供的集中数据库类型中，选择客户端对应的数据库类型，并修改数据库名称、用户名和密码信息；
（2）配置服务器ip地址：修改activemq.brokerUrl中内容为客户端ip地址；
（3）配置dbExchange.dialect为客户端的数据库类型，如oracle、sqlserver等。
2．配置exchange-home\conf\mapping\mapping.xml（数据库映射文件）：简单的直接输入表结构；复杂的可以用sql语句或webseivice方式实现。

运行：
1 bin/init.bat 初始化客户端表，如已建立过客户端表则运行bin/recreate.bat
2 bin/scan.bat 启动扫描，开始实时监控客户端数据库变化

