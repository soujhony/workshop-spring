package com.jhonystein.pedidex.security;

import com.jhonystein.pedidex.service.UsuarioService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private JwtTokenService jwtTokenService;

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoderBean());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/login", "/register")
                    .permitAll()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated();
        http.addFilterBefore(
            new JwtAuthorizationTokenFilter(jwtTokenService, usuarioService),
                UsernamePasswordAuthenticationFilter.class
        );

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers(HttpMethod.POST, "/login", "/register")
            .and()
            .ignoring()
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/**/*.html",
                        "/**/*.js",
                        "/**/*.css");
    }
    
    public static class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

        private final JwtTokenService jwtTokenService;
        private final UserDetailsService userDetailsService;

        public JwtAuthorizationTokenFilter(JwtTokenService jwtTokenService, UserDetailsService userDetailsService) {
            this.jwtTokenService = jwtTokenService;
            this.userDetailsService = userDetailsService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
            String requestHeader = request.getHeader("Authorization");

            String username = null;
            if (requestHeader != null 
                    && requestHeader.startsWith("Bearer ")) {
                String authToken = requestHeader.substring(7);
                username = jwtTokenService.getUsernameFromToken(authToken);
            }

            if (username != null 
                    && SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {
                UserDetails usuario = this.userDetailsService.loadUserByUsername(username);
                response.setHeader("X-Token", jwtTokenService.generateToken(usuario));

                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            chain.doFilter(request, response);
        }
        
    }

}
