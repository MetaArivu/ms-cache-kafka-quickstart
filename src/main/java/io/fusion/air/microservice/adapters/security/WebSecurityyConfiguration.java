/**
 * (C) Copyright 2022 Araf Karsh Hamid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fusion.air.microservice.adapters.security;

import io.fusion.air.microservice.server.config.ServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@EnableWebSecurity
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ServiceConfiguration serviceConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Forces All Request to be Secured
        // http.requiresChannel().anyRequest().requiresSecure();
        String apiPath = serviceConfig.getApiDocPath();
        http.authorizeRequests().antMatchers(apiPath + "/**").permitAll().and().exceptionHandling();
        // Enable CRPF Protection
        // http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        // Disable for Local Testing
        http.csrf().disable();
        String hostName = serviceConfig.getServerHost();
        // Content Security Policy
        http.headers()
                .contentSecurityPolicy("script-src 'self' *."+hostName+"; " +
                        "object-src *."+hostName+"; ");

    }
}

