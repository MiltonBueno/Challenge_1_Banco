package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import domain.account.Account;

public class Agency{

    private final String agencyNumber;
    private final List<Account> accounts = new ArrayList<>();
    
	public Agency(String agencyNumber) {
		this.agencyNumber = agencyNumber;
	}

	public String getAgencyNumber() {
		return agencyNumber;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

    public void addAccount(Account account) {
        accounts.add(account);
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
		Agency other = (Agency) obj;
		return Objects.equals(agencyNumber, other.agencyNumber);
	}

	@Override
	public String toString() {
		return "Agency [agencyNumber=" + agencyNumber + ", accounts=" + accounts.size() + "]";
	}
    
}
