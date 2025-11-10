package services;

import java.util.List;

import domain.Agency;
import exceptions.BusinessException;
import exceptions.DomainNotFoundException;
import repositories.AgencyRepository;

/**
 * Service layer for managing agency operations.
 */
public class AgencyService {

    private final AgencyRepository agencyRepository;

    public AgencyService(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    public void save(Agency agency) {
        if (agency == null || agency.getAgencyNumber() == null || agency.getAgencyNumber().isBlank()) {
            throw new BusinessException("Agency number cannot be null or empty");
        }
        agencyRepository.save(agency);
    }

    public List<Agency> findAll() {
        return agencyRepository.findAll();
    }

    public Agency findByNumber(String agencyNumber) {
        return agencyRepository.findByAgencyNumber(agencyNumber)
                .orElseThrow(() -> new DomainNotFoundException("Agency not found: " + agencyNumber));
    }

    public void deleteByNumber(String agencyNumber) {
        agencyRepository.deleteByAgencyNumber(agencyNumber);
    }

    /**
     * Finds an agency by number or creates a new one if it doesn't exist.
     */
    public Agency findOrCreate(String agencyNumber) {
        return agencyRepository.findByAgencyNumber(agencyNumber)
                .orElseGet(() -> {
                    Agency newAgency = new Agency(agencyNumber);
                    agencyRepository.save(newAgency);
                    return newAgency;
                });
    }
}
