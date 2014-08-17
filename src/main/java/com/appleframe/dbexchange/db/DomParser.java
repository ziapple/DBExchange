package com.appleframe.dbexchange.db;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.appleframe.dbexchange.db.metadata.ds.Column;
import com.appleframe.dbexchange.db.metadata.ds.DataSet;
import com.appleframe.dbexchange.db.metadata.ds.Key;
import com.appleframe.dbexchange.db.metadata.ds.Row;
import com.appleframe.dbexchange.db.metadata.map.Field;
import com.appleframe.dbexchange.db.metadata.map.FieldMapping;
import com.appleframe.dbexchange.db.metadata.map.Mapping;

public class DomParser {
	
	/**
	 * 转化成DataSet
	 * @param xml
	 * @return
	 */
	public static DataSet toDataSet(String xml) {
		DataSet ds = new DataSet();
		Document doc = null;
		try {
			// 将字符串转为XML
			doc = DocumentHelper.parseText(xml); 

			//dataset
			Element rootElt = doc.getRootElement(); 
			String table = rootElt.attributeValue("table");
			ds.setTable(table);
			
			//key
			Element keyElement = rootElt.element("key");
			Key key = new Key();
			ds.setKey(key);
			key.setName(keyElement.attributeValue("name"));
			key.setType(keyElement.attributeValue("type"));
			key.setData(keyElement.elementTextTrim("data"));
			
			//获取row
			Element rowsElt = rootElt.element("rows");
			Iterator rowIter = rowsElt.elementIterator("row");
			while (rowIter.hasNext()) {
				Row row = new Row();
				ds.getRows().add(row);
				
				Element rowElt = (Element) rowIter.next();
				String type = rowElt.attributeValue("type");
				row.setType(type);
				
				Iterator clmIter = rowElt.elementIterator("column");
				while (clmIter.hasNext()) {
					Column column = new Column();
					row.getColumns().add(column);
					
					Element columnElt = (Element) clmIter.next();
					column.setName(columnElt.attributeValue("name"));
					column.setType(columnElt.attributeValue("type"));
					column.setData(columnElt.elementTextTrim("data"));
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ds;
	}
	
	/**
	 * 转化成Mapping
	 * @param xml
	 * @return
	 */
	public static List<Mapping> toMappings(File file){
		List<Mapping> list = new ArrayList<Mapping>();
		
		Document doc = null;
		try {
			SAXReader sr = new SAXReader();//获取读取方式
			doc = sr.read(file); 
			
			Element rootElt = doc.getRootElement();
			Iterator mappingIter = rootElt.elementIterator("mapping");
			while (mappingIter.hasNext()) {
				Mapping mapping = new Mapping();
				list.add(mapping);
				
				Element mappingElt = (Element) mappingIter.next();
				mapping.setName(mappingElt.attributeValue("name"));
				mapping.setsTable(mappingElt.attributeValue("stable"));
				mapping.setpTable(mappingElt.attributeValue("ptable"));
				mapping.setcTable(mappingElt.attributeValue("ctable"));
				mapping.setType(mappingElt.attributeValue("type"));
				
				Element fieldsElt = mappingElt.element("fields");
				Iterator fieldItr = fieldsElt.elementIterator("field");
				while (fieldItr.hasNext()) {
					FieldMapping fm = new FieldMapping();
					mapping.getFieldMappings().add(fm);
					
					Element fieldElt = (Element) fieldItr.next();
					Element pFieldElt = (Element) fieldElt.element("pfield");
					Element cFieldElt = (Element) fieldElt.element("cfield");
					Field pField = new Field();
					Field cField = new Field();
					
					pField.setName(pFieldElt.attributeValue("name"));
					pField.setType(pFieldElt.attributeValue("type"));
					pField.setDbType(pFieldElt.attributeValue("dbType"));
					pField.setLength(pFieldElt.attributeValue("length"));
					pField.setpName(pFieldElt.attributeValue("pName")==null?"p_" + pField.getName() : pFieldElt.attributeValue("pName"));
					pField.setPrimaryKey(pFieldElt.attributeValue("primaryKey")==null?false:Boolean.parseBoolean(pFieldElt.attributeValue("primaryKey")));
					pField.setNullable(pFieldElt.attributeValue("nullable")==null?true:Boolean.parseBoolean(pFieldElt.attributeValue("nullable")));
					
					cField.setName(cFieldElt.attributeValue("name"));
					cField.setType(cFieldElt.attributeValue("type"));
					cField.setDbType(cFieldElt.attributeValue("dbType"));
					cField.setLength(cFieldElt.attributeValue("length"));
					cField.setPrimaryKey(cFieldElt.attributeValue("primaryKey")==null?false:Boolean.parseBoolean(cFieldElt.attributeValue("primaryKey")));
					cField.setNullable(cFieldElt.attributeValue("nullable")==null?true:Boolean.parseBoolean(cFieldElt.attributeValue("nullable")));
					
					fm.setpField(pField);
					fm.setcField(cField);
				}
			}
			
		}catch (DocumentException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据数据集和Mapping返回封装成Xml语句
	 * @param lst
	 * @param mapping
	 * @return
	 */
	public static String toXmlMessage(List<Map<String,Object>> rows,Mapping mapping){
		String keyXml = "", rowsXml = "", rowXml = "";
		
		for(Map<String,Object> row : rows){
			String columnXml = "";
			for(FieldMapping fm : mapping.getFieldMappings()){
				Field f = fm.getcField();
				Field p = fm.getpField();
				Object value = row.get(p.getpName());
				if(f.isPrimaryKey()){
					keyXml = "<key name='" + f.getName() + "' type='" + f.getType() + "'><data>" + value + "</data></key>\n";
				}
				columnXml += "<column name='" + f.getName() + "' type='" + f.getType() + "'><data>" + value + "</data></column>\n";
			}
			
			rowXml += "<row type='" + mapping.getType() + "'>\n" + columnXml + "</row>\n";
		}
		
		rowsXml += "<rows>\n" + rowXml + "</rows>\n";
		String xml = "<dataset table='" + mapping.getcTable() + "'>\n" + keyXml + rowsXml + "</dataset>\n";
		
		return xml;
	}
	
	public static void main(String[] args) {
		// 下面是需要解析的xml字符串例子
		String xmlString = "<dataset table='p_user'><key name='p_id' type='number'><data>1</data></key>" +
				"<rows><row type='add|update|delete'>" +
				"<column name='p_id' type='nu mber'><data>1</data></column>" +
				"<column name='p_name' type='string'><data>张三</data></column>" +
				"<column name='p_address' type='string'><data>北京市石景山远洋山水</data></column>" +
				"<column name='p_createtime' type='date'><data>2014-3-15 08:33:34</data></column></row></rows></dataset>";
		
		String xmlString1 = "<mappings><mapping stable='user' ptable='p_user' ctable='user' type='all|insert|update|delete|au|ad|ud'>" +
				"<fields>" +
				"<field>" +
				"<sfield name='id' type='number' dbType='varchar' length='25'></sfield ><cfield name='id' type='number'></cfield >" +
				"</field>" +
				"<field>" +
				"<sfield name='t_name' type='string' dbType='varchar' length='25'></sfield ><cfield name='name' type='string'>" +
				"</cfield ></field>" +
				"<field><sfield name='t_address' type='string' dbType='varchar' length='25'></sfield ><cfield name='address' type='string'></cfield >" +
				"</field>" +
				"<field>" +
				"<sfield name='t_createtime' type='string' dbType='date' length='25'></sfield ><cfield name='createtime' type='string'></cfield >" +
				"</field>" +
				"</fields>" +
				"</mapping>" +
				"</mappings>";
		
		DataSet ds = toDataSet(xmlString);
		System.out.println(ds);
//		List<Mapping> mappings = toMappings(xmlString1);
//		System.out.println(mappings);
	}
}
