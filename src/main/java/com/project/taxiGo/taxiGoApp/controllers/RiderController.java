package com.project.taxiGo.taxiGoApp.controllers;

import com.project.taxiGo.taxiGoApp.dto.*;
import com.project.taxiGo.taxiGoApp.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider")
@RequiredArgsConstructor
@Secured("ROLE_RIDER")
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/requestRide")
    public RideRequestDTO requestRide(@RequestBody RideRequestDTO rideRequest){
       return riderService.requestRide(rideRequest);
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RiderRideDTO> cancelRide(@PathVariable Long rideId){
        return ResponseEntity.ok(riderService.cancelRide(rideId));
    }

    @PostMapping("/rateDriver")
    public ResponseEntity<DriverDTO> rateDriver(@RequestBody RatingDTO driverRating){
        return ResponseEntity.ok(riderService.rateDriver(driverRating.getRideId(), driverRating.getRating()));
    }

    @GetMapping("/profile")
    public ResponseEntity<RiderDTO> getRiderProfile(){
        return ResponseEntity.ok(riderService.getMyProfile());
    }

    @GetMapping("/rides")
    public ResponseEntity<Page<RiderRideDTO>> getAllRiderRides(@RequestParam(defaultValue = "0") Integer pageOffset, @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        return ResponseEntity.ok(riderService.getAllRides(PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "createdTime", "id"))));
    }
}
