package kireiko.dev.millennium.ml.logic;

import kireiko.dev.millennium.ml.data.ObjectML;
import kireiko.dev.millennium.ml.data.ResultML;

import java.util.List;

public interface Millennium {
    ResultML checkData(List<ObjectML> o);
    void learnByData(List<ObjectML> o, boolean isMustBeBlocked);
    void saveToFile(String fileName);
    int parameters();
}
