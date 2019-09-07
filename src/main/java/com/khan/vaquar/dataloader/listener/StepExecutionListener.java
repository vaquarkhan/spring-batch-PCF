package com.khan.vaquar.dataloader.listener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.annotation.OnWriteError;

import com.khan.vaquar.dataloader.domain.Person;

/**
 * 
 * @author vaquar khan
 *
 */
public class StepExecutionListener {
	private static final Logger LOG = LogManager.getLogger(StepExecutionListener.class);

	@OnSkipInRead
	public void onSkipInRead(Throwable t) {
		LOG.error("On Skip in Read Error : " + t.getMessage());
	}

	@OnSkipInWrite
	public void onSkipInWrite(Person item, Throwable t) {
		LOG.error("Skipped in write due to : " + t.getMessage());
	}

	@OnSkipInProcess
	public void onSkipInProcess(Person item, Throwable t) {
		LOG.error("Skipped in process due to: " + t.getMessage() + "Person =" + item.toString());
		LOG.error(pringtStackTrace(t));

	}

	@OnWriteError
	public void onWriteError(Exception exception, List<? extends Person> items) {
		LOG.error("Error on write on " + items + " : " + exception.getMessage());
	}

	private String pringtStackTrace(Throwable exception) {
		if (null == exception) {
			return "";
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		String sStackTrace = sw.toString();
		return sStackTrace;
	}

}
