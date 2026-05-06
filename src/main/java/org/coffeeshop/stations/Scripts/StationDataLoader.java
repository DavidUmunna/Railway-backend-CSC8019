package org.coffeeshop.stations.scripts;

import org.coffeeshop.stations.models.Station;
import org.coffeeshop.stations.repositories.StationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component that loads initial station data into the database when the application starts.
 * This is useful for testing and development purposes to ensure there are stations available for the application to work with.
 * @author Umunna David
 * @since 2026-04-22
 * @version 1.0
 */
@Component
public class StationDataLoader implements CommandLineRunner {

    private static final String WEEKDAY_HOURS = "06:30-19:00";
    private static final String SATURDAY_HOURS = "07:00-18:00";
    private static final boolean CLOSED_ON_SUNDAY = true;
    
    private final StationRepository stationRepository;

    public StationDataLoader(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        /*if (stationRepository.count() == 0) {
            stationRepository.save(new Station("Cramlington", WEEKDAY_HOURS, SATURDAY_HOURS, CLOSED_ON_SUNDAY));
            stationRepository.save(new Station("North", WEEKDAY_HOURS, SATURDAY_HOURS, CLOSED_ON_SUNDAY));
            stationRepository.save(new Station("South", WEEKDAY_HOURS, SATURDAY_HOURS, CLOSED_ON_SUNDAY));
        }*/
        stationRepository.save(new Station("Cramlington", "08:00-18:00", "09:00-17:00", true));
    }
}
