/**
 * 
 */
package com.appleframe.dbexchange.db.metadata.map;

import java.util.ArrayList;
import java.util.List;

/**
 * 主映射文件
 * @author Administrator
 *
 */
public class Mapping {
	//映射名称,可以为空
	private String name;
	
	//源表
	private String sTable;
	
	//P表,可以为空
	private String pTable;
	
	//C表
	private String cTable;
	
	//同步类型,all|add|update|delete|au|ad|ud
	private String type;
	
	//字段队映字段
	private List<FieldMapping> fieldMappings = new ArrayList<FieldMapping>(0);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getsTable() {
		return sTable;
	}

	public void setsTable(String sTable) {
		this.sTable = sTable;
	}

	public String getpTable() {
		return pTable;
	}

	public void setpTable(String pTable) {
		this.pTable = pTable;
	}

	public String getcTable() {
		return cTable;
	}

	public void setcTable(String cTable) {
		this.cTable = cTable;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<FieldMapping> getFieldMappings() {
		return fieldMappings;
	}

	public void setFieldMappings(List<FieldMapping> fieldMappings) {
		this.fieldMappings = fieldMappings;
	}
	
	/**
	 * 获取主键
	 * @return
	 */
	public Field getKeyFiled(){
		for(FieldMapping fm : fieldMappings){
			Field f = fm.getcField();
			if(f.isNullable())
				return f;
		}
		
		return null;
	}
}
