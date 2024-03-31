package com.earlyadopter.backend.service.api.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class MakeCsvFileOutput {
    private static final Logger logger = LoggerFactory.getLogger(MakeCsvFileOutput.class);

    public static <T> void makeCsvFileOut(String fileName, Iterable<T> documentList) {

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            for (T document : documentList) {

                Class<?> objectClass = document.getClass();
                Field[] fields = objectClass.getDeclaredFields();

                for (Field field : fields) {

                    field.setAccessible(true);

                    String fieldName = field.getName();

                    Object value = field.get(fieldName);

                    writer.write(fieldName + " : ");
                    writer.write(value.toString() + "\t ");

                }

                writer.newLine();
            }

        } catch (IOException | IllegalAccessException e) {
            logger.info("Exception occur [{}] ", e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
