package com.quentinbauer.parkingapp.parkingService.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quentinbauer.parkingapp.parkingService.dto.ParkingDto;
import com.quentinbauer.parkingapp.parkingService.services.ParkingDataProcess;
import com.quentinbauer.parkingapp.parkingService.utils.DataFormatter;
import com.quentinbauer.parkingapp.parkingService.utils.typeData.JsonData;
import com.quentinbauer.parkingapp.parkingService.utils.typeData.TypeData;

/**
 * @author Quentin Bauer
 * The data process implementation of Poitiers Parking
 */

@Service
public class ParkingPoitiersDataProcessImpl  implements ParkingDataProcess {

	Logger logger = LoggerFactory.getLogger(ParkingPoitiersDataProcessImpl.class);

	private static final String url = "https://data.grandpoitiers.fr/api/records/1.0/search/?dataset=mobilites-stationnement-des-parkings-en-temps-reel&facet=nom";

	@Autowired 
	DataFormatter<JsonData> dataFormatter;

	@Override
	public List<ParkingDto> getListParkingWithAvailibility(List<ParkingDto> listParkingDto)throws Exception {

		TypeData<JsonData> rootData= dataFormatter.getDataRoot(url);
		String data = rootData.getDataToString();
		listParkingDto.forEach(parkingDto -> {
			String pathExpression = "$..records[?(@.fields.nom=='"+ parkingDto.getName() +"')].fields.places";
			try {
				int availibility = dataFormatter.getAvailabilityParkingValueByPathExpression(pathExpression,data);
				parkingDto.setAvailability(availibility);   								
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		});
		return listParkingDto;	
	}

}

