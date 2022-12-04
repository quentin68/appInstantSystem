package com.quentinbauer.parkingapp.parkingService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.quentinbauer.parkingapp.parkingService.Exception.ParkingBadRequestException;
import com.quentinbauer.parkingapp.parkingService.Exception.ParkingNotFoundException;
import com.quentinbauer.parkingapp.parkingService.dto.Coordinate;
import com.quentinbauer.parkingapp.parkingService.dto.ParkingDto;
import com.quentinbauer.parkingapp.parkingService.services.ParkingService;


@SpringBootTest
class ParkingServiceApplicationTests {


	@Autowired
	private ParkingService parkingService;


	@Test
	void contextLoads() {
	}



	@Test
	public void testGetListParkingAroundPositionBadRequestExceptionCoordinate() throws Exception {

		double longitude = 0;
		double latitude = 0; 
		Coordinate coordinate = new Coordinate(longitude,latitude);
		String city = "Poitier";
		int radius = 50;

		String expectedMessage ="Les coordonnées ne sont pas correctes";

		try
		{
			parkingService.getListParkingAroundPosition(coordinate, city, radius);
		}
		catch(ParkingBadRequestException e)
		{
			assertThat(e).hasNoCause()
			.hasMessage(expectedMessage);
		}
	}

	@Test
	public void testGetListParkingAroundPositionBadRequestExceptionRadius() throws Exception {

		double longitude = 0.347449305008423;
		double latitude = 46.30504044806181; 
		Coordinate coordinate = new Coordinate(longitude,latitude);
		String city = "Poitier";
		int radius = -5;

		String expectedMessage ="Le rayon de recherche doit être strictement supérieur à 0";

		try
		{
			parkingService.getListParkingAroundPosition(coordinate, city, radius);
		}
		catch(ParkingBadRequestException e)
		{
			assertThat(e).hasNoCause()
			.hasMessage(expectedMessage);
		}
	}

	@Test
	public void testGetListParkingAroundPositionBadRequestExceptionCity() throws Exception {

		double longitude = 0.347449305008423;
		double latitude = 46.30504044806181; 
		Coordinate coordinate = new Coordinate(longitude,latitude);
		String city = "";
		int radius = 50;

		String expectedMessage ="Une ville est nécessaire pour la recherche";

		try
		{
			parkingService.getListParkingAroundPosition(coordinate, city, radius);
		}
		catch(ParkingBadRequestException e)
		{
			assertThat(e).hasNoCause()
			.hasMessage(expectedMessage);
		}
	}

	@Test
	public void testGetListParkingAroundPositionNotFoundException() throws Exception {

		double longitude = 0.347449305008423;
		double latitude = 46.30504044806181; 
		Coordinate coordinate = new Coordinate(longitude,latitude);
		String city = "Nice";
		int radius = 50;

		String expectedMessage ="Aucun parking trouvé";

		try
		{
			parkingService.getListParkingAroundPosition(coordinate, city, radius);
		}
		catch(ParkingNotFoundException e)
		{
			assertThat(e).hasNoCause()
			.hasMessage(expectedMessage);
		}
	}

	@Test
	public void testGetListParkingAroundPositionSuccess() throws Exception {

		double longitude = 0.347449305008423;
		double latitude = 46.30504044806181; 
		Coordinate coordinate = new Coordinate(longitude,latitude);
		String city = "Poitiers";
		int radius = 50;

		List<ParkingDto> result = parkingService.getListParkingAroundPosition(coordinate, city, radius);
		assertEquals(result.get(0).getAvailability(),-99);
		assertEquals(result.size(), 1);
	}


}
