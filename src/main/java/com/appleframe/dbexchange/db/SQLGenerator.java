/**
 * 
 */
package com.appleframe.dbexchange.db;

import java.util.List;

import org.springframework.stereotype.Component;

import com.appleframe.dbexchange.db.metadata.map.Mapping;

/**
 * Sql生成器
 * @author Administrator
 *
 */
@Component
public interface SQLGenerator {
	//同步失败记录表名
	public static String MQ_ERR_TABLE = "mq_err";
	
	/**
	 * 根据Xml数据返回可执行的Sql语句
	 * @param xml
	 * @return
	 */
	public List<String> getExecuteSql(String xml);
	
	/**
	 * 生成触发器Sql语句
	 * @return
	 */
	public List<String> getTriggerSql(Mapping mapping);
	
	/**
	 * 生成插入字段 
	 * @param mapping
	 * @param prefix
	 * @return
	 */
	public String getInsertFields(Mapping mapping,String table, String prefix);
	
	/**
	 * 生成创建表语句
	 * @return
	 */
	public String getCreateTableSql(Mapping mapping);
	
	/**
	 * 获取新插入的Sql语句
	 * @param mapping
	 * @return
	 */
	public String getScanQuery(Mapping mapping);
	
	/**
	 * 获取扫描之后的Sql处理语句
	 * @param mapping
	 * @return
	 */
	public String getAfterScanQuery(Mapping mapping);
	
	/**
	 * 获取错误记录表创建语句
	 * @return
	 */
	public String getCreateErrTableSql();
}
