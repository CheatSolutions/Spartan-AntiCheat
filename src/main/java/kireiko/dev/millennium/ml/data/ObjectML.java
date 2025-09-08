package kireiko.dev.millennium.ml.data;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ObjectML implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Double> values;
    public ObjectML(List<Double> values) {
        this.values = values;
    }
}
