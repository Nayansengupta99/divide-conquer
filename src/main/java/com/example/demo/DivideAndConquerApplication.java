package com.example.demo;

import java.util.Properties;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;



@SpringBootApplication
@EnableMongoRepositories
public class DivideAndConquerApplication {

	public static void main(String[] args) {
		//SpringApplication.run(DivideAndConquerApplication.class, args);
		
		Properties props = new Properties();
		String mongoPass = System.getenv("MONGOPASS");
		String mongoDBUrl = "mongodb+srv://nayan97:" + mongoPass
				+ "@cluster0.cgcpm.mongodb.net/bill?retryWrites=true&w=majority";
		props.put("server.port", "8081");
		props.put("spring.data.mongodb.uri", mongoDBUrl);
		props.put("spring.data.mongodb.databasee", "bill");
		props.put("spring.jpa.defer-datasource-initialization", "true");

		new SpringApplicationBuilder(DivideAndConquerApplication.class).properties(props).run(args);
	}

}
