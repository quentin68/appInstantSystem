package com.quentinbauer.parkingapp.parkingService.services;

import java.util.List;

import com.quentinbauer.parkingapp.parkingService.dto.ParkingDto;

/**
 * @Author Quentin Bauer
 * This is the parking data process. An implementation by city
 */

public interface ParkingDataProcess {

	/**
	 * This method will be used to get list parking with parking availibility data
	 * @param listparkingDto initial parking list before add parking availablity data
	 * @return List<ParkingDto> the list of parking with availibility data 

	 */
	List<ParkingDto> getListParkingWithAvailibility(List<ParkingDto> listparkingDto) throws Exception;


}
