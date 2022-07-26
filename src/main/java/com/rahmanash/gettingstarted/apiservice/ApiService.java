package com.rahmanash.gettingstarted.apiservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rahmanash.gettingstarted.apiservice.H2.H2;

@Component
public class ApiService {
	
	
	private String[] allowedServices;
	
	@Autowired
	private H2 h2;
	
	public ApiService(@Value("${apiservice.service.allowedServices}") final String allowedServicesList) {
		allowedServices = allowedServicesList.split(",");
	}
	
	public Boolean isValidService(String serviceId) {
		Boolean isValidService = false;
		System.out.println("allowedServices =="+allowedServices.length);
		for(String services:allowedServices) {
			if(services.equals(serviceId.toUpperCase())) {
				isValidService = true;
				break;
			}
		}
		return isValidService;
	}
	
	

}
