package com.khan.vaquar.dataloader.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

/**
 * 
 * @author vaquar khan
 *
 */
@Component
public class JobCompletionExitListener extends JobExecutionListenerSupport {
	static Logger log = LogManager.getLogger(JobCompletionExitListener.class);

	@Override
	public void afterJob(JobExecution jobExecution) {

		if (jobExecution == null)
			return;

		ExitStatus es = jobExecution.getExitStatus();

		if (es != null && "FAILED".equals(es.getExitCode())) {
			log.error("Failed job executiuion:" + jobExecution + " exitStatus:" + es);
			System.exit(-1);
		}

		System.exit(0);
	}

}
