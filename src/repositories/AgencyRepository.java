package repositories;

import java.util.List;
import java.util.Optional;

import domain.Agency;

public interface AgencyRepository {
	
    void save(Agency agency);
    
    List<Agency> findAll();
    
    Optional<Agency> findByAgencyNumber(String agencyNumber);

	void deleteByAgencyNumber(String agencyNumber);
    
}
