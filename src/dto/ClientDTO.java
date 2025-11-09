package dto;

import java.util.Objects;

import domain.Client;

public class ClientDTO {
	
	private final String id;
	private final String name;
	
	public ClientDTO(Client client) {
		this.id = client.getId().toString();
		this.name = client.getName();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientDTO other = (ClientDTO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return name + " (ID: " + id.substring(0, 8) + "...)";
	}
}
