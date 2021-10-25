package lhbruse;

import lhbruse.annotation.EnableWebMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Hello world!
 */
@EnableWebMvc
@ComponentScan(basePackages = "lhbruse")
@Configuration
public class App {
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }
}
