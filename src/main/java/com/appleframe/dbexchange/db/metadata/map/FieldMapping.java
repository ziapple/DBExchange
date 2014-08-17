package com.appleframe.dbexchange.db.metadata.map;

/**
 * 映射字段信息
 * @author Administrator
 *
 */
public class FieldMapping {
	//主表字段
	private Field pField = new Field();
	
	//从表字段
	private Field cField = new Field();

	public Field getpField() {
		return pField;
	}

	public void setpField(Field pField) {
		this.pField = pField;
	}

	public Field getcField() {
		return cField;
	}

	public void setcField(Field cField) {
		this.cField = cField;
	}
}
