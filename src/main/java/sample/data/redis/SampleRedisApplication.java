/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.data.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication
//@EnableRedisRepositories(basePackageClasses = {SampleBeanRepository.class})
@EnableElasticsearchRepositories(basePackageClasses = {SampleBeanESRepository.class})
public class SampleRedisApplication {

	private static final Logger logger = LoggerFactory.getLogger(SampleRedisApplication.class);

	public static void main(String[] args) throws Exception {
		// Close the context so it doesn't stay awake listening for redis
		ConfigurableApplicationContext context = SpringApplication.run(SampleRedisApplication.class, args);

		SampleBean helloSampleBean = new SampleBean(UUID.randomUUID().toString(), "hello", LocalDate.of(2020, 12, 12), LocalDateTime.now());
		ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
		logger.info("Expecting date to be written as: " + objectMapper.writeValueAsString(helloSampleBean.date));

//		SampleBeanRepository repository = context.getBean(SampleBeanRepository.class);
//		String id = repository.save(helloSampleBean).id;

		SampleBeanESRepository esRepository = context.getBean(SampleBeanESRepository.class);
		String id = esRepository.save(helloSampleBean).id;
        logger.info("Now it is written");

		logger.info("BEAN ID: {}", id);
//		logger.info("Bean from redis: {}", repository.findOne(id).toString());
		logger.info("Bean from ES: {}", esRepository.findOne(id).toString());

		context.close();
	}

}
