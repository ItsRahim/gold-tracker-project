package com.rahim.batchimport.listener;

import com.rahim.batchimport.model.GoldData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

@Component
public class CustomItemReaderListener implements ItemReadListener<GoldData> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomItemReaderListener.class);

    @Override
    public void beforeRead() {
        //Do nothing
    }

    @Override
    public void afterRead(GoldData goldData) {
        //Do nothing
    }

    @Override
    public void onReadError(Exception ex) {
        if (ex instanceof FlatFileParseException parseException) {
            LOG.error("Error reading file at line {}, parsing failed for input: {}", parseException.getLineNumber(), parseException.getInput(), ex);
        } else {
            LOG.error("An unexpected error occurred during item reading: {}", ex.getMessage(), ex);
        }
    }

}
