package com.videsimonk.config;

import com.videsimonk.processsor.FirstStepProcessor;
import com.videsimonk.reader.FirstStepReader;
import com.videsimonk.writer.FirstStepWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Component
public class JobConfig {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    private FirstStepReader reader;

    @Autowired
    private FirstStepProcessor processor;

    @Autowired
    private FirstStepWriter writer;

    @Bean
    public Job firstJob(){
        return new JobBuilder("First-Job1",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .next(SecondStep())
                .build();
    }

    @StepScope
    public Step firstStep(){
       return new StepBuilder("First-Step",jobRepository).tasklet(firstTasklet(),transactionManager).build();
    }

    @StepScope
    public Step SecondStep(){
        return new StepBuilder("Chunk-oriented Step",jobRepository)
                .<Integer,Long>chunk(3,transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

    }

    private Tasklet firstTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("First Tasklet is finished");
                return RepeatStatus.FINISHED;
            }
        };
    }
}
