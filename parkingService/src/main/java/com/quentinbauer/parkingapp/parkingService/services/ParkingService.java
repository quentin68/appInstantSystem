package com.quentinbauer.parkingapp.parkingService.services;

import java.util.List;

import com.quentinbauer.parkingapp.parkingService.Exception.ParkingBadRequestException;
import com.quentinbauer.parkingapp.parkingService.Exception.ParkingNotFoundException;
import com.quentinbauer.parkingapp.parkingService.dto.Coordinate;
import com.quentinbauer.parkingapp.parkingService.dto.ParkingDto;

/**
 * @Author Quentin Bauer
 * This is the parking service 
 */
public interface ParkingService {


	/**
	 * This method will be used to get List parking around the given coordinate.
	 * @param coordinateFrom the coordinate given
	 * @param city the city name of the parkings
	 * @radius radius the radius of the research
	 * @return List<ParkingDto> the list of parking around the coordinateFrom 
	 * @throws ParkingBadRequestException bad request exception
	 * @throws ParkingNotFoundException no parking found exception
	 */
	public List<ParkingDto> getListParkingAroundPosition(Coordinate coordinateFrom,String city, int radius) throws ParkingBadRequestException, ParkingNotFoundException;

}
