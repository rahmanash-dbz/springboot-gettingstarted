package com.rahmanash.gettingstarted.apiservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class ApiServiceController {
	
	@Autowired
	private ApiService apiService;
	
	@RequestMapping("/service/{serviceId}")
	public Mono<String> validService(@PathVariable String serviceId) throws JSONException{
		JSONObject response = new JSONObject();
		response.put("serviceId", serviceId);
		response.put("Valid", apiService.isValidService(serviceId));
		return Mono.just(response.toString());
	}
	

}
