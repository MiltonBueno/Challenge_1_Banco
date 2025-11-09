package domain;

import java.util.UUID;

public abstract class BaseIdEntity {
	
	protected final UUID id = UUID.randomUUID();

	public UUID getId() {
		return id;
	}

}
