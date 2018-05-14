package dk.cngroup.trainings.spring.springassignment.config;

import dk.cngroup.trainings.spring.springassignment.service.user.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private UserService userService;

	public WebSecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userService = userService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/web/**")
				.authenticated()
				.and()
				.formLogin()
				.loginPage("/web/login")
				.permitAll()
				.and()
				.logout()
				.logoutUrl("/web/logout")
				.permitAll();

		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/api/**").and().ignoring().antMatchers("/signup");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.
				userDetailsService(userService)
				.passwordEncoder(bCryptPasswordEncoder);
	}
}