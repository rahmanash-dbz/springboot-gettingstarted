package com.rahmanash.gettingstarted.apiservice.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.assertj.core.api.Assertions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.rahmanash.gettingstarted.apiservice.ApiService;
import com.rahmanash.gettingstarted.apiservice.ThreadExecutors.ThreadExecutorService;

import reactor.core.publisher.Flux;

@WebFluxTest(RoomController.class)
public class RoomControllerTests {
	
	@Autowired
    private WebTestClient webClient;
	
	@MockBean
	private RoomRepository roomRepository;
	
	@MockBean
	private ApiService apiService;
	
	
	
	@TestConfiguration
	  public static class TestExecutorConfig {
		 @Bean()
		 public ExecutorService fixedThreadPool() {
			 return Executors.newFixedThreadPool(4);
		 }
	    
	 }
	
	
	@Autowired
	private ExecutorService executorService;
	
	@MockBean
	private ThreadExecutorService execService;
	
//	@Captor
//    private ArgumentCaptor<Supplier> registerMessageLambdaCaptor;
	
	
	Room createRoom(Long id,String serviceId) {
		Room room = new Room();
		room.setId(id);
		room.setCreatedTime(System.currentTimeMillis());
		room.setDeletedTime(-1L);
		room.setServiceId(serviceId);
		room.setStatus(Room.Status.CREATED);
		return room;
	}
	
	@Test
	@DisplayName(value="Get all rooms of a service by it's Id")
	void testGetRoom() {
		Room room = createRoom(1L,"DOLBY");
		List roomsList = new ArrayList();
		roomsList.add(room);
		Supplier fnc = ()->{return this.roomRepository.findByServiceIdAndDeletedTimeEquals("DOLBY", -1L);};
		
		when(this.roomRepository.findByServiceIdAndDeletedTimeEquals("DOLBY", -1L)).thenReturn(roomsList);
		when(this.execService.getThread(Mockito.any(Supplier.class))).thenReturn(CompletableFuture.supplyAsync(fnc, this.executorService));
		 
		webClient
         .get().uri("/rooms/service/DOLBY")
         .exchange()
         .expectStatus().isOk()
         .expectBodyList(Room.class).hasSize(1).consumeWith(rooms->{
         	assertThat(rooms.getResponseBody().get(0)).usingRecursiveComparison().isEqualTo(roomsList.get(0));
          });
	}
	
	@Test
	@DisplayName(value="Get all rooms by list of id's")
	void testGetRoomListByIds() {
		HashMap<Long,Room> roomsMap = new HashMap();
		
		Room room = createRoom(1L,"DOLBY");
		when(this.roomRepository.getById("1")).thenReturn(room);
		roomsMap.put(room.getId(), room);
		
		room = createRoom(2L,"GOOGLE");
		when(this.roomRepository.getById("2")).thenReturn(room);
		roomsMap.put(room.getId(), room);
		
		room = createRoom(3L,"BLUEJEANS");
		when(this.roomRepository.getById("3")).thenReturn(room);
		roomsMap.put(room.getId(), room);
		
		/*
		ThreadExecutorService mockThreadService = mock(ThreadExecutorService.class);
		Mockito.verify(mockThreadService).getThread(registerMessageLambdaCaptor.capture());
		Supplier givenFunction = registerMessageLambdaCaptor.getValue();
		
		when(mockThreadService.getThread(Mockito.any())).thenReturn(CompletableFuture.supplyAsync(givenFunction, this.executorService));
		*/
		
		Answer<CompletableFuture> answer = new Answer<CompletableFuture>() {
		    public CompletableFuture answer(InvocationOnMock invocation) throws Throwable {
		    	return CompletableFuture.supplyAsync((Supplier) invocation.getArguments()[0], executorService);
		    }
		};
		
		when(this.execService.getThread(Mockito.any(Supplier.class))).thenAnswer(answer);
		
		webClient
         .get().uri("/rooms/list?ids=1,2,3")
         .exchange()
         .expectStatus().isOk()
         .expectBodyList(Room.class).hasSize(3).consumeWith(rooms->{
        	rooms.getResponseBody().forEach(thisRoom->{
        		Room _room = roomsMap.get(thisRoom.getId());
        		if(_room != null ) {
        			assertThat(thisRoom).usingRecursiveComparison().isEqualTo(_room);
        		}else {
        			assertThat(true).isEqualTo(false);
        		}
        	});
         });
		
        
	}
	
	
	@Test
	@DisplayName(value="Create room by a Service")
	void testCreateRoom() {
		Room room = createRoom(1L,"DOLBY");

		Supplier fnc = ()->{return this.roomRepository.save(room);};
		
		when(this.apiService.isValidService("DOLBY")).thenReturn(true);
		when(this.roomRepository.save(room)).thenReturn(room);
		when(this.execService.getThread(Mockito.any(Supplier.class))).thenReturn(CompletableFuture.supplyAsync(fnc, this.executorService));
		 
		FluxExchangeResult<Room> fluxRoom = webClient
         .post().uri("/rooms/service/DOLBY/room")
         .body(BodyInserters.fromValue(room))
         .exchange()
         .expectStatus().isOk()
         .returnResult(Room.class);
		
		Room createdRoom = fluxRoom.getResponseBody().blockFirst();
		assertThat(createdRoom).usingRecursiveComparison().isEqualTo(room);
	}
	
	
	@Test
	@DisplayName(value="Deletion of rooms by a Service")
	void testDeleteRoom() {
		Room room = createRoom(1L,"DOLBY");

		Supplier fnc = ()->{return this.roomRepository.deleteRoomByServiceId("DOLBY");};
		
		when(this.roomRepository.deleteRoomByServiceId("DOLBY")).thenReturn(1);
		when(this.execService.getThread(Mockito.any(Supplier.class))).thenReturn(CompletableFuture.supplyAsync(fnc, this.executorService));
		 
		webClient
         .delete().uri("/rooms/service/DOLBY")
         .exchange()
         .expectStatus().isOk()
         .expectBody(Integer.class).isEqualTo(1);
	}
	
	

}
