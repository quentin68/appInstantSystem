package com.quentinbauer.parkingapp.parkingService.Exception;

public class ParkingNotFoundException extends Exception{

	/**
	 * @Author Quentin Bauer
	 * Exception class for parking not found
	 */
	private static final long serialVersionUID = 1L;
	

	public ParkingNotFoundException(String message) {
		super(message);
	}

}
