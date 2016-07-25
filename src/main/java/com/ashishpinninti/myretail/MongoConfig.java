package com.ashishpinninti.myretail;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * This is Mongo configuration class.
 * 
 * @author apinninti
 *
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.ashishpinninti.myretail.repository")
public class MongoConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return MyretailConstants.DB_NAME;
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient(MyretailConstants.LOCALHOST_IP,
				MyretailConstants.MONGO_PORT);
	}

	@Override
	protected String getMappingBasePackage() {
		return "org.ashishpinninti.myretail";
	}
}
