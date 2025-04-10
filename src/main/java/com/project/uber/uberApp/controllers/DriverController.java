package com.project.uber.uberApp.controllers;

import com.project.uber.uberApp.dto.*;
import com.project.uber.uberApp.services.DriverService;
import jakarta.websocket.server.PathParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
@Secured("ROLE_DRIVER")
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<DriverRideDTO> acceptRideRequest(@PathVariable(name = "rideRequestId") Long rideRequest){
        return ResponseEntity.ok(driverService.acceptRide(rideRequest));
    }

    @PostMapping("/startRide/{rideRequestId}")
    public ResponseEntity<DriverRideDTO> startRide(@PathVariable Long rideRequestId, @RequestBody StartRideDTO startRideDTO){
        return ResponseEntity.ok(driverService.startRide(rideRequestId, startRideDTO.getOtp()));
    }

    @PostMapping("/endRide/{rideId}")
    public ResponseEntity<DriverRideDTO> endRide(@PathVariable Long rideId){
        return ResponseEntity.ok(driverService.endRide(rideId));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<DriverRideDTO> cancelRide(@PathVariable Long rideId){
        return ResponseEntity.ok(driverService.cancelRide(rideId));
    }

    @PostMapping("/rateRider")
    public ResponseEntity<RiderDTO> rateRider(@RequestBody RatingDTO rating){
        return ResponseEntity.ok(driverService.rateRider(rating.getRideId(), rating.getRating()));
    }

    @GetMapping("/rides")
    public ResponseEntity<Page<DriverRideDTO>>  getAllDriverRides(@RequestParam(defaultValue = "0") Integer pageOffSet, @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        return ResponseEntity.ok(driverService.getAllRides(PageRequest.of(pageOffSet, pageSize, Sort.by(Sort.Direction.DESC, "createdTime", "id"))));
    }

    @GetMapping("/profile")
    public ResponseEntity<DriverDTO> getDriverProfile(){
        return ResponseEntity.ok(driverService.getMyProfile());
    }
}
