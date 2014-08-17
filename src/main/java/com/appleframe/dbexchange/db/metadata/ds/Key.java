package com.appleframe.dbexchange.db.metadata.ds;

/**
 * 主键
 * @author Administrator
 *
 */
public class Key {
	//字段名
		private String name;
		
		//字段类型
		private String type;
		
		//字段值
		private String data;

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

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
}
