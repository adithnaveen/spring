------------------------------------------------------------------------------------------------------------
Spring Batch 
------------------------------------------------------------------------------------------------------------
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.spring.batch</groupId>
  <artifactId>SpringBatchWorks</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  
  
    <properties>
        <springframework.version>4.0.6.RELEASE</springframework.version>
        <springbatch.version>3.0.1.RELEASE</springbatch.version>
        <mysql.connector.version>5.1.31</mysql.connector.version>
        <joda-time.version>2.3</joda-time.version>
    </properties>
 
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>${springframework.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-oxm</artifactId>
            <version>${springframework.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>${springframework.version}</version>
        </dependency>
 
        <dependency>
            <groupId>org.springframework.batch</groupId>
            <artifactId>spring-batch-core</artifactId>
            <version>${springbatch.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.batch</groupId>
            <artifactId>spring-batch-infrastructure</artifactId>
            <version>${springbatch.version}</version>
        </dependency>
         
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.connector.version}</version>
        </dependency>
        
         
        <dependency>
            <groupId>joda-time</groupId>
            <artifactId>joda-time</artifactId>
            <version>${joda-time.version}</version>
        </dependency>
    </dependencies>
  <build>
    <sourceDirectory>src</sourceDirectory>
    <plugins>
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.3</version>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
------------------------------------------------------------------------------------------------------------

package com.naveen.springbatch.model;
 
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.LocalDate;
 
@XmlRootElement(name = "ExamResult")
public class ExamResult {
 
    private String studentName;
 
    private LocalDate dob;
 
    private double percentage;
 
    @XmlElement(name = "studentName")
    public String getStudentName() {
        return studentName;
    }
 
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
 
    @XmlElement(name = "dob")
    @XmlJavaTypeAdapter(type = LocalDate.class, value = com.naveen.springbatch.LocalDateAdapter.class)
    public LocalDate getDob() {
        return dob;
    }
 
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
 
    @XmlElement(name = "percentage")
    public double getPercentage() {
        return percentage;
    }
 
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
 
    @Override
    public String toString() {
        return "ExamResult [studentName=" + studentName + ", dob=" + dob
                + ", percentage=" + percentage + "]";
    }
 
}
------------------------------------------------------------------------------------------------------------
package com.naveen.springbatch;
 
import java.sql.PreparedStatement;
import java.sql.SQLException;
 
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import com.naveen.springbatch.model.ExamResult;
 
/*
 * 
 * Note that since we are using Joda-time LocalDate which can not be mapped to MySQL date directly, 
 * we need to provide custom-logic to handle this conversion.Below is the class for this conversion.
 */
public class ExamResultItemPreparedStatementSetter implements ItemPreparedStatementSetter<ExamResult> {
 
    public void setValues(ExamResult result, PreparedStatement ps) throws SQLException {
        ps.setString(1, result.getStudentName());
        ps.setDate(2, new java.sql.Date(result.getDob().toDate().getTime()));
        ps.setDouble(3, result.getPercentage());
    }
 
}
------------------------------------------------------------------------------------------------------------
package com.naveen.springbatch;

import org.springframework.batch.item.ItemProcessor;

import com.naveen.springbatch.model.ExamResult;

/*
 * 
 * ItemProcessor is Optional, and called after item read but before item write. It gives us the opportunity to perform a business logic on each item.In our case, for example, we will filter out all the items whose percentage is less than 75. So final result will only have records with percentage >= 75.
 */
public class ExamResultItemProcessor implements ItemProcessor<ExamResult, ExamResult> {

	@Override
	public ExamResult process(ExamResult result) throws Exception {
		System.out.println("Processing result :" + result);

		/*
		 * Only return results which are equal or more than 75%
		 *
		 */
		if (result.getPercentage() < 75) {
			return null;
		}

		return result;
	}

}

------------------------------------------------------------------------------------------------------------
package com.naveen.springbatch;
 
import java.util.List;
 
import org.joda.time.DateTime;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
 

/**
 * Job listener is Optional and provide the opportunity to execute some business logic before job start and 
 * after job completed.For example setting up environment can be done before job and cleanup can be 
 * done after job completed.

 */
public class ExamResultJobListener implements JobExecutionListener{
 
    private DateTime startTime, stopTime;
 
    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = new DateTime();
        System.out.println("ExamResult Job starts at :"+startTime);
    }
 
    @Override
    public void afterJob(JobExecution jobExecution) {
        stopTime = new DateTime();
        System.out.println("ExamResult Job stops at :"+stopTime);
        System.out.println("Total time take in millis :"+getTimeInMillis(startTime , stopTime));
 
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            System.out.println("ExamResult job completed successfully");
            //Here you can perform some other business logic like cleanup
        }else if(jobExecution.getStatus() == BatchStatus.FAILED){
            System.out.println("ExamResult job failed with following exceptions ");
            List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
            for(Throwable th : exceptionList){
                System.err.println("exception :" +th.getLocalizedMessage());
            }
        }
    }
 
    private long getTimeInMillis(DateTime start, DateTime stop){
        return stop.getMillis() - start.getMillis();
    }
 
}
------------------------------------------------------------------------------------------------------------
package com.naveen.springbatch;
 
import javax.xml.bind.annotation.adapters.XmlAdapter;
 
import org.joda.time.LocalDate;
 
public class LocalDateAdapter extends XmlAdapter<String, LocalDate>{
 
    public LocalDate unmarshal(String v) throws Exception {
        return new LocalDate(v);
    }
 
    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }
 
}
------------------------------------------------------------------------------------------------------------
package com.naveen.springbatch.client;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 
public class Main {
    @SuppressWarnings("resource")
    public static void main(String areg[]){
 
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-batch-context.xml");
 
        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        Job job = (Job) context.getBean("examResultJob");
 
        try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Job Exit Status : "+ execution.getStatus());
 
        } catch (JobExecutionException e) {
            System.out.println("Job ExamResult failed");
            e.printStackTrace();
        }
    }
 
}
------------------------------------------------------------------------------------------------------------
context-datasource.xml
------------------------------------------------------------------------------------------------------------
<?xml version="1.0" encoding="UTF-8"?>


<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
	   http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="dataSource"
		class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="com.mysql.jdbc.Driver" />
		<property name="url" value="jdbc:mysql://localhost/navdb" />
		<property name="username" value="root" />
		<property name="password" value="kanchan" />
	</bean>

      
</beans>

	   					   

------------------------------------------------------------------------------------------------------------
spring-batch-context.xml
------------------------------------------------------------------------------------------------------------
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:int="http://www.springframework.org/schema/integration"
  xmlns:batch="http://www.springframework.org/schema/batch"
  xmlns:batch-int="http://www.springframework.org/schema/batch-integration"
  xsi:schemaLocation="
    http://www.springframework.org/schema/batch-integration
    http://www.springframework.org/schema/batch-integration/spring-batch-integration.xsd
    http://www.springframework.org/schema/batch
    http://www.springframework.org/schema/batch/spring-batch.xsd
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/integration
    http://www.springframework.org/schema/integration/spring-integration.xsd">

    
    <import resource="classpath:context-datasource.xml"/>
 
    <!-- JobRepository and JobLauncher are configuration/setup classes -->
    <bean id="jobRepository" class="org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean" />
 
    <bean id="jobLauncher"   class="org.springframework.batch.core.launch.support.SimpleJobLauncher">
        <property name="jobRepository" ref="jobRepository" />
    </bean>
 
    <!-- ItemReader which reads data from XML file -->
    <bean id="xmlItemReader" class="org.springframework.batch.item.xml.StaxEventItemReader">
 
        <property name="resource" value="classpath:examResult.xml" />
 
        <property name="fragmentRootElementName" value="ExamResult" />
 
        <property name="unmarshaller">
            <bean class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
                <property name="classesToBeBound">
                    <list>
                        <value>com.naveen.springbatch.model.ExamResult</value>
                    </list>
                </property>
            </bean>
        </property>
    </bean>
 
    <!-- ItemWriter which writes data to database -->
    <bean id="databaseItemWriter" class="org.springframework.batch.item.database.JdbcBatchItemWriter">
        <property name="dataSource" ref="dataSource" />
        <property name="sql">
            <value>
                <![CDATA[        
                    insert into EXAM_RESULT(STUDENT_NAME, DOB, PERCENTAGE) 
                    values (?, ?, ?)
                ]]>
            </value>
        </property>
 
        <!-- We need a custom setter to handle the conversion between Jodatime LocalDate and MySQL DATE -->
        <property name="ItemPreparedStatementSetter">
            <bean class="com.naveen.springbatch.ExamResultItemPreparedStatementSetter" />
        </property>
  </bean>
 
    <!-- Optional ItemProcessor to perform business logic/filtering on the input records -->
    <bean id="itemProcessor" class="com.naveen.springbatch.ExamResultItemProcessor" />
 
    <!-- Optional JobExecutionListener to perform business logic before and after the job -->
    <bean id="jobListener" class="com.naveen.springbatch.ExamResultJobListener" />
 
    <!-- Step will need a transaction manager -->
    <bean id="transactionManager" class="org.springframework.batch.support.transaction.ResourcelessTransactionManager" />
 
    <!-- Actual Job -->
    <batch:job id="examResultJob">
        <batch:step id="step1">
            <batch:tasklet transaction-manager="transactionManager">
                <batch:chunk reader="xmlItemReader" writer="databaseItemWriter"
                    processor="itemProcessor" commit-interval="10" />
            </batch:tasklet>
        </batch:step>
        <batch:listeners>
            <batch:listener ref="jobListener" />
        </batch:listeners>
    </batch:job>
 
</beans>          
------------------------------------------------------------------------------------------------------------
examResult.xml
------------------------------------------------------------------------------------------------------------
<?xml version="1.0" encoding="UTF-8"?>

<UniversityExamResultList>
    <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
       <ExamResult>
        <dob>1985-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Brian Burlet</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1970-02-01</dob>
        <percentage>61.0</percentage>
        <studentName>Renard konig</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1993-02-01</dob>
        <percentage>92.0</percentage>
        <studentName>Rita Paul</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1965-02-01</dob>
        <percentage>83.0</percentage>
        <studentName>Han Yenn</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1977-02-01</dob>
        <percentage>76.0</percentage>
        <studentName>Harry Scout</studentName>
    </ExamResult>
   <ExamResult>
        <dob>1984-02-25</dob>
        <percentage>89.0</percentage>
        <studentName>Naveen Kumar</studentName>
    </ExamResult>
    <ExamResult>
        <dob>1990-06-04</dob>
        <percentage>94.0</percentage>
        <studentName>Chaitra Dixit</studentName>
    </ExamResult>
   <ExamResult>
        <dob>2010-04-20</dob>
        <percentage>56.0</percentage>
        <studentName>Kanchan N</studentName>
    </ExamResult>
   
   <ExamResult>
        <dob>1992-02-03</dob>
        <percentage>77</percentage>
        <studentName>Krishna Mohan</studentName>
    </ExamResult>
    
</UniversityExamResultList>
------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------
Spring Security  4.x 
------------------------------------------------------------------------------------------------------------
pom.xml
------------------------------------------------------------------------------------------------------------
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.spring.security</groupId>
	<artifactId>SpringSecurity4.0Version</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>war</packaging>

	<properties>
		<springframework.version>4.1.6.RELEASE</springframework.version>
		<springsecurity.version>4.0.1.RELEASE</springsecurity.version>
	</properties>

	<dependencies>
		<!-- Spring -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>${springframework.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-web</artifactId>
			<version>${springframework.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
			<version>${springframework.version}</version>
		</dependency>

		<!-- Spring Security -->
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-web</artifactId>
			<version>${springsecurity.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-config</artifactId>
			<version>${springsecurity.version}</version>
		</dependency>

		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.1.0</version>
		</dependency>
		<dependency>
			<groupId>javax.servlet.jsp</groupId>
			<artifactId>javax.servlet.jsp-api</artifactId>
			<version>2.3.1</version>
		</dependency>
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>jstl</artifactId>
			<version>1.2</version>
		</dependency>
	</dependencies>




	<build>
		<sourceDirectory>src</sourceDirectory>
		<plugins>
			<plugin>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.3</version>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
				</configuration>
			</plugin>
			<plugin>
				<artifactId>maven-war-plugin</artifactId>
				<version>2.6</version>
				<configuration>
					<warSourceDirectory>WebContent</warSourceDirectory>
					<failOnMissingWebXml>false</failOnMissingWebXml>
				</configuration>
			</plugin>

			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-war-plugin</artifactId>
				<version>2.4</version>
				<configuration>
					<warSourceDirectory>WebContent</warSourceDirectory>
					<warName>SpringSecurityHelloWorldAnnotationExample</warName>
					<failOnMissingWebXml>false</failOnMissingWebXml>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
------------------------------------------------------------------------------------------------------------
package com.naveen.springsecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.naveen.springsecurity")
public class HelloWorldConfiguration {

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;
	}

}
------------------------------------------------------------------------------------------------------------
package com.naveen.springsecurity.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
 
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("bill").password("abc123").roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password("root123").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("dba").password("root123").roles("ADMIN","DBA");
        //dba have two roles.
        
        System.out.println("in configure global security method ");
    }
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
  
      http.authorizeRequests()
        .antMatchers("/", "/home").permitAll() 
        .antMatchers("/admin/**").access("hasRole('ADMIN')")
        .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
        .and().formLogin()
        .and().exceptionHandling().accessDeniedPage("/Access_Denied");
      
      	System.out.println("in Configure http ");
    }
}
------------------------------------------------------------------------------------------------------------
package com.naveen.springsecurity.configuration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	// have not done anything here, but can override the methods
	
	/* equal to 
	 * <filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
 
<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
	 */
}
------------------------------------------------------------------------------------------------------------
package com.naveen.springsecurity.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringMvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { HelloWorldConfiguration.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
}
------------------------------------------------------------------------------------------------------------
package com.naveen.springsecurity.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloWorldController {

	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public String homePage(ModelMap model) {
		model.addAttribute("greeting", "Hi, Welcome to mysite. ");
		return "welcome";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(ModelMap model) {
		System.out.println("in admin  Controller ");

		model.addAttribute("user", getPrincipal());
		return "admin";
	}

	@RequestMapping(value = "/db", method = RequestMethod.GET)
	public String dbaPage(ModelMap model) {
		System.out.println("in DB Controller ");
		model.addAttribute("user", getPrincipal());
		return "dba";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "welcome";
	}

	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("user", getPrincipal());
		return "accessDenied";
	}

	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
}
------------------------------------------------------------------------------------------------------------
accessDenied.jsp
------------------------------------------------------------------------------------------------------------
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
  Dear <strong>${user}</strong>, You are not authorized to access this page
    <a href="<c:url value="/logout" />">Logout</a>
</body>
</html>
------------------------------------------------------------------------------------------------------------
admin.jsp
------------------------------------------------------------------------------------------------------------
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	Dear
	<strong>${user}</strong>, Welcome to Admin Page.
	<a href="<c:url value="/logout" />">Logout</a>
</body>
</html>


------------------------------------------------------------------------------------------------------------
dba.jsp
------------------------------------------------------------------------------------------------------------
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	Dear
	<strong>${user}</strong>, Welcome to DBA Page.
	<a href="<c:url value="/logout" />">Logout</a>
</body>
</html>
------------------------------------------------------------------------------------------------------------
welcome.jsp
------------------------------------------------------------------------------------------------------------
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
		Greeting : ${greeting} This is a welcome page.
</body>
</html>
------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------