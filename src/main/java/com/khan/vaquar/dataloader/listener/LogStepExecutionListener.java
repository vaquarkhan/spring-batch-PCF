package com.khan.vaquar.dataloader.listener;

import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * 
 * @author vaquar khan
 *
 */
public class LogStepExecutionListener implements StepExecutionListener {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(LogStepExecutionListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {
		log.debug("------------------------------------------------------------------------------------");
		log.debug("START BEFORE STEP");

		if (stepExecution == null)
			return;
		log.debug("StepExecutionListener - beforeStep:getStartTime=" + stepExecution.getStartTime());
		log.debug("StepExecutionListener - beforeStep:getStartTime=" + stepExecution.getEndTime());
		log.debug("StepExecutionListener - beforeStep:getExitStatus=" + stepExecution.getExitStatus());
		log.debug("StepExecutionListener - beforeStep:getFailureExceptions=" + stepExecution.getFailureExceptions());
		log.info("StepExecutionListener - beforeStep:getSummary=" + stepExecution.getSummary());

		//

		log.debug("StepExecutionListener - afterStep:getProcessSkipCount=" + stepExecution.getProcessSkipCount());
		log.debug("StepExecutionListener - afterStep:getRollbackCount=" + stepExecution.getRollbackCount());
		log.debug("StepExecutionListener - afterStep:getWriteCount=" + stepExecution.getWriteCount());
		log.debug("StepExecutionListener - afterStep:getWriteSkipCount=" + stepExecution.getWriteSkipCount());
		log.info("StepExecutionListener - afterStep:getCommitCount=" + stepExecution.getCommitCount());
		log.info("StepExecutionListener - afterStep:getReadCount=" + stepExecution.getReadCount());
		log.debug("StepExecutionListener - afterStep:getReadSkipCount=" + stepExecution.getReadSkipCount());
		log.debug("StepExecutionListener - afterStep:getLastUpdated=" + stepExecution.getLastUpdated());
		log.info("StepExecutionListener - afterStep:getExitStatus=" + stepExecution.getExitStatus());
		log.info("StepExecutionListener - afterStep:getFailureExceptions=" + stepExecution.getFailureExceptions());
		log.info("StepExecutionListener - afterStep:getSummary=" + stepExecution.getSummary());

		log.info("------------------------------------------------------------------------------------");

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// System.out.println("StepExecutionListener - afterStep");
		log.info("------------------------------------------------------------------------------------");
		log.debug("END AFTER STEP");

		if (stepExecution == null)
			return null;

		log.debug("StepExecutionListener - afterStep:getFilterCount=" + stepExecution.getFilterCount());
		log.debug("StepExecutionListener - afterStep:getProcessSkipCount=" + stepExecution.getProcessSkipCount());
		log.debug("StepExecutionListener - afterStep:getRollbackCount=" + stepExecution.getRollbackCount());
		log.debug("StepExecutionListener - afterStep:getWriteCount=" + stepExecution.getWriteCount());
		log.debug("StepExecutionListener - afterStep:getWriteSkipCount=" + stepExecution.getWriteSkipCount());
		log.debug("StepExecutionListener - afterStep:getStepName=" + stepExecution.getStepName());
		log.debug("StepExecutionListener - afterStep:getStartTime=" + stepExecution.getStartTime());
		//
		log.debug("StepExecutionListener - afterStep:getStartTime=" + stepExecution.getEndTime());
		log.info("StepExecutionListener - afterStep:getCommitCount=" + stepExecution.getCommitCount());
		log.info("StepExecutionListener - afterStep:getReadCount=" + stepExecution.getReadCount());
		log.debug("StepExecutionListener - afterStep:getReadSkipCount=" + stepExecution.getReadSkipCount());
		log.debug("StepExecutionListener - afterStep:getLastUpdated=" + stepExecution.getLastUpdated());
		log.info("StepExecutionListener - afterStep:getExitStatus=" + stepExecution.getExitStatus());
		log.info("StepExecutionListener - afterStep:getFailureExceptions=" + stepExecution.getFailureExceptions());
		log.info("StepExecutionListener - afterStep:getSummary=" + stepExecution.getSummary());
		log.info("------------------------------------------------------------------------------------");

		return null;
	}

}
