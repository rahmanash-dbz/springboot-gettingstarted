package com.rahmanash.gettingstarted.apiservice.room;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("repotest")
public class RoomRepositoryTests {
	
	@Autowired
	private RoomRepository roomRepository;
	
	Room createRoom(Long id,String serviceId) {
		Room room = new Room();
		if(id!=null) {
			room.setId(id);
		}
		room.setCreatedTime(System.currentTimeMillis());
		room.setDeletedTime(-1L);
		room.setServiceId(serviceId);
		room.setStatus(Room.Status.CREATED);
		return room;
	}
	
	@BeforeEach
    public void setup() {
       Room room = createRoom(1L,"META");
       roomRepository.save(room);
       
       room = createRoom(2L,"GOOGLE");
       roomRepository.save(room);
       
       room = createRoom(3L,"BLUEJEANS");
       roomRepository.save(room);
       
       room = createRoom(4L,"DOLBY");
       roomRepository.save(room);
    }
	
	
    @Test
    public void createRoom() {
        Room room = createRoom(null,"DOLBY");
        Room createdRoom = roomRepository.save(room);
        assertThat(createdRoom).usingRecursiveComparison().ignoringFields("id").isEqualTo(room);
    }
    
    @Test
    public void getRoomByRoomId() {
    	Room room = roomRepository.getReferenceById(1L);
    	assertThat(room.getId()).isEqualTo(1L);
    }
    
    @Test
    public void getRoomsByServiceId() {
    	List<Room> rooms = roomRepository.findByServiceIdAndDeletedTimeEquals("DOLBY",-1L);
    	assertThat(rooms).size().isEqualTo(1);
    }

}
