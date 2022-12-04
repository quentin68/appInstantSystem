package com.quentinbauer.parkingapp.parkingService.utils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.JsonPath;
import com.quentinbauer.parkingapp.parkingService.utils.typeData.JsonData;

import net.minidev.json.JSONArray;

/**
 * @Author Quentin Bauer
 * The dataJsonFormatter implementation
 */

@Component
public class DataJsonFormatter implements DataFormatter<JsonData> {

	@Override
	public JsonData getDataRoot(String url) throws JSONException, IOException {
		
		String json = IOUtils.toString(new URL(url), Charset.forName("UTF-8"));	
		JSONObject datasets = new JSONObject(json);
		return new JsonData(datasets);
	}

	
	@Override
	public int getAvailabilityParkingValueByPathExpression(String pathExpression, String data) throws JSONException {
		JSONArray ja = JsonPath.parse(data).read(pathExpression);
		if(ja!=null)
			return (int) ja.get(0);
		return 0;
	}

}
