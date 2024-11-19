package com.projectdemo1.config;

import com.projectdemo1.converter.StringToPetColorTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToPetColorTypeConverter());
    } //enum 타입의 color를 string으로 만들어서 타입캐스팅하는 역할. color가 enum이 아니면 필요없음
}
