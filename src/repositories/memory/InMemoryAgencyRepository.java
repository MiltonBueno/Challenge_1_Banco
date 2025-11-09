package repositories.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import domain.Agency;
import repositories.AgencyRepository;

public class InMemoryAgencyRepository implements AgencyRepository {
	

    private final Map<String, Agency> database = new ConcurrentHashMap<>();

    @Override
    public void save(Agency agency) {
        if (agency == null || agency.getAgencyNumber() == null) {
            throw new IllegalArgumentException("Cannot save null agency or agency with null number");
        }
        database.put(agency.getAgencyNumber(), agency);
    }

    @Override
    public List<Agency> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<Agency> findByAgencyNumber(String agencyNumber) {
        if (agencyNumber == null || agencyNumber.isBlank()) {
            throw new IllegalArgumentException("Agency number cannot be null or empty");
        }
        return Optional.ofNullable(database.get(agencyNumber));
    }

    @Override
    public void deleteByAgencyNumber(String agencyNumber) {
        if (agencyNumber == null || agencyNumber.isBlank()) {
            throw new IllegalArgumentException("Agency number cannot be null or empty");
        }

        if (!database.containsKey(agencyNumber)) {
            throw new NoSuchElementException("Agency not found for agencyNumber: " + agencyNumber);
        }

        database.remove(agencyNumber);
    }
    
}
