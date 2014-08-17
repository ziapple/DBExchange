/**
 * 
 */
package com.appleframe.dbexchange.db;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据库执行器
 * @author Administrator
 *
 */
@Service
@Transactional
public class DBExecutor {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SQLGenerator sqlGenerator;
	
	/**
	 * 执行传递过来的Xml语句
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void executeXml(String xml){
		List<String> list = sqlGenerator.getExecuteSql(xml);
		for(String sql : list){
			System.out.println(sql);
			try{
				jdbcTemplate.execute(sql);  
			}catch(Exception e){
				//出错写到一张出错表里面
				jdbcTemplate.execute("insert into JMS_ERROR(sql, reason) values('" + sql + "','" + e.getMessage() + "')");
			}
		}
	}
	
	/**
	 * 执行
	 * @param sql
	 */
	public void executeSql(String sql){
		jdbcTemplate.execute(sql);		
	}
	
	/**
	 * 返回List
	 * @param sql
	 * @return
	 */
	public List<Map<String,Object>> queryForList(String sql){
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 判断表是否存在
	 * @param table
	 * @return
	 */
	public boolean isTableExit(String table){
		String sql = "select * from " + table;
		try{
			jdbcTemplate.execute(sql);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static void main(String[] args) {
		// 下面是需要解析的xml字符串例子
		String xmlString = "<dataset table='p_user'><key name='p_id' type='number'><data>1</data></key>" +
				"<rows><row type='add|update|delete'>" +
				"<column name='p_id' type='nu mber'><data>1</data></column>" +
				"<column name='p_name' type='string'><data>张三</data></column>" +
				"<column name='p_address' type='string'><data>北京市石景山远洋山水</data></column>" +
				"<column name='p_createtime' type='date'><data>2014-3-15 08:33:34</data></column></row></rows></dataset>";

		DBExecutor db = new DBExecutor();
		db.executeXml(xmlString);
	}
}
