package com.cxy.config;

import com.cxy.entity.Permission;
import com.cxy.filter.CookieCheckFilter;
import com.cxy.handler.LoginFailureHandler;
import com.cxy.handler.LoginSuccessHandler;
import com.cxy.handler.MyAccessDeniedHandler;
import com.cxy.handler.RememberMeSuccessHandler;
import com.cxy.http.filter.SessionFilter;
import com.cxy.service.PermissionService;
import com.cxy.service.UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private RememberMeSuccessHandler rememberMeSuccessHandler;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Autowired
    private PermissionService permissionService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().anyRequest().permitAll();
//        http.authorizeRequests()
//                .anyRequest().authenticated();
        http.authorizeRequests().antMatchers("/user/signUp").permitAll();
        List<Permission> permissions = permissionService.getAllPermissions();
        permissions.forEach(e->{
            if("*".equals(e.getMethon())){
                try {
                    http.authorizeRequests().antMatchers(e.getPath()).hasAnyAuthority(e.getName());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }else{
                List<String> methodList = Arrays.asList(e.getMethon().split(","));
                for (String method : methodList) {
                    try {
                        http.authorizeRequests().antMatchers(HttpMethod.resolve(method), e.getPath()).hasAnyAuthority(e.getName());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        http.authorizeRequests().antMatchers("/test/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                //指定要拦截的登录url
                .loginProcessingUrl("/user/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .and()
                .addFilterBefore(new CookieCheckFilter(), AnonymousAuthenticationFilter.class)
//                .addFilterBefore(new SessionFilter(), WebAsyncManagerIntegrationFilter.class)
                //处理remenberMe成功后加入uid到cookie，默认只添加了sessionId
//                .rememberMe().authenticationSuccessHandler(rememberMeSuccessHandler).and()
                //处理未登录和没有权限的情况
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler).authenticationEntryPoint(unauthorizedEntryPoint).and()
                .httpBasic().and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable();


    }

    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.addExposedHeader("Connection");
        corsConfiguration.addExposedHeader("Set-Cookie");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)// 设置自定义的userDetailsService
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();// 使用不使用加密算法保持密码
//        return new BCryptPasswordEncoder();
    }
}
