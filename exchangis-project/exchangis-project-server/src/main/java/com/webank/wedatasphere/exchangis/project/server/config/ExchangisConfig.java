package com.webank.wedatasphere.exchangis.project.server.config;

import com.webank.wedatasphere.dss.standard.app.sso.plugin.filter.UserInterceptor;
import com.webank.wedatasphere.exchangis.project.server.filter.DSSOriginSSOFilter;
import com.webank.wedatasphere.exchangis.project.server.interceptor.WTSSHttpRequestUserInterceptor;
import com.webank.wedatasphere.exchangis.project.server.utils.TokenUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangisConfig {

    @Bean
    public FilterRegistrationBean registerFilter(DSSOriginSSOFilter dssOriginSSOFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(dssOriginSSOFilter);
        registration.addUrlPatterns("/*");
        registration.setName("DSSOriginSSOFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public DSSOriginSSOFilter createDSSOriginSSOFilter(){
        return new DSSOriginSSOFilter();
    }

    @Bean
    public UserInterceptor getUserInterceptor(TokenUtils tokenUtils) {
        return new WTSSHttpRequestUserInterceptor(tokenUtils);
    }

}
