package cn.zhaizq.sso.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("cn.zhaizq.sso.service.mapper")
@ComponentScan(basePackages = {"cn.zhaizq.sso"})
public class Application extends SpringBootServletInitializer {
	public static void main(String[] args) {
		new SpringApplication(Application.class).run();
	}
}