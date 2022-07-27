package com.rahmanash.gettingstarted.apiservice.room;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
@Transactional
public interface RoomRepository extends JpaRepository<Room, Object>{
	
	List<Room> findByServiceIdAndDeletedTimeEquals(String serviceId,Long delTime);
	
	
	@Modifying
	@Query("delete from Room where serviceId = :serviceId")
	Integer deleteRoomByServiceId( @Param("serviceId") String serviceId);
	
}
