package org.phstudy.eclipselink.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.phstudy.model.IBook;

@Entity
public class Book implements Serializable, IBook {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "metadata", columnDefinition = "hstore")
	@Convert(converter = org.phstudy.eclipselink.conventer.MapToStringConveter.class)
	private Map<String, String> metadata = new HashMap<String, String>();

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public Map<String, String> getMetadata() {
		return metadata;
	}

	@Override
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}
}
