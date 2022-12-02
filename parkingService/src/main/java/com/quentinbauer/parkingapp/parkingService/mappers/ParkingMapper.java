package com.quentinbauer.parkingapp.parkingService.mappers;


import org.mapstruct.Mapper;

import com.quentinbauer.parkingapp.parkingService.dto.ParkingDto;
import com.quentinbauer.parkingapp.parkingService.entity.Parking;

/**
 * @Author Quentin Bauer
 * Mapper of the parking entity with Dto
 */
@Mapper(componentModel = "spring")
public interface ParkingMapper {
	
	ParkingDto parkingToParkingDto(Parking parking);

}
