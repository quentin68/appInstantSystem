package com.quentinbauer.parkingapp.parkingService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quentinbauer.parkingapp.parkingService.dto.Coordinate;
import com.quentinbauer.parkingapp.parkingService.dto.ParkingDto;
import com.quentinbauer.parkingapp.parkingService.services.ParkingService;

import io.swagger.v3.oas.annotations.Operation;



/**
 * @author Quentin Bauer
 */

@RestController
public class ParkingController {

	@Autowired
	private ParkingService parkingService;

	@CrossOrigin(origins = "http://localhost:8086")
	@GetMapping(value = "/parkings/{city}")
	@Operation(description="get list parking around the given coordinate in the given radius", operationId="city name")
	public ResponseEntity<List<ParkingDto>> getListParkingAroundPosition(Coordinate coordinateFrom,@PathVariable("city") String city, @RequestParam("radius")int radius) throws Exception {
		List<ParkingDto> parkingListDto;
		parkingListDto = parkingService.getListParkingAroundPosition(coordinateFrom,city, radius);
		return new ResponseEntity<>(parkingListDto,  HttpStatus.OK);		
	}
}

	