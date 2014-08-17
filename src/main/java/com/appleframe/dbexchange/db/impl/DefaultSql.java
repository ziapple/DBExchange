/**
 * 
 */
package com.appleframe.dbexchange.db.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.appleframe.dbexchange.db.DomParser;
import com.appleframe.dbexchange.db.metadata.ds.Column;
import com.appleframe.dbexchange.db.metadata.ds.DataSet;
import com.appleframe.dbexchange.db.metadata.ds.Key;
import com.appleframe.dbexchange.db.metadata.ds.Row;
import com.appleframe.dbexchange.db.metadata.map.Field;
import com.appleframe.dbexchange.db.metadata.map.FieldMapping;
import com.appleframe.dbexchange.db.metadata.map.Mapping;

/**
 * Sql生成器
 * @author Administrator
 *
 */
@Component
public abstract class DefaultSql {
	/**
	 * 根据Xml数据返回可执行的Sql语句
	 * @param xml
	 * @return
	 */
	public List<String> getExecuteSql(String xml){
		List<String> list = new ArrayList<String>();
		DataSet ds = DomParser.toDataSet(xml);
		for(Row row : ds.getRows()){
			String sql = "", fields = "", values = "";			
			
			//插入操作
			if(row.getType().equals("insert")){
				for(Column column : row.getColumns()){
					fields += "," + column.getName();
					String type = column.getType();
					if(type.equals("number")){//数字型
						values += "," + column.getData();
					}else if(type.equals("string")){//文本型
						values += "," + "'" + column.getData() + "'";
					}else if(type.equals("date")){//日期型
						values += "," + "'" + column.getData() + "'";
					}
				}
				
				if(fields != null){
					fields = fields.substring(1);
				}
				if(values != null){
					values = values.substring(1);
				}
				
				sql = "insert into " + ds.getTable() + "(" + fields + ") values(" + values + ")";
			}
			
			//更新
			if(row.getType().equals("update")){
				Key key = ds.getKey();
				String exp = null,set = null;
				if(key.getType().equals("string")){
					exp = key.getName() + " = '" + key.getData() + "'";
				}else{
					exp = key.getName() + " = " + key.getData();
				}
				
				for(Column column : row.getColumns()){	
					String type = column.getType();
					if(type.equals("number")){//数字型
						set += "," + column.getName() + " = " + column.getData();
					}else if(type.equals("string")){//文本型
						set += "," + column.getName() + " = '" + column.getData() + "'";
					}else if(type.equals("date")){//日期型
						set += "," + column.getName() + " = '" + column.getData() + "'";
					}
				}
				
				if(set != null){
					set = set.substring(1);
				}
				
				sql = "update " + ds.getTable() + " set " + set + " where " + exp;
			}
			
			//删除
			if(row.getType().equals("update")){
				Key key = ds.getKey();
				String exp = null,set = null;
				if(key.getType().equals("string")){
					exp = key.getName() + " = '" + key.getData() + "'";
				}else{
					exp = key.getName() + " = " + key.getData();
				}
				sql = "delete from " + ds.getTable() + " where " + exp; 
			}
			
			//增加sql语句
			list.add(sql);
		}
		return list;
	}
	
	/**
	 * 生成插入字段 
	 * @param mapping
	 * @param prefix
	 * @return
	 */
	public String getInsertFields(Mapping mapping,String table, String prefix){
		String str = "";
		for(FieldMapping fm : mapping.getFieldMappings()){
			if(prefix == null){
				if(table.equals("pTable"))
					str += "," + fm.getpField().getpName();
				else if(table.equals("sTable"))
					str += "," + fm.getpField().getName();
			}else{
				if(table.equals("pTable"))
					str += "," + prefix + "." + fm.getpField().getpName();
				else if(table.equals("sTable"))
					str += "," + prefix + "." + fm.getpField().getName();
			}
			
		}
		if(str.length()!=0)
			str = str.substring(1);
		
		return str;
	}
	
	/**
	 * 建表语句
	 * @param mapping
	 * @return
	 */
	public String getCreateTableSql(Mapping mapping) {
		String innerSql = "";		
		for(FieldMapping fm : mapping.getFieldMappings()){ 
			Field f = fm.getpField();
			innerSql += f.getpName() + " " + f.getDbType();
			if(f.isPrimaryKey()){
				innerSql += " primary key";
			}else if(!f.isNullable()){
				innerSql += " not null";
			}
			innerSql += ",";
		}
		innerSql += "FLAG varchar(2) not null";
		/**
		if(innerSql.length()!=0)
			innerSql = innerSql.substring(0, innerSql.length()-1);
		**/
		
		String sql = "create table " + mapping.getpTable() + "(" + innerSql + ")";
		return sql;
	} 
	
	/**
	 * 获取新插入的Sql语句
	 * @param mapping
	 * @return
	 */
	public String getScanQuery(Mapping mapping){
		String pTable = mapping.getpTable();
		String sql = "select * from " + pTable + " where FLAG='N'";
		return sql;
	}
	
	/**
	 * 获取新插入之后的Sql语句
	 * @param mapping
	 * @return
	 */
	public String getAfterScanQuery(Mapping mapping){
		String pTable = mapping.getpTable();
		String sql = "update " + pTable + " set FLAG='R' where FLAG='N'";
		return sql;
	}
	
	/**
	 * 处理Sql语句
	 * @param sql
	 * @return
	 */
	public static String dealStringSql(String sql){
		if(sql != null){
			sql = sql.replace("'", "\\'");
			return sql;
		}
		return null;
	}
	
	public static void main(String[] args){
		System.out.println(dealStringSql("'jsdgsdg' hello 'sgdsg'"));
	}
}
