package com.web.mavenproject6.config;

import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.web.mavenproject6.forms.MailForm;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.servlet.MultipartConfigElement;
import javax.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
@PropertySource("resources/application.properties")
@ComponentScan(basePackages = "com.web.mavenproject6")
@EnableWebMvc
@Import({AppSecurityConfig.class})
@EnableScheduling

public class MvcConfiguration extends WebMvcConfigurerAdapter implements SchedulingConfigurer {

    @Value("${mail.subject}")
    private String mailSubject;
    @Value("${mail.from}")
    private String mailFrom;
    @Value("${mail.website.name}")
    private String mailWebsiteName;
    @Value("${mail.host}")
    private String mailHost;
    @Value("${mail.smtp.host}")
    private String mailSmtpHost;
    @Value("${mail.smtp.username}")
    private String mailSmtpUsername;
    @Value("${mail.smtp.password}")
    private String mailSmtpPassword;
    @Value("${mail.smtp.port}")
    private String mailSmtpPort;
    @Value("${mail.smtp.auth}")
    private String mailSmtpAuth;
    @Value("${mail.smtp.starttls.enable}")
    private String mailSmtpStartTlsEnable;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "plain", Charset.forName("UTF-8"))));
        converters.add(stringConverter);
        
        BufferedImageHttpMessageConverter bufferedImage = new BufferedImageHttpMessageConverter();
        bufferedImage.setDefaultContentType(MediaType.IMAGE_JPEG);
        converters.add(bufferedImage);
        
     
//        
//         <bean class="org.springframework.http.converter.FormHttpMessageConverter"/>
//                <bean class="org.springframework.http.converter.ByteArrayHttpMessageConverter"/>
//                <bean class="org.springframework.http.converter.xml.SourceHttpMessageConverter"/>
//                
//                <!-- Converter for images -->
//                <bean class="org.springframework.http.converter.BufferedImageHttpMessageConverter"/>
//                
//                <!-- This must come after our image converter -->
//                <bean class="org.springframework.http.converter.json.MappingJacksonHttpMessageConverter"/>
        
        ByteArrayHttpMessageConverter byteArray = new ByteArrayHttpMessageConverter();
        byteArray.setSupportedMediaTypes(Arrays.asList(MediaType.IMAGE_JPEG,MediaType.IMAGE_PNG,MediaType.APPLICATION_OCTET_STREAM,MediaType.MULTIPART_FORM_DATA));
        converters.add(byteArray);
    }
    
    
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver r = new CommonsMultipartResolver();
        r.setMaxUploadSize(1024000000);
        return r;
    }
    
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("128KB");
        factory.setMaxRequestSize("128KB");
        return factory.createMultipartConfig();
    }

    @Bean
    public TemplateResolver templateResolver() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setMessageSource(messageSource());
        Set<IDialect> dialects = new HashSet<>();
        dialects.add(new SpringSecurityDialect());
        dialects.add(new DataTablesDialect());
        templateEngine.setAdditionalDialects(dialects);
        return templateEngine;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setViewNames(new String[]{"thy/*"});
        return viewResolver;
    }

    @Bean
    public ViewResolver viewResolverJSP() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewNames(new String[]{"jsp/*"});
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("i18n.messages");
        return source;
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());

        return bean;
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("ru"));
        resolver.setCookieName("myLocaleCookie");
        resolver.setCookieMaxAge(4800);
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("mylocale");
        registry.addInterceptor(interceptor);
    }

    @Bean
    public HandlerExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        resolver.setDefaultErrorView("thy/error/exception");
        return resolver;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(10);
    }

    @Bean
    JavaMailSender javaMailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setPort(Integer.parseInt(mailSmtpPort));
        javaMailSender.setHost(mailSmtpHost);
        javaMailSender.setUsername(mailSmtpUsername);
        javaMailSender.setPassword(mailSmtpPassword);
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.auth", mailSmtpAuth);
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", mailSmtpStartTlsEnable);
        return javaMailSender;
    }

    @Bean
    MailForm mailForm() {
        MailForm mailBean = new MailForm();
        mailBean.setHost(mailHost);
        mailBean.setSubject(mailSubject);
        mailBean.setWebsiteName(mailWebsiteName);
        mailBean.setFrom(mailFrom);
        return mailBean;
    }

    @Bean
    VelocityEngineFactoryBean velocityEngine() {
        VelocityEngineFactoryBean velocityEngine = new VelocityEngineFactoryBean();
        velocityEngine.setResourceLoaderPath("resources/mail/template/");
        return velocityEngine;
    }
}
