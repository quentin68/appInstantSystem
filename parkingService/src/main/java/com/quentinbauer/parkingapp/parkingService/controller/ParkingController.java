package com.quentinbauer.parkingapp.parkingService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quentinbauer.parkingapp.parkingService.dto.Coordinate;
import com.quentinbauer.parkingapp.parkingService.dto.ParkingDto;
import com.quentinbauer.parkingapp.parkingService.services.ParkingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;



/**
 * @author Quentin Bauer
 * Parking controller class
 */

@RestController
public class ParkingController {

	@Autowired
	private ParkingService parkingService;

	@CrossOrigin(origins = "http://localhost:8086")
	@GetMapping(value = "/parkings/{city}")
	@Operation(description="get list parking around the given coordinate in the given radius", operationId="city name")
	public ResponseEntity<List<ParkingDto>> getListParkingAroundPosition(@Parameter(description = "Coordinate from", example="{\"longitude\": 0.3474493,\"latitude\": 46.305042}") Coordinate coordinateFrom,@Parameter(description = "city name to search parkings", example="Poitiers") @PathVariable("city") String city, @Parameter(description = "radius in metter of the research of parkings", example="50") @RequestParam("radius")int radius) throws Exception {
		List<ParkingDto> parkingListDto = parkingService.getListParkingAroundPosition(coordinateFrom,city, radius);
		return new ResponseEntity<>(parkingListDto,  HttpStatus.OK);		
	}
}

	