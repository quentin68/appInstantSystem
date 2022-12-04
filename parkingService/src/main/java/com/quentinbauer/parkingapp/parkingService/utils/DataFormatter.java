package com.quentinbauer.parkingapp.parkingService.utils;

import com.quentinbauer.parkingapp.parkingService.utils.typeData.TypeData;

/**
 * @Author Quentin Bauer
 * This is the parking data formatter. No matter the data format from source
 */

public interface DataFormatter<T extends TypeData<?>> {
	
public abstract T getDataRoot(String url) throws Exception;
public abstract int getAvailabilityParkingValueByPathExpression(String pathExpression, String data) throws Exception;

}
