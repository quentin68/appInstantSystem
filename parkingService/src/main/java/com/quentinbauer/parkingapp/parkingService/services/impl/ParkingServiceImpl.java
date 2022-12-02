package com.quentinbauer.parkingapp.parkingService.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quentinbauer.parkingapp.parkingService.Exception.ParkingBadRequestException;
import com.quentinbauer.parkingapp.parkingService.Exception.ParkingNotFoundException;
import com.quentinbauer.parkingapp.parkingService.dto.Coordinate;
import com.quentinbauer.parkingapp.parkingService.dto.ParkingDto;
import com.quentinbauer.parkingapp.parkingService.entity.Parking;
import com.quentinbauer.parkingapp.parkingService.mappers.ParkingMapper;
import com.quentinbauer.parkingapp.parkingService.repository.ParkingRepository;
import com.quentinbauer.parkingapp.parkingService.services.ParkingService;

@Service
public class ParkingServiceImpl implements ParkingService {

	@Autowired
	private ParkingRepository parkingRepository;
	
	@Autowired
	private ParkingMapper parkingMapper;
	
	
	@Transactional (readOnly =true)
	@Override
	public List<ParkingDto> getListParkingAroundPosition(Coordinate coordinateFrom,String city,int radius) throws ParkingBadRequestException, ParkingNotFoundException {
			if(coordinateFrom!=null && coordinateFrom.getLatitude()> 0 && coordinateFrom.getLongitude() > 0)
			{
				if(StringUtils.isEmpty(city))
				{
					throw new ParkingBadRequestException("Une ville est nécessaire pour la recherche");
				}
				if (radius <=0)
				{
					throw new ParkingBadRequestException("Le rayon de recherche doit être strictement supérieur à 0");
				}
				List<Parking> listParking = parkingRepository.findByCityName(city);
				List<ParkingDto> parkingDtoList = new ArrayList<>();
				if(!listParking.isEmpty())
				{
						listParking.forEach(parking -> {
							double distanceToParking= this.distanceToParking(parking,coordinateFrom);
						if(distanceToParking<= radius)
						{
							ParkingDto parkingDto = parkingMapper.parkingToParkingDto(parking);
							parkingDto.setDistance(distanceToParking);
							parkingDtoList.add(parkingDto);
						}
					});
				}
				else
				{
					throw new ParkingNotFoundException("Aucun parking trouvé");
				}
				return parkingDtoList;
			}
			
			else
			{
				throw new ParkingBadRequestException("Les coordonnées ne sont pas correctes");
			}			
	}


	/*
	 * Distance between given coordinate and the parking
	 */
	private double distanceToParking(Parking parking, Coordinate coordinateFrom) {
		
		double latitudeFrom= Math.toRadians(coordinateFrom.getLatitude());
		double longitudeFrom= Math.toRadians(coordinateFrom.getLongitude());
		double latitudeTo= Math.toRadians(parking.getLatitude());
		double longitudeTo= Math.toRadians(parking.getLongitude());
		int earthRadius = 6378137;
		return earthRadius*Math.acos(Math.sin(latitudeTo)*Math.sin(latitudeFrom)+Math.cos(latitudeTo)*Math.cos(latitudeFrom)*Math.cos(longitudeTo-longitudeFrom));

	}
}




