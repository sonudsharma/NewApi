package com.spring.config;

import static org.hibernate.cfg.AvailableSettings.FORMAT_SQL;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@ComponentScans(value = { @ComponentScan("com.dao"), @ComponentScan("com.service"), @ComponentScan("com.controller"), @ComponentScan("com.token") })
public class AppConfig implements WebMvcConfigurer {

	@Autowired
	private Environment env;

	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getRequiredProperty("mysql.driver"));
		dataSource.setUrl(env.getRequiredProperty("mysql.url"));
		dataSource.setUsername(env.getRequiredProperty("mysql.user"));
		dataSource.setPassword(env.getRequiredProperty("mysql.password"));
		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		factoryBean.setDataSource(getDataSource());

		Properties props = new Properties();
		// Setting JDBC properties
		// props.put(DRIVER, env.getProperty("mysql.driver"));
		// props.put(URL, env.getProperty("mysql.url"));
		// props.put(USER, env.getProperty("mysql.user"));
		// props.put(PASS, env.getProperty("mysql.password"));

		// Setting Hibernate properties
		props.put(SHOW_SQL, env.getProperty("hibernate.show_sql"));
		props.put(FORMAT_SQL, env.getProperty("hibernate.format_sql"));
		/*
		 * // Setting C3P0 properties props.put(C3P0_MIN_SIZE,
		 * env.getProperty("hibernate.c3p0.min_size")); props.put(C3P0_MAX_SIZE,
		 * env.getProperty("hibernate.c3p0.max_size"));
		 * props.put(C3P0_ACQUIRE_INCREMENT,
		 * env.getProperty("hibernate.c3p0.acquire_increment"));
		 * props.put(C3P0_TIMEOUT, env.getProperty("hibernate.c3p0.timeout"));
		 * props.put(C3P0_MAX_STATEMENTS,
		 * env.getProperty("hibernate.c3p0.max_statements"));
		 */

		factoryBean.setHibernateProperties(props);
		factoryBean.setPackagesToScan("com.models");
		// factoryBean.setAnnotatedClasses(Users.class);

		return factoryBean;
	}

	@Bean
	public static PropertyPlaceholderConfigurer properties() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		Resource[] resources = new ClassPathResource[] { new ClassPathResource("message_en.properties") };
		ppc.setLocations(resources);
		ppc.setIgnoreUnresolvablePlaceholders(true);
		return ppc;
	}

	@Bean
	public HibernateTransactionManager getTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(getSessionFactory().getObject());
		return transactionManager;
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		return resolver;
	}

	@Bean
	public StringHttpMessageConverter getStringHttpMessageConverter() {
		return new StringHttpMessageConverter();
	}

	@Bean
	public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter();
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/");
	}

	@Bean
	public QuickbloxChatInterceptor getChatInterceptor() {
		return new QuickbloxChatInterceptor();
	}

    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor(getChatInterceptor());

    }
}
