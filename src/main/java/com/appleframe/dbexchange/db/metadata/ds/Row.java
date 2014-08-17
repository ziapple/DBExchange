package com.appleframe.dbexchange.db.metadata.ds;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据行
 * @author Administrator
 *
 */
public class Row {
	//行类型
	public String type;
	
	//字段值
	public List<Column> columns = new ArrayList<Column>(0);

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
}
