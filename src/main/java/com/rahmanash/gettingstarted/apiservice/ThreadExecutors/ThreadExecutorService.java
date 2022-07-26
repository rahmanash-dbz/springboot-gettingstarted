package com.rahmanash.gettingstarted.apiservice.ThreadExecutors;

import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadExecutorService {
	
	@Autowired
	private ExecutorService executorService;
	
	public CompletableFuture getThread(Supplier fn){
		return CompletableFuture.supplyAsync(fn, executorService);
	}

}
