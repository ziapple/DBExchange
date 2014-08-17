/**
 * 
 */
package com.appleframe.dbexchange.mq;

import javax.jms.Queue;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.appleframe.dbexchange.db.metadata.ds.Message;

/**
 *
 */
@Component
public class ResourceMessageProducer {
	
	private JmsTemplate template;

	private Queue destination;
	
	public void commit(Message msg){
		System.out.println("发送消息:" + msg.getXml());
		template.convertAndSend(this.destination, msg);
	}

	public void setDestination(Queue destination) {
		this.destination = destination;
	}

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}
	
}
