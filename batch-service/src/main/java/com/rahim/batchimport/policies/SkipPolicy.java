package com.rahim.batchimport.policies;

import com.rahim.batchimport.exception.DuplicateEffectiveDateException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;

@RequiredArgsConstructor
public class SkipPolicy implements org.springframework.batch.core.step.skip.SkipPolicy {

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        if (t instanceof NumberFormatException || t instanceof DuplicateEffectiveDateException) {
            return true;
        }

        return t instanceof Exception;
    }
}
