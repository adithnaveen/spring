-------------------------------------------------------------------------------------------------------------
pom.xml 
-------------------------------------------------------------------------------------------------------------

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.naveen</groupId>
	<artifactId>SpringWork</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<properties>
		<spring.version>4.0.5.RELEASE</spring.version>
	</properties>	

	<dependencies>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>${spring.version}</version>
		</dependency>
		
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context</artifactId>
			<version>${spring.version}</version>
		</dependency>
	

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-orm</artifactId>
			<version>${spring.version}</version>
		</dependency>


		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-aop</artifactId>
			<version>${spring.version}</version>
		</dependency>
		
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jdbc</artifactId>
			<version>${spring.version}</version>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-aspects</artifactId>
			<version>${spring.version}</version>
		</dependency>
		
	<dependency>
		<groupId>cglib</groupId>
		<artifactId>cglib</artifactId>
		<version>2.2.2</version>
	</dependency>
	</dependencies>
	
	
	
	
</project>

---------------------------------------------------

Spring basics 

package com.naveen01.basics;

public interface IHelloService {
	public String sayHello();
	public String sayHello(String name, String city);
}	

-------------------------------------------------------------------------------------------

package com.naveen01.basics;

public class DefaultHello implements IHelloService{
	private String name;
	private String city;
	
	// Its good to have default constructor
	// if you dont have default constructor then use setters
	public DefaultHello(){}

	public DefaultHello(String name, String city) {
		this.name = name;
		this.city = city;	}

	@Override
	public String toString() {
		return "DefaultHello [name=" + name + ", city=" + city + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String sayHello(String name, String city) {
		String myStrs[]={
				"Hey How are you Mr/Ms %s & Howz your city %s", 
				"Good Morning %s and heard you are in %s",
				"There is some thing good %s in your city %s"
		};
		
		int randNumber = (int) (Math.random() * 3);
		System.out.println("Random Number generated is " + randNumber);
		return String.format(myStrs[randNumber].toString(), name, city);
	}
	public String  sayHello() {
		return sayHello(name, city);
		
	}
	

}


----------------------------------------------------------------------------------------------

package com.naveen01.basics;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientApp {
	public static void main(String[] args) {
		
		
		// you can also use BeanFactory which Spring, has deprecated as of now 
		// XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("basicsApplication.xml"));

		// reference to spring container 
		ApplicationContext context = new ClassPathXmlApplicationContext("basicsApplication.xml");
		
		IHelloService hello = (IHelloService) context.getBean("helloWorld");
		
		System.out.println(hello.sayHello());
	}
}

---------------------------------------------------------------------------------------------------
The Below program show working of Highly Cohesive
 -Loosely coupled System with annotation
---------------------------------------------------------------------------------------------------
package spring.training;

public interface Greeting {
	public String sayHello();
}


---------------------------------------------------------------------------------------------------
package spring.training;

public class GreetFrench implements Greeting{

	public String sayHello() {
		return "bonjour tout le monde";
	}
	
}

---------------------------------------------------------------------------------------------------
package spring.training;

public class GreetEnglish implements Greeting{

	public String sayHello() {
		return "Hello World"; 
	}

}

---------------------------------------------------------------------------------------------------
package spring.training;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Bean(name="english")
	public Greeting english(){
		return new GreetEnglish();
	}
	
	@Bean(name="french")
	public Greeting french(){
		return new GreetFrench();
	}
}	

---------------------------------------------------------------------------------------------------
package spring.training;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Client {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		
		Greeting greet = (Greeting) context.getBean("french");

		System.out.println(greet.sayHello());
	
	}
}


---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
Bean Post Processor 
---------------------------------------------------------------------------------------------------

beanlifecycle.xml 
-------------------------------
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
	   http://www.springframework.org/schema/beans/spring-beans.xsd"
	   default-init-method="myInit" default-destroy-method="myDest">

   	<!--  property information can be given with the spring specified class, and giving the property file name  -->
   	 <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="location">
            <value>employeeinfo.properties</value>
        </property>
    </bean>

<!--  if the scope type is prototype then the destroy method is not called.  -->
	<bean id="employee"	 class="com.naveen02.beanlifecycle.Employee"
		 init-method="myInit" destroy-method="myDest">
		 <property name="empId" value="${employee.empId}"/>
		 <property name="empName" value="${employee.empName}"/>
		 <property name="empSal" value="${employee.empSal}"/>
	</bean>	

   	<!--  bean post processor -->
   	<bean class="com.naveen02.beanlifecycle.DisplayBeanPostProcessor"/>
   	
   	
   	<!--  bean post processor factory, spring will call all the classes which implements the BeanFactoryPostProcessor -->
   <!-- 	<bean class="com.naveen02.beanlifecycle.MyBeanFactory"/>
   	 -->
     	
</beans>

	   					   


--------------------------------------------------------------------------------------------------------------

package com.naveen02.beanlifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


// this class will have two more methods which are called init & destroy the 
// name of the methods can be any thing, which we specify in the .xml file 


public class Employee implements InitializingBean, DisposableBean{
	private int empId;
	private String empName;
	private double empSal;

	public Employee(){
		System.out.println("Employee Created... ");
	}
	
	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", empSal=" + empSal + "]";
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public double getEmpSal() {
		return empSal;
	}
	public void setEmpSal(double empSal) {
		this.empSal = empSal;
	}
	

	public void myInit(){
		System.out.println("Init method for Employee called");
	}
	
	public void myDest(){
		System.out.println("Destory Method for Employee Called... ");
		
	}


	public void myInit1(){
		System.out.println("Init1 method for Employee called");
	}
	
	public void myDest1(){
		System.out.println("Destory1 Method for Employee Called... ");
	}


	// coming from InitializingBean, DisposableBean interfaces 
	public void destroy(){
		System.out.println("Disposable Bean Employee Called... ");
		
	}
	public void afterPropertiesSet() throws Exception {
		// We can write any BL here 
		System.out.println("After Properties Set is called.. ");
	}	
	
}

--------------------------------------------------------------------------------------------------------------

package com.naveen02.beanlifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

// this class can help modify or display the class name. 
public class DisplayBeanPostProcessor implements BeanPostProcessor {
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("Before Initialization called, for Class ->" + beanName);
		if(bean instanceof Employee){
			Employee emp = (Employee) bean;
			// here we check whether the object created is of type Employee if so 
			// the are setting/changing the name
			emp.setEmpName("Mahesh");
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("After Initialization called, for Class -> " + beanName);
		return bean;
	}

}

-------------------------------------------------------------------------------------------------------------

package com.naveen02.beanlifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;


// this will be called before the bean factory is initialized 
// and have an entry of the same in .xml 
public class MyBeanFactory implements BeanFactoryPostProcessor{

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		System.out.println("Bean Factory Post Processor Intitialized... ");
	}
}

-------------------------------------------------------------------------------------------------------------

package com.naveen02.beanlifecycle;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientApp {
	public static void main(String[] args) {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("beanlifecycle.xml");
		
		Employee employee = context.getBean("employee", Employee.class);
		
		System.out.println(employee);
		
		
		// the init and destory method which is specified in beanlifecycle.xml file is 
		// called only if you use registerShutdonwHook 
		
		// ensure that the scope is not prototype if so the destroy method is not called. 
	 	// context.registerShutdownHook();
	}
}

---------------------------------------------------------------------------------------------------------------
Bean Auto Wiring with annotation but this program first show without anontation the configuration information is in .xml file with autowire properly in .xml file 
--------------------------------------------------------------------------------------------------------------
package com.naveen03.beanautowire01;

import org.springframework.stereotype.Component;

@Component
public class Camera {
	private String type;

	public Camera() {
		System.out.println("Creating Camera " + this );
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

--------------------------------------------------------------------------------------------------------------
package com.naveen03.beanautowire01;

import org.springframework.stereotype.Component;

@Component
public class Screen {
	private String glassType;
	private int len;
	private int bre;
	
	public Screen() {
		System.out.println("Creating Screen " + this);
	}
	
	public String getGlassType() {
		return glassType;
	}
	public void setGlassType(String glassType) {
		this.glassType = glassType;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public int getBre() {
		return bre;
	}
	public void setBre(int bre) {
		this.bre = bre;
	}
	
	
}

--------------------------------------------------------------------------------------------------------------
package com.naveen03.beanautowire01;

import org.springframework.stereotype.Component;

@Component
public class Speaker {
	private String type;
	private int volLevels;
	
	 public Speaker() {
		 System.out.println("Creating Speaker " + this );
	 }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getVolLevels() {
		return volLevels;
	}
	public void setVolLevels(int volLevels) {
		this.volLevels = volLevels;
	}
	
}

--------------------------------------------------------------------------------------------------------------
package com.naveen03.beanautowire01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mobile {
	private Screen scr;
	private Speaker speaker;
	private Camera camera;

	public Mobile() {
		System.out.println("Creating Mobile " + this);
	}
	
	public Speaker getSpeaker() {
		return speaker;
	}

	@Autowired
	public void setSpeaker(Speaker speaker) {
		System.out.println("Setting Speaker " + speaker);
		this.speaker = speaker;
	}

	public Camera getCamera() {
		return camera;
	}


	@Autowired
	public void setCamera(Camera camera) {
		System.out.println("Setting Camera " + camera);
		this.camera = camera;
	}

	public Screen getScr() {
		return scr;
	}

	@Autowired
	public void setScr(Screen scr) {
		System.out.println("Setting Screen " + scr);
		this.scr = scr;
	}
	
	@Override
	public String toString() {
		return "Mobile [screen=" + scr + ", speaker=" + speaker + ", camera=" + camera + "]";
	}

}

--------------------------------------------------------------------------------------------------------------
package com.naveen03.beanautowire01;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientMobile {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("mobilecontext.xml");
		Mobile mobile = (Mobile) context.getBean("mobile");
/*		
		System.out.println("__________________________________________________");
		System.out.println("Program to show working of auto beanAutwire ");
		System.out.println("__________________________________________________");
		System.out.println("Camera Details :"  + mobile.getCamera());
		System.out.println("Speaker Details :" + mobile.getSpeaker());
		System.out.println("Screen Details " + mobile.getScr().getBre() +", " + mobile.getScr().getLen());
*/	
		System.out.println(mobile);
		
	
	}
}

--------------------------------------------------------------------------------------------------------------
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd">

	
	<context:annotation-config/>
	<context:component-scan base-package="com.naveen03.beanautowire01" />

	<!-- Introduced Qualifier which refers to the @Qualifer in the class Screen 
		// qualifer will be useful when you have the variable name different in the 
		class and the bean id is different and you still want to use @Autowire, then 
		we can use @Qualifier -->
<!-- 	<bean id="screen" class="com.naveen03.beanautowire01.Screen">
		<property name="glassType" value="tuffen" />
		<property name="len" value="5" />
		<property name="bre" value="3" />
	</bean>

	<bean id="speaker" class="com.naveen03.beanautowire01.Speaker">
		<property name="type" value="mono" />
		<property name="volLevels" value="10" />
	</bean>

	<bean id="camera" class="com.naveen03.beanautowire01.Camera">
		<property name="type" value="vga" />
	</bean>

	<bean id="mobile" class="com.naveen03.beanautowire01.Mobile">
		<property name="camera" ref="camera"/>
		<property name="speaker" ref="speaker"/>
		<property name="scr" ref="screen"/>
	</bean>
 -->

 
 
 </beans>

	   					   

--------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------
Collection 
-------------------------------------------------------------------------------------------------------------

package com.naveen04.collections;

import java.util.List;

public class QuestionBank {
	private String question;
	private List<String> options;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	@Override
	public String toString() {
		return "QuestionBank [question=" + question + ", options=" + options + "]";
	}
		
}

----------------------------------------------------------------------------------------------------------

package com.naveen04.collections;

import java.util.Set;

public class AddressSet {
	private Set<String> addressSet;

	public Set<String> getAddressSet() {
		return addressSet;
	}

	public void setAddressSet(Set<String> addressSet) {
		this.addressSet = addressSet;
	}

	@Override
	public String toString() {
		return "AddressSet [addressSet=" + addressSet + "]";
	}
	
	
	
}



---------------------------------------------------------------------------------------------------------------
package com.naveen04.collections;

import java.util.Map;

public class AccountMap {
	private Map<String, Long> accountMap;

	public Map<String, Long> getAccountMap() {
		return accountMap;
	}

	public void setAccountMap(Map<String, Long> accountMap) {
		this.accountMap = accountMap;
	}

	@Override
	public String toString() {
		return "AccountMap [accountMap=" + accountMap + "]";
	} 
	
	
}


-------------------------------------------------------------------------------------------------------------

package com.naveen04.collections;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyCollection {
	List  questionBank;
	Set addressSet;
	Map accountMap;
	
	
	public List getQuestionBank() {
		return questionBank;
	}
	public void setQuestionBank(List questionBank) {
		this.questionBank = questionBank;
	}
	public Set getAddressSet() {
		return addressSet;
	}
	public void setAddressSet(Set addressSet) {
		this.addressSet = addressSet;
	}
	public Map getAccountMap() {
		return accountMap;
	}
	public void setAccountMap(Map accountMap) {
		this.accountMap = accountMap;
	}
	@Override
	public String toString() {
		return "MyCollection [questionBank=" + questionBank + ", addressSet=" + addressSet + ", accountMap="
				+ accountMap + "]";
	}
	
	
}

---------------------------------------------------------------------------------------------------------------
package com.naveen04.collections;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientApp {
	public static void main(String[] args) {
		ApplicationContext context = new 
			ClassPathXmlApplicationContext("collectionApplication.xml");
		QuestionBank qb = (QuestionBank) context.getBean("questionList");
		System.out.println(qb);
		
		AddressSet myAddress = context.getBean("addressSet", AddressSet.class);
		System.out.println(myAddress);
		
		AccountMap aMap = (AccountMap) context.getBean("accountMap");
		
		System.out.println(aMap);
		
		System.out.println("-------------------------------------------------------------");
		MyCollection myColl = context.getBean("myCollection", MyCollection.class);
		System.out.println(myColl);
	}
}

---------------------------------------------------------------------------------------------------------------


<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
	   http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="questionList" class="com.naveen04.collections.QuestionBank">
		<property name="question" value="Inheritence is the feature of which language?"/>
		 <property name="options">
   		<list>
            <value>JAVA</value>
            <value>C++</value>
            <value>C Programming</value>
            <value>Scala</value>
         </list>
        </property>
	</bean>	

	<bean id="addressSet" class="com.naveen04.collections.AddressSet">
		 <property name="addressSet">
			<set>
				<value>Richmod Road, Bengaluru</value>
				<value>Richmod Road, Bengaluru</value>
				<value>MG Road, Bengaluru</value>
				<value>Cottonpet, Bengaluru</value>
			</set>
        </property>
	</bean>	
	
	
	<bean id="accountMap" class="com.naveen04.collections.AccountMap">
		 <property name="accountMap">
			<map>
				<entry key="1001" value="20000"/>
				<entry key="1001" value="20000"/>
				<entry key="1002" value="30000"/>
				<entry key="1003" value="40000"/>
				
			</map>
        </property>
	</bean>	
	
	
	<bean id="myCollection" class="com.naveen04.collections.MyCollection">
		<property name="questionBank" ref="questionList"/>
		<property name="addressSet" ref="addressSet"/>
		<!--  dont have it for map as it needs key value pair  -->
	</bean>
	
   	
</beans>





----------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------
Message Resource to show working of Internationalization 
----------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------
package com.naveen05.messagesource;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class HelloWorld {
	private String greeting;
	private String message;
	
	@Autowired
	private MessageSource messageSource;
	
	public String getGreeting() {
		return greeting;
	}
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void sayHelloWorld(){
		System.out.println(messageSource.getMessage("helloworld.greet", new Object[]{greeting, message},
				"Good Morning...", null) +", ");		
	}
	
	public void sayHelloWorld_fr(){
		System.out.println(messageSource.getMessage("helloworld.greet", new Object[]{greeting, message},
				"Good Morning...",  new Locale("FR")) +", ");		
	}
}



----------------------------------------------------------------------------------------------------------------------
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context-3.0.xsd">


	<context:annotation-config/>
	
	<!--  by default every bean is the singleton, if you want the bean to be 
		create every time you can pass it as scope="prototype" in bean tag -->
	<bean id="helloWorld" class="com.naveen05.messagesource.HelloWorld">
		<property name="greeting" value="Naveen"/>
		<property name="message" value="How are you today?"/>
		
	</bean>	   	
	   	
	<bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
		<property name="basenames">
			<list>
				<!--  no need to give .properties -->
				<value>mymessages</value>
			</list>
		</property>
	</bean>
</beans>
----------------------------------------------------------------------------------------------------------------------
mymessages.properties
----------------------------------------------------------------------------------------------------------------------
goodmorning=shubhodaya
helloworld.greet=({0}, Hope you are doing well, and i wanted to say {1})

----------------------------------------------------------------------------------------------------------------------
mymessages_fr.properties
----------------------------------------------------------------------------------------------------------------------
goodmorning=BonJour
helloworld.greet=({0}, comment ca va {1})

----------------------------------------------------------------------------------------------------------------------
package com.naveen05.messagesource;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("messageSource.xml");
		System.out.println(context.getMessage("goodmorning", new Object[]{"Naveen", "kumar"},   new Locale("FR")));

		System.out.println("----------------------------------------------------------------------");
		System.out.println(context.getMessage("goodmorning", new Object[]{"Naveen", "kumar"}, "Good Morning in English", null));
		
		System.out.println("----------------------------------------------------------------------");
		
		HelloWorld hw = (HelloWorld) context.getBean("helloWorld");
		hw.sayHelloWorld();
		hw.sayHelloWorld_fr();
	}
}

----------------------------------------------------------------------------------------------------------------------


----------------------------------------------------------------------------------------------------------------------

Spring DB
----------------------------------------------------------------------------------------------------------------------

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
	   http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="dataSource"
		class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="oracle.jdbc.driver.OracleDriver" />
		<property name="url" value="jdbc:oracle:thin:@10.0.0.16:1521:xe" />
		<property name="username" value="naveen" />
		<property name="password" value="naveen" />
	</bean>

   <!-- Definition for studentJDBCTemplate bean -->
	<bean id="studentJDBCTemplate" 
      class="com.naveen.dao.StudentJDBCTemplate">
      <property name="dataSource"  ref="dataSource" />    
   </bean>
      
</beans>

	   					   

_________________________________________________________
Student DAO.java
_________________________________________________________
package com.naveen.interfaces;

import java.util.List;

import javax.sql.DataSource;

import com.naveen.entity.Student;

public interface StudentDAO {

	/** 
	 * to get student object from procedure
	 * 
	 */
	public Student getStudentFromProcedure(int id);

	/**
	 * This is the method to be used to initialize database resources ie.
	 * connection.
	 */
	public void setDataSource(DataSource ds);

	/**
	 * This is the method to be used to create a record in the Student table.
	 */
	public void create(int id, String name, Integer age);

	/**
	 * This is the method to be used to list down a record from the Student
	 * table corresponding to a passed student id.
	 */
	public Student getStudent(Integer id);

	/**
	 * This is the method to be used to list down all the records from the
	 * Student table.
	 */
	public List<Student> listStudents();

	/**
	 * This is the method to be used to delete a record from the Student table
	 * corresponding to a passed student id.
	 */
	public void delete(Integer id);

	/**
	 * This is the method to be used to update a record into the Student table.
	 */
	public void update(Integer id, Integer age);
}



_______________________________________________
Student.java
_________________________________________________
package com.naveen.entity;

public class Student {
	private Integer age;
	private String name;
	private Integer id;

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getAge() {
		return age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Student [age=" + age + ", name=" + name + ", id=" + id + "]";
	}

}




--------------------------------------------------------
StudentMApper.java
______________________________________________________
package com.naveen.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.naveen.entity.Student;

public class StudentMapper implements RowMapper<Student> {
	public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
		Student student = new Student();
		student.setId(rs.getInt("id"));
		student.setName(rs.getString("name"));
		student.setAge(rs.getInt("age"));
		return student;
	}
}


_________________________________________________________
StudentJDBCTemplate.java
_________________________________________________________
package com.naveen.dao;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.object.StoredProcedure;

import com.naveen.entity.Student;
import com.naveen.interfaces.StudentDAO;

public class StudentJDBCTemplate implements StudentDAO {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	private SimpleJdbcCall simpleJdbcCall;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);

	}

	private class StudentSP extends StoredProcedure {
		private static final String SPROC_NAME = "PR_GETRECORD";

		public StudentSP(DataSource datasource) {
			super(datasource, SPROC_NAME);
			declareParameter(new SqlParameter("pr_id", Types.INTEGER)); // declaring
																		// sql
																		// in
																		// parameter
																		// to
																		// pass
																		// input
			declareParameter(new SqlOutParameter("pr_name", Types.VARCHAR));
			declareParameter(new SqlOutParameter("pr_age", Types.INTEGER));
			
			// declaring
						
		}

		public Student execute(int sid) {
			Map<String, Object> results = super.execute(sid);
			
			Student student = new Student();
			student.setId(sid);
			student.setName(results.get("pr_name").toString());
			student.setAge(Integer.parseInt(results.get("pr_age").toString()));
			
			
			return student; 	
		}
	}

	public Student getStudentFromProcedure(int id) {
		
		return new StudentSP(dataSource).execute(102);
		
	}

	public void create(int id, String name, Integer age) {
		String SQL = "insert into Student (id, name, age) values (?, ?, ?)";

		jdbcTemplateObject.update(SQL, id, name, age);
		System.out.println("Created Record Name = " + name + " Age = " + age);
		return;
	}

	public Student getStudent(Integer id) {
		String SQL = "select * from Student where id = ?";
		//Student student = jdbcTemplateObject.queryForObject(SQL, new Object[] { id },
		//		new BeanPropertyRowMapper<Student>(Student.class));
		
		
		System.out.println("using student mapper ");
		return jdbcTemplateObject.queryForObject(SQL, new Object[]{id}, new StudentMapper());
		
		
		// return student;
	}
	




	public int getNumberOfStudents(){
		String sql = "SELECT COUNT(*) FROM student";
		 
		
		int total = jdbcTemplateObject.queryForInt(sql);
	 
		return total;

	}






	public List<Student> listStudents() {
		String SQL = "select * from Student";

		// first way
		List<Student> studs = jdbcTemplateObject.query(SQL, new BeanPropertyRowMapper<Student>(Student.class));

		// second way
		/*
		 * List<Student> students = new ArrayList<Student>(); List<Map<String,
		 * Object>> rows = jdbcTemplateObject.queryForList(SQL);
		 * 
		 * for(Map row : rows){ Student temp = new Student();
		 * temp.setId(Integer.parseInt(row.get("id").toString()));
		 * temp.setName((String)row.get("name"));
		 * temp.setAge(Integer.parseInt(row.get("age").toString()));
		 * 
		 * students.add(temp); }
		 */

		return studs;
	}

	public void delete(Integer id) {
		String SQL = "delete from Student where id = ?";
		jdbcTemplateObject.update(SQL, id);
		System.out.println("Deleted Record with ID = " + id);
		return;
	}

	public void update(Integer id, Integer age) {
		String SQL = "update Student set age = ? where id = ?";
		jdbcTemplateObject.update(SQL, age, id);
		System.out.println("Updated Record with ID = " + id);
		return;
	}

}



_____________________________________________________
Client.java
____________________________________________________

package com.naveen.client;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.naveen.dao.StudentJDBCTemplate;
import com.naveen.entity.Student;

public class Client {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("springdb.xml");

		StudentJDBCTemplate studentJDBCTemplate = (StudentJDBCTemplate) context.getBean("studentJDBCTemplate");

		/** insert  */ 
		// System.out.println("------Records Creation--------");
		// studentJDBCTemplate.create(101, "Zara", 11);
		// studentJDBCTemplate.create(102, "Nuha", 2);
		// studentJDBCTemplate.create(103, "Ayan", 15);

		// studentJDBCTemplate.delete(101);

		// studentJDBCTemplate.update(102, 22);

		// System.out.println(studentJDBCTemplate.getStudent(102));
/*
		System.out.println("------Listing Multiple Records--------");
		List<Student> students = studentJDBCTemplate.listStudents();
		for (Student record : students) {
			System.out.print("ID : " + record.getId());
			System.out.print(", Name : " + record.getName());
			System.out.println(", Age : " + record.getAge());
		}

	*/	
		System.out.println("+++++++++++++++++++++++++++");
		
		System.out.println(studentJDBCTemplate.getStudentFromProcedure(102));
		
	}
}




