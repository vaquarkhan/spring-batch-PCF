package com.khan.vaquar.dataloader.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author vaquar khan
 * 
 *         http://localhost:9090/trigger/start
 */
@RestController
@RequestMapping(value = "/trigger")

public class BatchTriggerController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;

	@GetMapping(value = "/start") // , consumes = "application/json", produces = "application/json"
	public String invokeBatch() {
		//
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
				.toJobParameters();

		try {
			jobLauncher.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException e) {
			e.printStackTrace();
			return "Fail";
		} catch (JobRestartException e) {
			e.printStackTrace();
			return "Fail";
		} catch (JobInstanceAlreadyCompleteException e) {
			e.printStackTrace();
			return "Fail";
		} catch (JobParametersInvalidException e) {
			e.printStackTrace();
			return "Fail";
		}
		return "Success";
	}

}
