package com.videsimonk.batchproj;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.videsimonk.config","com.videsimonk.reader","com.videsimonk.processsor","com.videsimonk.writer"})
public class BatchprojApplication{

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job firstJob;

	public static void main(String[] args) {
		SpringApplication.run(BatchprojApplication.class, args);
	}


}
