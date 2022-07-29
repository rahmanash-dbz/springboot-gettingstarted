package com.rahmanash.gettingstarted.apiservice.room.roomstats;

import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Flux;


public class RoomStatsConsumerService {
	
	
	private RoomStatsProducerService producerService;
	
	public RoomStatsConsumerService(RoomStatsProducerService producerService) {
		this.producerService = producerService;
		ConsumeStatsAndSendToDataBase();
		ConumseStatsAndSendToQueuingService();
		
	}

	private void ConumseStatsAndSendToQueuingService() {
			Flux<RoomStats> queuingServiceFlux = this.producerService.getStatsFlux();
			queuingServiceFlux.subscribe(stat->{
				//Assume we have handlers and producers of Queuing Service
				//and send this stat to that Service
				if(stat.getNewRoom()) {
					System.out.println("new room added and info sent to Queuing");
				}else {
					System.out.println("a room deleted and info sent to Queuing");
				}
				
			});
	}

	private void ConsumeStatsAndSendToDataBase() {
		Flux<RoomStats> databaseServiceFlux = this.producerService.getStatsFlux();
		databaseServiceFlux.subscribe(stat->{
			//Assume we have handlers and producers of Queuing Service
			//and send this stat to that Service
			if(stat.getNewRoom()) {
				System.out.println("new room added and info stored in database");
			}else {
				System.out.println("a room deleted and info updated in database");
			}
			
		});
	}
	
	
	
}
