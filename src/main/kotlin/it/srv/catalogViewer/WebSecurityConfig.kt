package it.srv.catalogViewer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private val dataSource: DataSource? = null

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication().dataSource(dataSource)
            .usersByUsernameQuery("select username, password, enabled from users where username = ?")
            .authoritiesByUsernameQuery("select username, authority from authorities where username = ?")
            .passwordEncoder(argon2PasswordEncoder())
    }

    @Autowired
    fun argon2PasswordEncoder(): Argon2PasswordEncoder {
        return Argon2PasswordEncoder(16, 64, 8, 1 shl 13, 16)
    }

    @Throws(java.lang.Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers(
                "/",
                "/home",
                "/register",
                "/js/**",
                "/stylesheets/**",
                "/logo.png",
                "/generalHelp",
                "/authentication",
                "/api/**",
                "/favicon.ico"
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginProcessingUrl("/login")
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
            .and()
            .sessionManagement()
            .invalidSessionUrl("/error?message=\"Session timeout, login again.\"")
            .maximumSessions(3)
            .sessionRegistry(sessionRegistry())
            .and()
            .sessionFixation()
            .none()
            .and()
            .logout()
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        http.headers()
            .and()
            .cors()
        http.csrf().ignoringAntMatchers("/api/**")
            .and()
            .rememberMe()
            .alwaysRemember(true)
            .tokenValiditySeconds(3600 * 24 * 100)
            .and()
            .httpBasic()
            .disable()
    }

    @Bean
    fun sessionRegistry(): SessionRegistry? {
        return SessionRegistryImpl()
    }
}