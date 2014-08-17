package com.appleframe.dbexchange.quartz;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appleframe.common.SpringContextHolder;
import com.appleframe.dbexchange.bin.Client;
import com.appleframe.dbexchange.db.DBExecutor;
import com.appleframe.dbexchange.db.DomParser;
import com.appleframe.dbexchange.db.SQLGenerator;
import com.appleframe.dbexchange.db.metadata.map.Mapping;

/**
 * @Description 查铺
 * @version V1.0
 */
@Component
public class DBScanScheduler{
	protected Logger log = Logger.getLogger(DBScanScheduler.class);
	private static long i = 1;
	
	@Autowired
	private DBThreadPoolTask task;
	
	@Autowired
	private DBExecutor dbExecutor;
	
	public void execute() throws JobExecutionException {
		try {
			//为了防止数据库连接过大, 同时只有一个线程进行扫描
			if(i < 2){
				SQLGenerator sqlGenerator = (SQLGenerator)SpringContextHolder.getBean("sqlFactory");
				
//				log.info("正在进行数据库扫描..." + (i++));
				List<Mapping> mappings = Client.getMappingInstance();
				//对数据库进行扫描
				for(Mapping mapping : mappings){
					String sql = sqlGenerator.getScanQuery(mapping);
					List<Map<String,Object>> rows = dbExecutor.queryForList(sql);
					if(!rows.isEmpty()){
						System.out.println(mapping.getpTable() + "表扫描到记录" + rows.size() + "条");
						String xml = DomParser.toXmlMessage(rows, mapping);
						task.execute(xml);
					}
					
					//扫到之后将这些记录置为R
					sql = sqlGenerator.getAfterScanQuery(mapping);
					dbExecutor.executeSql(sql);
				}
				
				i--;
			}
		} catch (Throwable en) {
			log.error("************失败************", en);
		}
	}
}
