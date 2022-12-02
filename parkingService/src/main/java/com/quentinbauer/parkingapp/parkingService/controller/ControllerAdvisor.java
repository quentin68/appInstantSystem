package com.quentinbauer.parkingapp.parkingService.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.quentinbauer.parkingapp.parkingService.Exception.ParkingBadRequestException;
import com.quentinbauer.parkingapp.parkingService.Exception.ParkingNotFoundException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ParkingBadRequestException.class)
	public ResponseEntity<Object> handleParkingBadRequestException(
			ParkingBadRequestException ex, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ParkingNotFoundException.class)
	public ResponseEntity<Object> handleParkingNotFoundException(
			ParkingBadRequestException ex, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> genericExceptionHandler(HttpServletRequest request, Exception exception) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "Problème technique");
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}