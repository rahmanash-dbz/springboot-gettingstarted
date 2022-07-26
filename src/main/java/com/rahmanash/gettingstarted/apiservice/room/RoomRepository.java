package com.rahmanash.gettingstarted.apiservice.room;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Object>{
	
	List<Room> findByServiceIdAndDeletedTimeEquals(String serviceId,Long delTime);
	
}
