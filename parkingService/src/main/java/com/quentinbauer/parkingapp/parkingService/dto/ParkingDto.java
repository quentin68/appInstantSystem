package com.quentinbauer.parkingapp.parkingService.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ParkingDto {

	@JsonProperty("id")
	private int id;

	@NotNull
	@JsonProperty("name")
	private String name;

	@NotNull
	@JsonProperty("longitude")
	private double longitude;

	@NotNull
	@JsonProperty("latitude")
	private double latitude;

	@NotNull
	@JsonProperty("nbPlaceTotal")
	private int nbPlaceTotal;	

	@JsonProperty("distance")
	private double distance;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getNbPlaceTotal() {
		return nbPlaceTotal;
	}

	public void setNbPlaceTotal(int nbPlaceTotal) {
		this.nbPlaceTotal = nbPlaceTotal;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}	


}
