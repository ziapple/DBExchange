/**
 * 
 */
package com.appleframe.dbexchange.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appleframe.dbexchange.db.metadata.ds.Message;
import com.appleframe.dbexchange.mq.ResourceMessageProducer;


/**
 * 基于线程池的数据库扫描任务
 */
@Component
public class DBThreadPoolTask {
	@Autowired
    private ResourceMessageProducer rmp;
    
    //线程池任务
	public void execute(String xml){
		System.out.println("扫描到一条记录...");
        
		//发送消息
		Message msg = new Message("xml", xml);
		rmp.commit(msg);
	}
}       