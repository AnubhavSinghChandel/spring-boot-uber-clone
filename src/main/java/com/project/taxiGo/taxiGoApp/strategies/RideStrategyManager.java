package com.project.taxiGo.taxiGoApp.strategies;

import com.project.taxiGo.taxiGoApp.strategies.implementations.driverMatching.DriverMatchingHighestRatedDriverStrategy;
import com.project.taxiGo.taxiGoApp.strategies.implementations.driverMatching.DriverMatchingNearestDriverStrategy;
import com.project.taxiGo.taxiGoApp.strategies.implementations.rideFare.RideFareDefaultFareCalculationStrategy;
import com.project.taxiGo.taxiGoApp.strategies.implementations.rideFare.RideFareSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareDefaultFareCalculationStrategy defaultRideFareCalculationStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double riderRating){

        if(riderRating>=4.8){
            return highestRatedDriverStrategy;
        }else {
            return nearestDriverStrategy;
        }
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy(){

        // 6PM to 9PM is surge time
        LocalTime surgeStartTime = LocalTime.of(18, 0);
        LocalTime surgeEndTime = LocalTime.of(21, 0);
        LocalTime currentTime = LocalTime.now();
        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);
        if(isSurgeTime){
            return surgePricingFareCalculationStrategy;
        }else {
            return defaultRideFareCalculationStrategy;
        }
    }
}
