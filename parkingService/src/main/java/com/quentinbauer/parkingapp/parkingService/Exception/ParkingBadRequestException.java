package com.quentinbauer.parkingapp.parkingService.Exception;

public class ParkingBadRequestException extends Exception{

	/**
	 * @Author Quentin Bauer
	 * Exception class for parking bad request
	 */
	private static final long serialVersionUID = 1L;


	public ParkingBadRequestException(String message) {
		super(message);
	}

}
