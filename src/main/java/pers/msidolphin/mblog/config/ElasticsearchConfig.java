package pers.msidolphin.mblog.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Order(1)
@Configuration
@EnableAutoConfiguration
public class ElasticsearchConfig {

	@Bean
	public TransportClient elasticsearchClient() throws UnknownHostException {
		InetSocketTransportAddress master = new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300);
		InetSocketTransportAddress slave_01 = new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300);

		Settings settings = Settings.builder().put("cluster.name", "elastic").build();
		TransportClient elasticsearchClient = new PreBuiltTransportClient(settings);
		elasticsearchClient.addTransportAddress(master).addTransportAddress(slave_01);
		return elasticsearchClient;
	}
}
