package com.quentinbauer.parkingapp.parkingService.utils.typeData;

import org.springframework.boot.configurationprocessor.json.JSONObject;

/**
 * @Author Quentin Bauer
 * The Json Data class
 */
public class JsonData extends TypeData<JsonData> {
	
	JSONObject jsonObject;
	
	public JsonData(JSONObject jsonObject)
	{
		this.jsonObject = jsonObject;
	}


	@Override
	public JsonData getData() {
		return new JsonData(this.jsonObject);
	}


	@Override
	public String getDataToString() {
		
		return this.jsonObject.toString();
				
	}


	public JSONObject getJsonObject() {
		return jsonObject;
	}


	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	



}
