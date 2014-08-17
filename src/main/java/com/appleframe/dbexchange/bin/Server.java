/**
 * 
 */
package com.appleframe.dbexchange.bin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.appleframe.dbexchange.db.DBExecutor;
import com.appleframe.dbexchange.db.SQLGenerator;

/**
 * 启动服务端程序
 * @author Administrator
 *
 */
public class Server {

	public static void main(String[] args) throws Exception {  
        //启动Spring环境
		System.out.println("正在启动Spring环境...");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("app-server.xml");
		
		//获取数据库执行器,带事务的
		DBExecutor dbExecutor = ctx.getBean(DBExecutor.class);
				
		//获取SQL工厂
		SQLGenerator sqlGenerator = (SQLGenerator)ctx.getBean("sqlFactory");
				
		//创建数据库表
		if(!dbExecutor.isTableExit(SQLGenerator.MQ_ERR_TABLE)){
			String sql = sqlGenerator.getCreateErrTableSql();
			System.out.println("正在创建" + SQLGenerator.MQ_ERR_TABLE + ":" + sql);
			try{
				dbExecutor.executeSql(sql);
				System.out.println(SQLGenerator.MQ_ERR_TABLE + "创建成功");
			}catch(Exception e){
				System.out.println(SQLGenerator.MQ_ERR_TABLE + "创建失败:" + e.getMessage());
			}
		}
    }
}
