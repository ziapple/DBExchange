package com.appleframe.dbexchange.db.impl;

import java.util.ArrayList;
import java.util.List;

import com.appleframe.dbexchange.db.SQLGenerator;
import com.appleframe.dbexchange.db.metadata.map.Mapping;

public class MySQLImpl extends DefaultSql implements SQLGenerator {
	
	/**
	 * 生成触发器Sql语句
	 * @return
	 */
	@Override
	public List<String> getTriggerSql(Mapping mapping){
		List<String> list = new ArrayList<String>();
		
		String name = "trg_" + mapping.getpTable() + "_for_" + mapping.getType(); 
		
		String type = mapping.getType();
		if(type.equals("insert")){//新增语句
			String sql = "drop trigger if exists " + name + ";";
			list.add(sql);
			
			sql = "create trigger " + name + " after insert" + " on " + mapping.getsTable() + " for each row begin ";
			sql += "insert into " + mapping.getpTable() + "(" + getInsertFields(mapping, "pTable", null) + ", FLAG) " +
					"values(" + getInsertFields(mapping, "sTable", "NEW") + ",'N'); ";
			sql += "end;";
			list.add(sql);
		}else if(type.equals("update")){
			
		}
		
		return list;
	}

	//获取创建错误记录表的Sql语句
	public String getCreateErrTableSql(){
		String sql = "create table " + SQLGenerator.MQ_ERR_TABLE + "(id bigint(8) NOT NULL AUTO_INCREMENT ," +
				"targetsql text NULL, xml text NULL, error text NULL ,createtime date NULL, PRIMARY KEY (id));";
		return sql;
	}
}
