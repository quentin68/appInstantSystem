package com.quentinbauer.parkingapp.parkingService.utils;

import java.io.IOException;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Component;

import com.quentinbauer.parkingapp.parkingService.utils.typeData.TypeData;
import com.quentinbauer.parkingapp.parkingService.utils.typeData.XmlData;

/**
 * @Author Quentin Bauer
 * The dataXmlFormatter implementation
 */
@Component
public class DataXmlFormatter implements DataFormatter<TypeData<XmlData>>{

	@Override
	public XmlData getDataRoot(String url)  throws JSONException, IOException {
		//TO IMPLEMENT => Je ne l'ai pas fait pour le mini projet 
		return null;
	}

	@Override
	public int getAvailabilityParkingValueByPathExpression(String path, String data) {
		//TO IMPLEMENT => Je ne l'ai pas fait pour le mini projet 
		return 0;
	}

}
