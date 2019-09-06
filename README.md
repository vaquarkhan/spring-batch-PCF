# spring-batch-PCF
Spring Batch Applications on PCF with h2 db and hal browser 


![Alt Text](https://www.tutorialspoint.com/spring_batch/images/spring-batch.jpg)

### SpringBatchArchitecture 

- https://terasoluna-batch.github.io/guideline/5.0.0.RELEASE/en/Ch02_SpringBatchArchitecture.html


### Pre-Requisite
JDK 1.8
Spring Boot knowledge
Maven
IDE (Eclipse, STS, etc.)
PCF instance

---------------------------------------------------------

### Lets start

### Url :

	- http://localhost:8080/h2-console
	- http://localhost:8080/trigger/start
	- http://localhost:8080/browser/index.html#/


1) Use Spring initiializer and generate project 

- https://start.spring.io/

---------------------------------------------------------


2) Verify POM file 


			<?xml version="1.0" encoding="UTF-8"?>
			<project xmlns="http://maven.apache.org/POM/4.0.0"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				<modelVersion>4.0.0</modelVersion>

				<groupId>org.springframework</groupId>
				<artifactId>vkhan-batch-processing</artifactId>
				<version>0.1.0</version>

				<parent>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-parent</artifactId>
					<version>2.1.6.RELEASE</version>
					<relativePath />
				</parent>

			 <developers>
					<developer>
						<name>Vaquar  Khan</name>
						<id>1</id>
						<email>vaquar.khan@gmail.com</email>
						<organization>Khan's Software Foundation</organization>
					</developer>
			 </developers>
			 
			 

				<properties>
					<java.version>1.8</java.version>
				</properties>

				<dependencies>
					<dependency>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-starter-batch</artifactId>
					</dependency>

					<dependency>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-starter-web</artifactId>
					</dependency>

					<dependency>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-starter-actuator</artifactId>
					</dependency>
					<!-- https://mvnrepository.com/artifact/org.springframework.data/spring-data-rest-hal-browser -->
					<dependency>
						<groupId>org.springframework.data</groupId>
						<artifactId>spring-data-rest-hal-browser</artifactId>
						<version>3.1.10.RELEASE</version>
					</dependency>


					<dependency>
						<groupId>com.h2database</groupId>
						<artifactId>h2</artifactId>
						<scope>runtime</scope>
					</dependency>

					<!-- https://mvnrepository.com/artifact/com.github.nyla-solutions/nyla.solutions.core -->
					<dependency>
						<groupId>com.github.nyla-solutions</groupId>
						<artifactId>nyla.solutions.core</artifactId>
						<version>1.1.8</version>
					</dependency>

					<!-- https://mvnrepository.com/artifact/commons-dbcp/commons-dbcp -->
					<dependency>
						<groupId>commons-dbcp</groupId>
						<artifactId>commons-dbcp</artifactId>
						<version>1.4</version>
					</dependency>

				</dependencies>


				<build>
					<plugins>
						<plugin>
							<groupId>org.springframework.boot</groupId>
							<artifactId>spring-boot-maven-plugin</artifactId>
						</plugin>
					</plugins>
				</build>

			</project>



---------------------------------------------------------


3) update application.properties 


		server.port=9090
		#
		spring.h2.console.enabled=true
		#
		spring.datasource.url=jdbc:h2:mem:testdb
		spring.datasource.driverClassName=org.h2.Driver
		spring.datasource.username=sa
		spring.datasource.password=
		spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

		keySpELExpression=matchKey
		##
		spring.batch.job.enabled=false
		spring.batch.initializer.enabled=false
		#h2db
		spring.h2.console.settings.web-allow-others=true
		
		
---------------------------------------------------------

		
4) Create schema-all.sql

			DROP TABLE people IF EXISTS;

			CREATE TABLE people  (
				person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
				first_name VARCHAR(20),
				last_name VARCHAR(20)
			);


---------------------------------------------------------


5) Create sample-data.csv

			v1	z1
			v2	z2
			v3	z3
			v4	z4
			v5	z5
			v6	z6

I have created 10001 records and every 100 records deleted 1st name in csv file to throw exception.
### Note : step 3,4,5 going to inside resources folder

---------------------------------------------------------


6) Create Springboot class

				package khan.vaquar.loader;

				import org.springframework.boot.SpringApplication;
				import org.springframework.boot.autoconfigure.SpringBootApplication;
				/**
				 * 
				 * @author vaquar khan
				 *
				 */
				@SpringBootApplication
				public class Application {

					public static void main(String[] args) throws Exception {
						SpringApplication.run(Application.class, args);
					}
				}


---------------------------------------------------------

7) Create Rest controller 

				package khan.vaquar.loader.controller;

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
				 *http://localhost:9090/trigger/start
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


---------------------------------------------------------

8) Ceeate Spring batch configuration 

					package khan.vaquar.loader.config;

					import java.nio.file.Paths;

					import javax.sql.DataSource;

					import org.springframework.batch.core.Job;
					import org.springframework.batch.core.Step;
					import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
					import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
					import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
					import org.springframework.batch.core.launch.support.RunIdIncrementer;
					import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
					import org.springframework.batch.item.database.JdbcBatchItemWriter;
					import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
					import org.springframework.batch.item.file.FlatFileItemReader;
					import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
					import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
					import org.springframework.beans.factory.annotation.Autowired;
					import org.springframework.context.annotation.Bean;
					import org.springframework.context.annotation.Configuration;
					import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
					import org.springframework.core.convert.converter.Converter;
					import org.springframework.core.env.Environment;
					import org.springframework.core.io.ClassPathResource;

					import khan.vaquar.loader.domain.Person;
					import khan.vaquar.loader.exception.LoaderFaultMgr;
					import khan.vaquar.loader.listener.ChunkCountListener;
					import khan.vaquar.loader.listener.JobCompletionNotificationListener;
					import khan.vaquar.loader.listener.LogStepExecutionListener;
					import khan.vaquar.loader.listener.StepExecutionListener;
					import khan.vaquar.loader.processor.PersonItemProcessor;
					/**
					 * 
					 * @author vaquar khan
					 *
					 */
					@Configuration
					@EnableBatchProcessing
					public class BatchConfiguration {

						@Autowired
						public JobBuilderFactory jobBuilderFactory;

						@Autowired
						public StepBuilderFactory stepBuilderFactory;
					 
						//
					   // @Autowired
						//RowMapper<Object> rowMapper;

						//@Resource
						//DataSource jobRepositoryDataSource;

						///@Resource
						//DataSource jdbcDataSource;

						@Autowired
						Environment env;

						@Autowired
						Converter<Object, Object> itemKeyMapper;
						

						
						
						//@Bean
						//public DataSource jdbcDataSource(Environment env)
						//		throws Exception {
						//	org.apache.commons.dbcp.BasicDataSource dataStore = new org.apache.commons.dbcp.BasicDataSource();
							//dataStore.setDriverClassName(env.getRequiredProperty("jdbcClassName"));
							//dataStore.setUrl(env.getRequiredProperty("jdbcUrl"));
							//dataStore.setUsername(env.getRequiredProperty("jdbcUsername"));
							//dataStore.setPassword(env.getRequiredProperty("jdbcPassword"));//use Bcrypt hash
						//	return dataStore;
					//	}
						
						@Bean
						public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
							return new PropertySourcesPlaceholderConfigurer();
						}
						

						
						// tag::readerwriterprocessor[]
						@Bean
						public FlatFileItemReader<Person> reader() {
							return new FlatFileItemReaderBuilder<Person>()
								.name("personItemReader")
								.resource(new ClassPathResource("sample-data.csv"))
								.delimited()
								.names(new String[]{"firstName", "lastName"})
								.fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
									setTargetType(Person.class);
								}})
								.build();
						}

						@Bean
						public PersonItemProcessor processor() {
							return new PersonItemProcessor();
						}

						@Bean
						public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
							return new JdbcBatchItemWriterBuilder<Person>()
								.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
								.sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
								.dataSource(dataSource)
								.build();
						}
						// end::readerwriterprocessor[]

						// tag::jobstep[]
						@Bean
						public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
							return jobBuilderFactory.get("importUserJob")
								.incrementer(new RunIdIncrementer())
								.listener(listener)
								.flow(step1)
								.end()
								.build();
						}

							
						@Bean
						public Step step1(JdbcBatchItemWriter<Person> writer) {
							return stepBuilderFactory.get("step1")
								.<Person, Person> chunk(10)
								.reader(reader())
								.processor(processor())
								.writer(writer)
								.faultTolerant()
								.listener(new ChunkCountListener())
								//.retryLimit(3)
								.skip(Exception.class)
								.skipLimit(500)
							   // .noSkip(Exception.class)
							   // .throttleLimit(10)
								.listener(new StepExecutionListener())
								.listener(new LogStepExecutionListener())
								.build();
							
							
						}
						// end::jobstep[]
						
						
						   
					/**
					 * Loging
					 * @param env
					 * @return
					 */
					@Bean
					public LoaderFaultMgr loaderFaultMgr(Environment env) {
						//return new LoaderFaultMgr(Paths.get(env.getRequiredProperty("exception.write.file")).toFile());
						return new LoaderFaultMgr(Paths.get("C:\\tmp\\batchlog\\test.log").toFile());
						
						
					}

					}




---------------------------------------------------------


9)
