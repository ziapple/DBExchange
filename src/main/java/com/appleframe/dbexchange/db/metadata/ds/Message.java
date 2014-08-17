package com.appleframe.dbexchange.db.metadata.ds;

import java.io.Serializable;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4378526329085400199L;

	/**
	 * 消息类型
	 * xml : xml语句
	 * cmd : 命令语句
	 */
	private String type;
	
	//消息xml数据
	private String xml;

	public Message(String type, String xml) {
		super();
		this.type = type;
		this.xml = xml;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
}
