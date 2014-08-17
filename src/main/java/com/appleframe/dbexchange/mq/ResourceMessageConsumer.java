package com.appleframe.dbexchange.mq;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.appleframe.common.SpringContextHolder;
import com.appleframe.dbexchange.db.DBExecutor;
import com.appleframe.dbexchange.db.SQLGenerator;
import com.appleframe.dbexchange.db.impl.DefaultSql;
import com.appleframe.dbexchange.db.metadata.ds.Message;
import com.appleframe.utils.date.DateUtils;

public class ResourceMessageConsumer {
	private ThreadPoolTaskExecutor threadPoolExecutor;
	
	@Autowired
	private DBExecutor dbExecutor;
	
	public void execute(Message msg) {
		System.out.println("接收消息:" + msg.getXml());
		
		//处理任务,将Message转化成Sql
		SQLGenerator sqlGenerator = (SQLGenerator)SpringContextHolder.getBean("sqlFactory");
		if(msg.getType().equals("xml")){
			List<String> lst = sqlGenerator.getExecuteSql(msg.getXml());
			String sql = "";
			try{
				for(int i = 0; i < lst.size(); i++){
					sql = lst.get(i);
					System.out.print(sql);
					dbExecutor.executeSql(sql);
				}
			}catch(Exception e){
				System.out.println("数据库执行失败,详细:" + e.getMessage());
				String err = "insert into " + SQLGenerator.MQ_ERR_TABLE + "(targetsql, xml, error, createtime) values('" + DefaultSql.dealStringSql(sql) + "','" + DefaultSql.dealStringSql(msg.getXml()) + "','" + DefaultSql.dealStringSql(e.getMessage()) + "','" + DateUtils.getShortCurrentDate() + "');";
				System.out.println(err);
				dbExecutor.executeSql(err);
			}
		}
	}

	public void setThreadPoolExecutor(ThreadPoolTaskExecutor threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;
	}
}
