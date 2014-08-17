/**
 * 
 */
package com.appleframe.dbexchange.mq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * 消息转换着
 */
public class ResourceMessageConverter implements MessageConverter {
	//读取消息
	public Object fromMessage(Message message) throws JMSException,
			MessageConversionException {
		if (message instanceof ObjectMessage) {  
            HashMap<String, byte[]> map = (HashMap<String, byte[]>) ((ObjectMessage) message).getObjectProperty("Map");  
            try {  
                // POJO must implements Seralizable  
                ByteArrayInputStream bis = new ByteArrayInputStream(map.get("POJO"));  
                ObjectInputStream ois = new ObjectInputStream(bis);  
                Object returnObject = ois.readObject();  
                return returnObject;  
            } catch (IOException e) {  
            	e.printStackTrace();
            } catch (ClassNotFoundException e) {  
            	e.printStackTrace();
            }  
  
            return null;  
        } else {  
            throw new JMSException("Msg:[" + message + "] is not Map");  
        }  
		
	}

	//发送消息
	public Message toMessage(Object object, Session session) throws JMSException,
			MessageConversionException {
		// check Type  
        ActiveMQObjectMessage objMsg = (ActiveMQObjectMessage) session.createObjectMessage();  
        HashMap<String, byte[]> map = new HashMap<String, byte[]>();  
        try {  
            // POJO must implements Seralizable  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            ObjectOutputStream oos = new ObjectOutputStream(bos);  
            oos.writeObject(object);  
            map.put("POJO", bos.toByteArray());  
            objMsg.setObjectProperty("Map", map);  
  
        } catch (IOException e) {  
           e.printStackTrace();
        }  
        return objMsg;  
	}
}
