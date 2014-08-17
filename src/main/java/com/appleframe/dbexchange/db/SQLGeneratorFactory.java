package com.appleframe.dbexchange.db;

import org.springframework.beans.factory.FactoryBean;

import com.appleframe.dbexchange.db.impl.MySQLImpl;
import com.appleframe.dbexchange.db.impl.OracleSqlImpl;

/**
 * 根据名称获取数据库方言
 * @author Administrator
 *
 */
public class SQLGeneratorFactory implements FactoryBean {
	//数据库类型
	private String dialect;
	
	public Object getObject() throws Exception {
		if(dialect.equals("oracle"))
			return new OracleSqlImpl();
		else if(dialect.equals("mysql"))
			return new MySQLImpl();
		
		return null;
	}
	
	public Class getObjectType() {
		return Double.class;
	}
	
	public boolean isSingleton() {
		return true;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
}
