package org.phstudy.model;

import org.phstudy.converter.MapToStringConveter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // In fact, EclipseLink does not need to use Converter
    @Convert(converter = MapToStringConveter.class)
    private Map<String, String> metadata = new HashMap<>();

    public Long getId() {
        return id;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
