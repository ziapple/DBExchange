package com.appleframe.dbexchange.db.metadata.map;

public class Field {
	//字段名
	private String name;
	
	//字段类型
	private String type;
	
	//数据库字段类型
	private String dbType;
	
	//字段长度
	private String length;
	
	//生成P表的字段名
	private String pName;
	
	//是否为主键
	private boolean primaryKey = false;
	
	//是否可为空
	private boolean nullable = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
}
