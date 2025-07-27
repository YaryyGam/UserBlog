package com.yaryy.api_gateway;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class LoadBalancerConfig {

    @Bean
    ReactorLoadBalancer<ServiceInstance> customLoadBalancer(
            ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
            LoadBalancerClientFactory loadBalancerClientFactory) {

        String serviceId = "quiz-service"; // укажи конкретный сервис
        return new LoadBalancerModification(serviceInstanceListSupplierProvider, serviceId);
    }
}
