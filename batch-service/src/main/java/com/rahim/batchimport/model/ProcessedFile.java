package com.rahim.batchimport.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProcessedFile {
    private String fileName;
    private String processingStatus;
}