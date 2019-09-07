package com.khan.vaquar.dataloader.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.stereotype.Component;

import nyla.solutions.core.util.Debugger;

/**
 * 
 * @author vaquar khan
 *
 */
@Component
public class Log4JSkipListener {

	static Logger log = LogManager.getLogger("badRecordLogger");

	@OnReadError
	public void errorOnRead(Exception exception) {

		log.error("errorOnRead:" + Debugger.stackTrace(exception));

	}

	@OnSkipInWrite
	public void errorOnWrite(Object o, Throwable error) {
		log.error("Error writing record = " + o);

		log.error("errorOnWrite:" + Debugger.stackTrace(error) + " OBJECT:" + o);
	}

}
