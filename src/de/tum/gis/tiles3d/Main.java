/*
 * Cesium Point Cloud Generator
 *
 * Copyright 2017 - 2018
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.gis.bgu.tum.de/
 *
 * The Cesium Point Cloud Generator is developed at Chair of Geoinformatics,
 * Technical University of Munich, Germany.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tum.gis.tiles3d;

import de.tum.gis.tiles3d.database.sqlite.SqliteDBManagerFactory;
import de.tum.gis.tiles3d.generator.PntcConfig;
import de.tum.gis.tiles3d.generator.PntcGenerationException;
import de.tum.gis.tiles3d.generator.PntcGenerator;
import de.tum.gis.tiles3d.util.Logger;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main().startUp(args);
    }

    public void startUp(String[] args) {
        PntcConfig config = new PntcConfig();
        //config.setInputPath(args[0]);
        config.setInputPath(new File("data" + File.separator + "sample_xyzRGB_data" + File.separator + "dom").getAbsolutePath());
        config.setSrid("4326");
        config.setSeparatorCharacter(",");
        config.setColorBitSize(16);
        config.setZScaleFactor(0.3048);
        config.setzOffset(-400);
        config.setTileSize(100);
        config.setMaxNumOfPointsPerTile(10000);
        //config.setOutputFolderPath(args[1]);
        config.setOutputFolderPath(new File("viewer" + File.separator + "output_data").getAbsolutePath());

        SqliteDBManagerFactory dbManagerFactory = new SqliteDBManagerFactory(config);
        final PntcGenerator generator = new PntcGenerator(config, dbManagerFactory);
        boolean success = false;
        try {
            success = generator.doProcess();
        } catch (PntcGenerationException e) {
            Logger.error(e.getMessage());
            Throwable cause = e.getCause();
            while (cause != null) {
                Logger.error("Cause: " + cause.getMessage());
                cause = cause.getCause();
                generator.setShouldRun(false);
            }
        }
    }
}
