package com.rahmanash.gettingstarted.apiservice.room.roomstats;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.concurrent.Queues;

@Component
public class RoomStatsProducerService {
	
	private Sinks.Many<RoomStats> roomStatsSink;
	
	private Flux<RoomStats> roomStatsFlux;
	
	public  RoomStatsProducerService() {
		//A sink which can emit elements to Multiple Subscribers attached
		this.roomStatsSink = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
		
		//Create a new thread to process this emitted room stats
		//so as to not block the calling thread
		Scheduler s = Schedulers.newSingle("thread-handle-roomstats");
		this.roomStatsFlux =  this.roomStatsSink.asFlux().publishOn(s);
		
		//Start the Consumer Service 
		RoomStatsConsumerService consumerService = new RoomStatsConsumerService(this);
	}
	
	
	public  Flux<RoomStats> getStatsFlux() {
		return this.roomStatsFlux;
	}
	
	public boolean sendStat(RoomStats stat) {
		this.roomStatsSink.emitNext(stat,Sinks.EmitFailureHandler.FAIL_FAST);
		return true;
	}
	
}
