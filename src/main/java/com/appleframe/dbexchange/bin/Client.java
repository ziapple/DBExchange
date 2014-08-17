/**
 * 
 */
package com.appleframe.dbexchange.bin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ResourceUtils;

import com.appleframe.dbexchange.db.DBExecutor;
import com.appleframe.dbexchange.db.DomParser;
import com.appleframe.dbexchange.db.SQLGenerator;
import com.appleframe.dbexchange.db.metadata.map.Mapping;

/**
 * 启动服务端程序
 * @author Administrator
 *
 */
public class Client {
	public static List<Mapping> mappings = new ArrayList<Mapping>(0);
	
	public static List<Mapping> getMappingInstance(){
		synchronized (mappings) {
			if(mappings.isEmpty()){
				//解析配制文件
				System.out.println("正在解析配制文件...");
				File file;
				try {
					file = ResourceUtils.getFile("classpath:mapping/mapping.xml");
					mappings = DomParser.toMappings(file);
					return mappings;
				} catch (Exception e) {
					System.out.println("解析配制文件失败..." + e.getMessage());
				}
			}else{
				return mappings;
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) throws Exception {
        //启动Spring环境
		System.out.println("正在启动Spring环境...");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("app-client.xml");
		System.out.println("Spring环境启动成功...");

		//获取数据库执行器,带事务的
		DBExecutor dbExecutor = ctx.getBean(DBExecutor.class);
		
		//获取SQL工厂
		SQLGenerator sqlGenerator = (SQLGenerator)ctx.getBean("sqlFactory");
		for(Mapping mapping : getMappingInstance()){
			String pTable = mapping.getpTable();
			if(!dbExecutor.isTableExit(pTable)){
				String sql = sqlGenerator.getCreateTableSql(mapping);
				System.out.println("正在创建" + pTable + ":" + sql);
				try{
					dbExecutor.executeSql(sql);
					System.out.println(pTable + "创建成功");
				}catch(Exception e){
					System.out.println(pTable + "创建失败:" + e.getMessage());
				}
				
				//创建触发器
				List<String> lst = sqlGenerator.getTriggerSql(mapping);
				for(String sqlTrigger : lst){
					System.out.println("正在创建触发器:" + sqlTrigger);
					try{
						dbExecutor.executeSql(sqlTrigger);
						System.out.println("触发器创建成功");
					}catch(Exception e){
						System.out.println("触发器创建失败:" + e.getMessage());
					}
				}
			}
		}
    }
}
