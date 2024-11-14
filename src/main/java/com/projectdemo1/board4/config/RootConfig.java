package com.projectdemo1.board4.config;



import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
                return modelMapper;

    }
 /*   @SpringBootApplication(scanBasePackages = "com.projectdemo1")
    public class Projectdemo1Application {
        public static void main(String[] args) {
            SpringApplication.run(Projectdemo1Application.class, args);
        }
    }*/
}
