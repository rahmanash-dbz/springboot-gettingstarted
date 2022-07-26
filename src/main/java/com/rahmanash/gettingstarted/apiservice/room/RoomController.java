package com.rahmanash.gettingstarted.apiservice.room;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rahmanash.gettingstarted.apiservice.ApiService;
import com.rahmanash.gettingstarted.apiservice.ThreadExecutors.ThreadExecutorService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rooms")
public class RoomController {
	
	@Autowired
	private ApiService apiService;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private ThreadExecutorService execService;
	
	
	
	@GetMapping("/service/{serviceId}")
	public Mono<List<Room>> getAllRoomsOfService(@PathVariable String serviceId){
		Supplier querySuppler = ()->{return roomRepository.findByServiceIdAndDeletedTimeEquals(serviceId, -1L);};
		return Mono.fromCompletionStage( execService.getThread(querySuppler) );
	}
	
	@GetMapping("/list")
	public Mono<List<Room>>  getRooms(@RequestParam Optional<List<String>> ids){
		if(!ids.isEmpty()) {
			
			Flux<String> roomIds =  Flux.fromArray(ids.get().toArray( new String[ids.get().size()]));
			Flux<Room> rooms =  roomIds.flatMap( roomId -> {
				Mono<Room> roomData = Mono.fromCompletionStage( execService.getThread( () -> { return roomRepository.getById(roomId); }) );
				return roomData;
			});
			Mono<List<Room>>  roomsList = rooms.collectList();
			return roomsList;
		}
		return Mono.empty();
	}
	
	@PostMapping("/service/{serviceId}/room")
	public Mono<Room> createRoom(@PathVariable String serviceId,@RequestBody Room room){
		System.out.println("Room Model --"+room.toCustomString());
		if(apiService.isValidService(serviceId)) {
			return Mono.fromCompletionStage(execService.getThread( ()->{
				return roomRepository.save(room);
				})
			);
		}
		return Mono.error(new Throwable("InValid Service"));
	}

}
