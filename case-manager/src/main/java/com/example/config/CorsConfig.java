package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
	/* 配置跨域过滤器 */
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); // 是否允许携带cookie，默认情况下，cors不会携带cookie，除非这个值是true
		config.addAllowedOrigin("http://localhost:5173"); // 对应前端服务的域名
		config.addAllowedHeader("*"); // 允许携带的头
		config.addAllowedMethod("*"); // 允许访问的方式
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
