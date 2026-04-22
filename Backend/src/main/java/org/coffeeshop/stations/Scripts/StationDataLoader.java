package org.coffeeshop.stations.Scripts;

import org.coffeeshop.stations.models.Station;
import org.coffeeshop.stations.repositories.StationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component that loads initial station data into the database when the application starts.
 * This is useful for testing and development purposes to ensure there are stations available for the application to work with.
 * @author Umunna David
 * @since 22/04/2026
 * @version 1.0
 */
@Component
public class StationDataLoader implements CommandLineRunner {
    
    private final StationRepository stationRepository;

    public StationDataLoader(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (stationRepository.count() == 0) {
            stationRepository.save(new Station("Central", "7am-7pm", "8am-6pm", true));
            stationRepository.save(new Station("North", "6am-8pm", "7am-7pm", false));
            stationRepository.save(new Station("South", "8am-6pm", "9am-5pm", true));
        }
    }

}
