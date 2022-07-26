package com.rahmanash.gettingstarted.apiservice.room;

import java.io.Serializable;

import org.hibernate.annotations.Proxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name="room", 
	    indexes = {
	       @Index(name = "ROOM_INDX_0", columnList = "service_id") })
@Proxy(lazy = false)
public class Room implements Serializable  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long _id) {
		id = _id;
	}
	
	@Column(name="room_info")
	private String roomInfo;
	
	public String getRoomInfo() {
		return roomInfo;
	}
	
	public void setRoomInfo(String _roomInfo) {
		roomInfo = _roomInfo;
	}
	
	
	@Column(name="created_time",nullable=false)
	private Long createdTime;
	
	public Long getCreatedTime() {
		return createdTime;
	}
	
	public void setCreatedTime(Long _createdTime) {
		createdTime = _createdTime;
    }
	
	@Column(name="deleted_time")
	private Long deletedTime = -1L;
	
	public Long getDeletedTime() {
		return deletedTime;
	}
	
	public void setDeletedTime(Long _deletedTime) {
		deletedTime = _deletedTime;
    }
	
	public enum Status{
        CREATED(1),
        ACTIVE(2),
        INACTIVE(3),
        LOCKED(4),
        DESTROYED(5),
        DISABLED(6),
        ENABLED(7);

        private Integer value;

        Status(Integer value){
            this.value = value;
        }

        
        public Integer getStatus(){
            return value;
        }
        
        public static Status convertToEnum(int id) {
        	Status status = null;
            for (Status item : Status.values()) {
                if (item.getStatus()==id) {
                	status = item;
                    break;
                }
            }
            return status;
        }
        
    }
	
	@Column(name="status")
	private int status = 1;
	
	public Status getStatus () {
        return Status.convertToEnum(this.status);
    }

    public void setRight(Status _status) {
        this.status = _status.getStatus();
    }
    
	
    @Column(name="service_id",nullable = false)
	private String serviceId;
    
    public String getServiceId() {
		return serviceId;
	}
    
    public void setServiceId(String _serviceId) {
    	serviceId = _serviceId;
    }
    
    public String toCustomString() {
    	return "Model ||  Room Id : "+id+"createdTime:"+createdTime+"status:"+status+"ServiceId:"+serviceId;
    }

}
