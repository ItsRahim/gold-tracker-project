package com.rahim.batchimport.processor;

import com.rahim.batchimport.model.GoldData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class DataCleanerProcessor implements ItemProcessor<GoldData, GoldData> {

    private static final Logger LOG = LoggerFactory.getLogger(DataCleanerProcessor.class);

    @Override
    public GoldData process(GoldData goldData) {
        return null;
    }
}
