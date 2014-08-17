package com.appleframe.dbexchange.db.impl;

import java.util.List;

import com.appleframe.dbexchange.db.SQLGenerator;
import com.appleframe.dbexchange.db.metadata.map.Mapping;

public class OracleSqlImpl extends DefaultSql implements SQLGenerator {

	@Override
	public List<String> getTriggerSql(Mapping mapping) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCreateErrTableSql() {
		// TODO Auto-generated method stub
		return null;
	}
}
