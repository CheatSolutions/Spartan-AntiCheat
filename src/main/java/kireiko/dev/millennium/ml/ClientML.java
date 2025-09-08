package kireiko.dev.millennium.ml;

import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules.*;
import kireiko.dev.millennium.ml.data.module.ModuleML;
import kireiko.dev.millennium.ml.logic.Logger;
import kireiko.dev.millennium.ml.logic.ModelVer;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ClientML {

    public static final boolean DEV_MODE = false;

    public static final String CLIENT_NAME = "001-premium-complex-18k";
    private static final int TABLE_SIZE = 2;

    public static final List<ModuleML> MODEL_LIST = Arrays.asList(
                    new M1Module(),
                    new M2Module(),
                    new M3Module(),
                    new M4Module(),
                    new M5Module(),
                    new MHuge1Module(),
                    new MHuge2Module()
    );

    public void run() {
        for (int i = 0; i < MODEL_LIST.size(); i++) {
            final ModuleML moduleML = MODEL_LIST.get(i);
            if (DEV_MODE) {
                FactoryML.loadFromFile(i, moduleML.getName() + ".dat", TABLE_SIZE, moduleML.getParameterBuffer(), ModelVer.VERSION_4_5);
            } else {
                FactoryML.loadFromResources(i, MODEL_LIST.get(i).getName() + ".dat", TABLE_SIZE, moduleML.getParameterBuffer(), ModelVer.VERSION_4_5);
            }
        }
        Logger.info(CLIENT_NAME + " loaded!");
    }
}
