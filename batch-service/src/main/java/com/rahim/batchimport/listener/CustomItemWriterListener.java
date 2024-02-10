package com.rahim.batchimport.listener;

import com.rahim.batchimport.model.GoldPriceHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomItemWriterListener implements ItemWriteListener<GoldPriceHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomItemWriterListener.class);

    @Override
    public void beforeWrite(Chunk<? extends GoldPriceHistory> items) {
        //Do Nothing
    }

    @Override
    public void afterWrite(Chunk<? extends GoldPriceHistory> items) {
        //Do Nothing
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends GoldPriceHistory> items) {
        LOG.error("Error occurred while writing items to the destination: " + exception.getMessage());
        List<? extends GoldPriceHistory> itemList = items.getItems();
        for (GoldPriceHistory item : itemList) {
            LOG.error("Error writing item: " + item);
        }
    }
}
