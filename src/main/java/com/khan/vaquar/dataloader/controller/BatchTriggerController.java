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

import com.khan.vaquar.dataloader.domain.response.Data;
import com.khan.vaquar.dataloader.domain.response.Error;
import com.khan.vaquar.dataloader.domain.response.Response;

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

	
	@GetMapping(value = "/startwithresponse", produces = "application/json")
	public Response invokeBatchWithResponse() {
		//
		String statue="Success";
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
		//
		Response response = new Response();
		

		try {
			jobLauncher.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException e) {
			e.printStackTrace();
			statue= "Fail";
		} catch (JobRestartException e) {
			e.printStackTrace();
			statue= "Fail";
		} catch (JobInstanceAlreadyCompleteException e) {
			e.printStackTrace();
			statue= "Fail";
		} catch (JobParametersInvalidException e) {
			e.printStackTrace();
			statue= "Fail";
		}catch(Exception e) {
			statue= "Fail";
		}
		return responseBuilder(statue);
	}
	/**
	 * 
	 * @param flag
	 * @return
	 */
	private Response responseBuilder(String flag) {
		Response response = new Response();

		Data data = new Data();
		data.setBatchId("1001");
		data.setBatchName("People-Batch-Process");
		data.setStatus(flag);
		response.setData(data);
		//
		Error error = new Error();
		error.setCode("");
		error.setMessage("");
		
		response.setError(error);
		
		return response;
	}
	
	
}
