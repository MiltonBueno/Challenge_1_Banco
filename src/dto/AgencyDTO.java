package dto;

import java.util.Objects;

import domain.Agency;

public class AgencyDTO {

    private final String agencyNumber;
	
	public AgencyDTO(Agency agency) {
		this.agencyNumber = agency.getAgencyNumber();
	}

	public String getAgencyNumber() {
		return agencyNumber;
	}

	@Override
	public int hashCode() {
		return Objects.hash(agencyNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgencyDTO other = (AgencyDTO) obj;
		return Objects.equals(agencyNumber, other.agencyNumber);
	}

	@Override
	public String toString() {
		return "Agency " + agencyNumber;
	}
    
}
