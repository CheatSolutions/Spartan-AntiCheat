package kireiko.dev.millennium.ml.data.module;

import kireiko.dev.millennium.ml.data.ResultML;

public interface ModuleML {
    String getName();
    ModuleResultML getResult(ResultML resultML);
    int getParameterBuffer();
}
