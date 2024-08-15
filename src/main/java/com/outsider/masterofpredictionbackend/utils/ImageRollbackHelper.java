package com.outsider.masterofpredictionbackend.utils;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class ImageRollbackHelper {

    private final List<String> imagesToDelete = new ArrayList<>();

    public void addImageToDelete(String imageUrl) {
        imagesToDelete.add(imageUrl);
    }

    public void registerForRollback() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    if (status == STATUS_ROLLED_BACK) {
                        imagesToDelete.forEach(ImageStorageService::deleteImage);
                    }
                }
            });
        }
    }
}