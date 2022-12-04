package com.quentinbauer.parkingapp.parkingService.repository;

import com.quentinbauer.parkingapp.parkingService.entity.Parking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Quentin Bauer
 * Parking repository
 */
@Repository
public interface ParkingRepository extends JpaRepository<Parking,Integer>
{
	List<Parking> findByCityName(String city);
}
