/**
 * 
 */
package com.appleframe.dbexchange.db.metadata.ds;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据集
 * @author Administrator
 *
 */
public class DataSet {
	//表名
	private String table;
	
	//主键
	private Key key;
	
	//行数据
	private List<Row> rows = new ArrayList<Row>(0);

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
}
