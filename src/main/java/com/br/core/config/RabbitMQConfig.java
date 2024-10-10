package com.br.core.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	@Bean
	public Queue queue() {
		return new Queue("department-user"); //Existe alguns padrões defendidos: User.v1.user-created.
		//nome do domínio{User}, versão do evento{v1}, e o tipo do evento{user-created}.
	}
	@Bean
	public Queue queueDocument() {
		return new Queue("department-document");
	}
	
	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}
	
	@Bean
	public ApplicationListener<ApplicationReadyEvent> 
	applicationReadyEventApplicationListener(RabbitAdmin admin) {
		return event -> admin.initialize();
	}
	
	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	// a API de service-registre deve esta rodando para retornar um corpo json o bean abaixo converte para um json
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
			Jackson2JsonMessageConverter converter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(converter);
		return rabbitTemplate;
	}

}
