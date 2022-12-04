package com.quentinbauer.parkingapp.parkingService.dto;

/**
 * @author Quentin Bauer
 * Coordinate class with longitude and latitude
 */

public class Coordinate {

	private double longitude;
	private double latitude;

	public Coordinate(double longitude, double latitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


}
