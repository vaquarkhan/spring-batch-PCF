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

		keySpELExpression=primaryKey
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


9) Now create domain object

			package khan.vaquar.loader.domain;
			/**
			 * 
			 * @author vaquar khan
			 *
			 */
			public class Person {

				private String lastName;
				private String firstName;

				public Person() {
				}

				public Person(String firstName, String lastName) {
					this.firstName = firstName;
					this.lastName = lastName;
				}

				public void setFirstName(String firstName) {
					this.firstName = firstName;
				}

				public String getFirstName() {
					return firstName;
				}

				public String getLastName() {
					return lastName;
				}

				public void setLastName(String lastName) {
					this.lastName = lastName;
				}

				@Override
				public String toString() {
					return "firstName: " + firstName + ", lastName: " + lastName;
				}

			}





---------------------------------------------------------


10) Create processor

				package khan.vaquar.loader.processor;

				import org.slf4j.Logger;
				import org.slf4j.LoggerFactory;
				import org.springframework.batch.item.ItemProcessor;

				import khan.vaquar.loader.domain.Person;
				/**
				 * 
				 * @author vaquar khan
				 *
				 */
				public class PersonItemProcessor implements ItemProcessor<Person, Person> {

					private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);
					
					@Override
					public Person process(final Person person) throws Exception {
						final String firstName = person.getFirstName().toUpperCase();
						final String lastName = person.getLastName().toUpperCase();
						//try {
							if (firstName.isEmpty()) {
								throw new Exception("Tets exception");
							}
							
						/*	
						} catch (Exception e) {
							String stackTrace = Debugger.stackTrace(e);
							// this.logger.error(stackTrace + " ITEMS:" + items);
							log.error(stackTrace + " ITEMS:" + firstName);
							//
							throw new LoaderException("null pointer", person.getFirstName(), person, e);

						}
				*/
					

					final Person transformedPerson = new Person(firstName, lastName);

					// log.info("Converting (" + person + ") into (" + transformedPerson + ")");

					return transformedPerson;
				}

				}


---------------------------------------------------------

11) Create SpELConverter

package khan.vaquar.loader.transformation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

/**
 * 
 * @author vaquar khan
 *
 */
@Component
public class SpELConverter implements Converter<Object, Object>{

	@Value("${keySpELExpression}")
	private String expression;
	
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Override
	public Object convert(Object source) {
		
		ExpressionParser parser = new SpelExpressionParser();

		Expression exp = parser.parseExpression(expression);
		Object output = exp.getValue(source);
		return output;
	}

}


--------------------------------------------------

###  We are looking all possible useful listner 

12)  ChunkCountListener 


				package khan.vaquar.loader.listener;

				import java.text.MessageFormat;

				import org.apache.logging.log4j.LogManager;
				import org.apache.logging.log4j.Logger;
				import org.springframework.batch.core.ChunkListener;
				import org.springframework.batch.core.scope.context.ChunkContext;
				/**
				 * 
				 * @author vaquar khan
				 *
				 */
				/**
				 * Log the count of items processed at a specified interval.
				 * 
				 *
				 */
				public class ChunkCountListener implements ChunkListener {

					private static final Logger log = LogManager.getLogger(ChunkCountListener.class);

					private MessageFormat fmt = new MessageFormat("{0} items processed");

					private int loggingInterval = 1000;

					@Override
					public void beforeChunk(ChunkContext context) {
						// Nothing to do here
					}

					@Override
					public void afterChunk(ChunkContext context) {

						int count = context.getStepContext().getStepExecution().getReadCount();

						// If the number of records processed so far is a multiple of the logging
						// interval then output a log message.
						if (count > 0 && count % loggingInterval == 0) {
							log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");
							log.info(fmt.format(new Object[] { new Integer(count) }));
							log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");
						}
					}

					@Override
					public void afterChunkError(ChunkContext context) {
						// Nothing to do here
					}

					public void setItemName(String itemName) {
						this.fmt = new MessageFormat("{0} " + itemName + " processed");
					}

					public void setLoggingInterval(int loggingInterval) {
						this.loggingInterval = loggingInterval;
					}
				}



--------------------------------------------------


13)  JobCompletionExitListener

			package khan.vaquar.loader.listener;

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
			public class JobCompletionExitListener extends JobExecutionListenerSupport
			{
				static Logger log = LogManager.getLogger(JobCompletionExitListener.class);
				
				@Override
				public void afterJob(JobExecution jobExecution) {

					if(jobExecution == null)
						return;
					
					ExitStatus es = jobExecution.getExitStatus();
					
					if(es != null && "FAILED".equals(es.getExitCode()))
					{
						log.error("Failed job executiuion:"+jobExecution+" exitStatus:"+es);
						System.exit(-1);
					}
					
					
					System.exit(0);
				}

			}


-------------------------------------------

14 ) JobCompletionNotificationListener


				package khan.vaquar.loader.listener;

				import org.slf4j.Logger;
				import org.slf4j.LoggerFactory;
				import org.springframework.batch.core.BatchStatus;
				import org.springframework.batch.core.JobExecution;
				import org.springframework.batch.core.listener.JobExecutionListenerSupport;
				import org.springframework.beans.factory.annotation.Autowired;
				import org.springframework.jdbc.core.JdbcTemplate;
				import org.springframework.stereotype.Component;

				import khan.vaquar.loader.domain.Person;
				/**
				 * 
				 * @author vaquar khan
				 *
				 */
				@Component
				public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

					private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

					private final JdbcTemplate jdbcTemplate;

					@Autowired
					public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
						this.jdbcTemplate = jdbcTemplate;
					}

					@Override
					public void afterJob(JobExecution jobExecution) {
						/*
						if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
							log.info("!!! JOB FINISHED! Time to verify the results");

							jdbcTemplate.query("SELECT first_name, last_name FROM people",
								(rs, row) -> new Person(
									rs.getString(1),
									rs.getString(2))
							).forEach(person -> log.info("Found <" + person + "> in the database."));
						}
						*/
					}
					
				}


-------------------------------------------

15) Log4JSkipListener

				package khan.vaquar.loader.listener;

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
					public void errorOnRead( Exception exception ) {
						
						log.error("errorOnRead:"+Debugger.stackTrace(exception));

					}

					@OnSkipInWrite
					public void errorOnWrite( Object o, Throwable error ) {
						log.error("Error writing record = " + o);
						
						log.error("errorOnWrite:"+ Debugger.stackTrace(error)+" OBJECT:"+o);
					}

				}
 

-------------------------------------------

16)  LogStepExecutionListener

			package khan.vaquar.loader.listener;

			import org.slf4j.LoggerFactory;
			import org.springframework.batch.core.ExitStatus;
			import org.springframework.batch.core.StepExecution;
			import org.springframework.batch.core.StepExecutionListener;
			/**
			 * 
			 * @author vaquar khan
			 *
			 */
			public class LogStepExecutionListener  implements StepExecutionListener {
				
				private static final org.slf4j.Logger log = LoggerFactory.getLogger(LogStepExecutionListener.class);
				
				
				@Override
				public void beforeStep(StepExecution stepExecution) {
					log.debug("------------------------------------------------------------------------------------");
					log.debug("START BEFORE STEP");
					
					if(stepExecution == null)
						return;
					log.debug("StepExecutionListener - beforeStep:getStartTime=" +  stepExecution.getStartTime());
					log.debug("StepExecutionListener - beforeStep:getStartTime=" +  stepExecution.getEndTime());
					log.debug("StepExecutionListener - beforeStep:getExitStatus=" +  stepExecution.getExitStatus());
					log.debug("StepExecutionListener - beforeStep:getFailureExceptions=" +  stepExecution.getFailureExceptions());
					log.info("StepExecutionListener - beforeStep:getSummary=" +  stepExecution.getSummary());
					
					//
					
					log.debug("StepExecutionListener - afterStep:getProcessSkipCount=" +  stepExecution.getProcessSkipCount());
					log.debug("StepExecutionListener - afterStep:getRollbackCount=" +  stepExecution.getRollbackCount());
					log.debug("StepExecutionListener - afterStep:getWriteCount=" +  stepExecution.getWriteCount());
					log.debug("StepExecutionListener - afterStep:getWriteSkipCount=" +  stepExecution.getWriteSkipCount());
					log.info("StepExecutionListener - afterStep:getCommitCount=" +  stepExecution.getCommitCount());
					log.info("StepExecutionListener - afterStep:getReadCount=" +  stepExecution.getReadCount());
					log.debug("StepExecutionListener - afterStep:getReadSkipCount=" +  stepExecution.getReadSkipCount());
					log.debug("StepExecutionListener - afterStep:getLastUpdated=" +  stepExecution.getLastUpdated());
					log.info("StepExecutionListener - afterStep:getExitStatus=" +  stepExecution.getExitStatus());
					log.info("StepExecutionListener - afterStep:getFailureExceptions=" +  stepExecution.getFailureExceptions());
					log.info("StepExecutionListener - afterStep:getSummary=" +  stepExecution.getSummary());
					
					
					log.info("------------------------------------------------------------------------------------");
					
				}

				@Override
				public ExitStatus afterStep(StepExecution stepExecution) {
					//System.out.println("StepExecutionListener - afterStep");
					log.info("------------------------------------------------------------------------------------");
					log.debug("END AFTER STEP");
					
					if(stepExecution == null)
						return null;
					
					log.debug("StepExecutionListener - afterStep:getFilterCount=" +  stepExecution.getFilterCount());
					log.debug("StepExecutionListener - afterStep:getProcessSkipCount=" +  stepExecution.getProcessSkipCount());
					log.debug("StepExecutionListener - afterStep:getRollbackCount=" +  stepExecution.getRollbackCount());
					log.debug("StepExecutionListener - afterStep:getWriteCount=" +  stepExecution.getWriteCount());
					log.debug("StepExecutionListener - afterStep:getWriteSkipCount=" +  stepExecution.getWriteSkipCount());
					log.debug("StepExecutionListener - afterStep:getStepName=" +  stepExecution.getStepName());
					log.debug("StepExecutionListener - afterStep:getStartTime=" +  stepExecution.getStartTime());
					//
					log.debug("StepExecutionListener - afterStep:getStartTime=" +  stepExecution.getEndTime());
					log.info("StepExecutionListener - afterStep:getCommitCount=" +  stepExecution.getCommitCount());
					log.info("StepExecutionListener - afterStep:getReadCount=" +  stepExecution.getReadCount());
					log.debug("StepExecutionListener - afterStep:getReadSkipCount=" +  stepExecution.getReadSkipCount());
					log.debug("StepExecutionListener - afterStep:getLastUpdated=" +  stepExecution.getLastUpdated());
					log.info("StepExecutionListener - afterStep:getExitStatus=" +  stepExecution.getExitStatus());
					log.info("StepExecutionListener - afterStep:getFailureExceptions=" +  stepExecution.getFailureExceptions());
					log.info("StepExecutionListener - afterStep:getSummary=" +  stepExecution.getSummary());
					log.info("------------------------------------------------------------------------------------");
					
					return null;
				}

			}


-------------------------------------------

17)  StepExecutionListener


					package khan.vaquar.loader.listener;

					import java.io.PrintWriter;
					import java.io.StringWriter;
					import java.util.List;

					import org.apache.logging.log4j.LogManager;
					import org.apache.logging.log4j.Logger;
					import org.springframework.batch.core.annotation.OnSkipInProcess;
					import org.springframework.batch.core.annotation.OnSkipInRead;
					import org.springframework.batch.core.annotation.OnSkipInWrite;
					import org.springframework.batch.core.annotation.OnWriteError;

					import khan.vaquar.loader.domain.Person;
					/**
					 * 
					 * @author vaquar khan
					 *
					 */
					public class StepExecutionListener {
						private static final  Logger LOG = LogManager.getLogger(StepExecutionListener.class);
						
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
							  LOG.error("Skipped in process due to: " + t.getMessage()+"Person ="+item.toString());
							  LOG.error(pringtStackTrace(t));
							  
							  
						  }

						  @OnWriteError
						  public void onWriteError(Exception exception, List<? extends Person> items) {
							  LOG.error("Error on write on " + items + " : " + exception.getMessage());
						  }

						  private String pringtStackTrace(Throwable exception) {
							if(null ==exception) {
								return "";
							}
							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
							exception.printStackTrace(pw);
							String sStackTrace = sw.toString(); 
							return sStackTrace;
						}
						
					}




--------------------------------------------------------------

18) Create manifest.yaml


			applications:
			- name: springbatchpoc
			  memory: 2G
			  disk_quota: 1G
			  instances: 1
			  health-check-type: none
			  path: target/vkhan-batch-processing-0.1.0.jar
			  env: 
				SPRING_PROFILES_ACTIVE : "dev"
				
			routes:
			- route: springbatchpoc.app.vaquar.khan.com

---------------------------------------------------------

### Now deploy to PCF

So far we have created a Spring Batch job. Now let's deploy it to PCF. You just need to package the code and cf push to PCF with a manifest file.


          cf login -a api.sys.app.vaquar.khan.com -u vaquarkhan
          cf push springbatchpoc

![Alt Text](https://1.bp.blogspot.com/-tDSpjNRCJM0/XJsVvkeDBtI/AAAAAAAAPhI/LqjTJGlTk-UPJ0zMos1KAVWRO0XuFmGfQCLcBGAs/s400/Crashed_app.PNG)


--------------------------------------------------------------

if want to run job via PCF scheduler 

### Schedule Batch Job With PCF Scheduler

![Alt Text](https://3.bp.blogspot.com/-DswQ4fRQ5js/XJse3AIrKrI/AAAAAAAAPhU/nkXEn9__YLkX--tRg5tJ3II7dTG4itr3QCLcBGAs/s320/PCFScheuler.PNG)

Go to the application in Apps Manager -> Tasks and click on Enable Scheduling to bind the application with the PCF Scheduler. Now you can create a job as shown in the below picture.

- http://www.rajeshbhojwani.co.in/2018/11/scheduling-jobs-using-pcf-scheduler.html
