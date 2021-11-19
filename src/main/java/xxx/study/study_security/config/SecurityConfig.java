package xxx.study.study_security.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author: V
 * @param:
 * @description:
 */
@EnableWebSecurity //应用security
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    授权configure方法
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3")
        ;
        http.formLogin()
                .usernameParameter("ugh")
                .passwordParameter("upwd")
                .loginPage("/toLogin")
                .loginProcessingUrl("/login")
                .successForwardUrl("/");
        http.csrf().disable();//关闭csrf功能:跨站请求伪造,默认只能通过post方式提交logout请求
        http.logout().logoutSuccessUrl("/");
        http.rememberMe().rememberMeParameter("remember");
    }
//    认证configure

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //在内存中获取认证数据   /jdbcAuthentication是去jdbc中获取
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
//                必须给密码加密，不然报错500      .passwordEncoder(new BCryptPasswordEncoder())
//                  new BCryptPasswordEncoder().encode("123")
                .withUser("xxx").password(new BCryptPasswordEncoder().encode("123")).roles("vip2","vip3")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123")).roles("vip1","vip2","vip3")
                .and()
                .withUser("guest").password(new BCryptPasswordEncoder().encode("123")).roles("vip1")
        ;
    }
}
