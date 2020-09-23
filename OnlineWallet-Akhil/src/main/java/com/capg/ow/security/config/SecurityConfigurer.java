package com.capg.ow.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.capg.ow.security.filter.JwtFilter;
import com.capg.ow.security.service.CustomUserDetailsService;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	 @Autowired
	    private CustomUserDetailsService userDetailsService;
	 
	 @Autowired
	    private JwtFilter jwtFilter;
	 
	 @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService)
	        .passwordEncoder(passwordEncoder());
	    }
//	 @Override
//	 protected void configure(HttpSecurity http) throws Exception{
//		 http.authorizeRequests()
//		 .antMatchers("/**").hasRole("USER")
//		 .antMatchers(/)
//	 }

	@Bean
	public JwtFilter jwtAuthenticationFilter() {
		return new JwtFilter();
	}
	
	@Bean
    public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
    }
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().disable();
		http.csrf().disable().authorizeRequests()
		.antMatchers("/signin","/onlinewallet/createwallet","/signup")
		.permitAll().antMatchers(HttpMethod.OPTIONS,"/**")
        .permitAll().anyRequest().authenticated()
        .and().exceptionHandling().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);;

	}
}