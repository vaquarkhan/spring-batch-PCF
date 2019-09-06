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

1) Use Spring initiializer and generate project 

- https://start.spring.io/

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



