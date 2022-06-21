package com.co.softworld.util;

import com.co.softworld.configuration.Config;
import com.co.softworld.configuration.Path;

import java.util.Optional;

import static com.co.softworld.configuration.IConstants.*;

public class Utility {

    private Utility() {
    }

    public static Path builderPath(Config config) {
        if (existObject(config) && existObject(config.getPath()))
            return config.getPath();
        Path path = new Path();
        path.setList(DEFAULT_BASE);
        path.setDetail(DEFAULT_ID);
        path.setSave(DEFAULT_BASE);
        path.setUpdate(DEFAULT_ID);
        path.setDelete(DEFAULT_ID);
        path.setUpload(DEFAULT_UPLOAD);
        path.setUploadId(DEFAULT_UPLOAD_ID);
        path.setPhoto(DEFAULT_PHOTO);
        return path;
    }

    public static boolean existObject(Object object) {
        return Optional.ofNullable(object).isPresent();
    }
}
