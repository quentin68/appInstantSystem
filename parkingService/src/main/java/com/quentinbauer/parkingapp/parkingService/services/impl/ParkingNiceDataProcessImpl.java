package com.quentinbauer.parkingapp.parkingService.services.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.quentinbauer.parkingapp.parkingService.dto.ParkingDto;
import com.quentinbauer.parkingapp.parkingService.services.ParkingDataProcess;

/**
 * @author Quentin Bauer
 * The data process implementation of Nice Parking
 */

@Component
public class ParkingNiceDataProcessImpl  implements ParkingDataProcess {

	// TODO : IMPLEMENT
	
	/*private static final String url = "OTHER_URL_TO_NICE";

	@Autowired 
	DataFormatter<XmlData> dataFormatter;
	 */


	@Override
	public List<ParkingDto> getListParkingWithAvailibility(List<ParkingDto> listparkingDto) throws Exception {
		return null;
	}



}

