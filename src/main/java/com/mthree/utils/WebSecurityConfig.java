package com.mthree.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mthree.services.TraderDetailsService;
 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    /** 
     * @return ModelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
 
    
    /** 
     * @return UserDetailsService
     */
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new TraderDetailsService();
    }
     
    
    /** 
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    
    /** 
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    
    /** 
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
 
    
    /** 
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()        
            .antMatchers("/").permitAll() // welcome screen url
            // .anyRequest().authenticated()
            .and()
            .formLogin()
            //.loginProcessingUrl("/perform_login")
            .loginPage("/user/login")
            .defaultSuccessUrl("/user/homepage") // home screen url
            .permitAll()
            .and()
            .logout().permitAll();
    }
    
    
    /** 
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    
    /** 
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
}
