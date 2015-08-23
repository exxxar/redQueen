package com.web.mavenproject6.config;

import com.web.mavenproject6.service.MyUserDetailsService;
import com.web.mavenproject6.utility.UserTypeEnum;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    private MyUserDetailsService detailsService;    
 
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(detailsService)
                .and()
                .inMemoryAuthentication()
                .withUser("SYSTEMADMIN").password("SPASSWORD").roles(
                        UserTypeEnum.SECURE.toString(),
                        UserTypeEnum.ADMIN.toString(),
                        UserTypeEnum.USER.toString()
                );

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login/**", "/public/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/camera/**").hasRole(UserTypeEnum.SECURE.toString())
                .antMatchers("/admin/**").hasRole(UserTypeEnum.ADMIN.toString())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .failureUrl("/login/error")
                .and()
                .logout().logoutUrl("/j_spring_security_logout")
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling().accessDeniedPage("/403");

//                .usernameParameter("username").passwordParameter("password")
//            .and()
//                .logout().logoutSuccessUrl("/login?logout")
//            .and()
//                .exceptionHandling().accessDeniedPage("/403")
//            .and()
//                .csrf();
    }

}
