package softuni.exam.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.exam.util.ValidatorImpl;
import softuni.exam.util.ValidatorInterface;

@Configuration
public class ApplicationBeanConfiguration {
    //Added with dependency
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    //Added with dependency
    @Bean
    public Gson gson(){
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }
    //Added from custom class
    @Bean
    public ValidatorInterface validatorInterface(){
        return new ValidatorImpl();
    }
}
