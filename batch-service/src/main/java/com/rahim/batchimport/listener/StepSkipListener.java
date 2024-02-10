package com.rahim.batchimport.listener;

import com.rahim.batchimport.model.GoldData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

public class StepSkipListener implements SkipListener<GoldData, Number> {

    private static final Logger LOG = LoggerFactory.getLogger(StepSkipListener.class);

    @Override
    public void onSkipInRead(Throwable t) {
        LOG.info("Skipped item during read: " + t.getMessage());
    }

    @Override
    public void onSkipInWrite(Number item, Throwable t) {
        LOG.info("Skipped item during write: " + item + ", Error: " + t.getMessage(), item);
    }

    @Override
    public void onSkipInProcess(GoldData item, Throwable t) {
        LOG.info("Skipped item during process: " + item.toString() + ", Error: " + t.getMessage());
    }
}
