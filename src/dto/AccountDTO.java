package dto;

import java.util.Objects;

import domain.account.Account;

public class AccountDTO {

	private final String accountNumber;
	private final ClientDTO clientDTO;
	private final AgencyDTO agencyDTO;

	public AccountDTO(Account account) {
		this.accountNumber = account.getAccountNumber();
		this.clientDTO = new ClientDTO(account.getClient());
		this.agencyDTO = new AgencyDTO(account.getAgency());
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public ClientDTO getClientDTO() {
		return clientDTO;
	}

	public AgencyDTO getAgencyDTO() {
		return agencyDTO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountDTO other = (AccountDTO) obj;
		return Objects.equals(accountNumber, other.accountNumber);
	}

	@Override
	public String toString() {
		return "Account " + accountNumber + " | Client: " + clientDTO.getName() + " | Agency: " + agencyDTO.getAgencyNumber();
	}
}
