package com.rahmanash.gettingstarted.apiservice.room.roomstats;

public class RoomStats {

	private boolean newRoom;
	
	private boolean roomDeleted;
	
	public RoomStats(boolean newRoom,boolean roomDeleted) {
		this.newRoom = newRoom;
		this.roomDeleted = roomDeleted;
	}
	
	public void setNewRoom(boolean state) {
		this.newRoom = state;
	}
	
	public void setRoomDeleted(boolean state) {
		this.roomDeleted = state;
	}
	
	public boolean getNewRoom() {
		return this.newRoom;
	}
	
	public boolean getRoomDeleted() {
		return this.roomDeleted;
	}
	
}
