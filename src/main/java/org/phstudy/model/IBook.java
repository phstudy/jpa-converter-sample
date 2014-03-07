package org.phstudy.model;

import java.util.Map;

public interface IBook {
	public abstract Long getId();

	public abstract Map<String, String> getMetadata();

	public abstract void setMetadata(Map<String, String> metadata);
}
