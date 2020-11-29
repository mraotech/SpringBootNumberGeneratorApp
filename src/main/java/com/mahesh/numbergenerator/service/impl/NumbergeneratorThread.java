package com.mahesh.numbergenerator.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mahesh.numbergenerator.service.NumbergeneratorService;

@Component
@Scope("prototype")
public class NumbergeneratorThread implements Runnable {

    @Autowired
    @Qualifier("primaryNumberGeneratorService")
    private NumbergeneratorService numbergeneratorService;

    private int step;
    private int goal;
    private int order;
    private String uuid;

    public NumbergeneratorThread(int step, int goal, String uuid, int order) {
        this.step = step;
        this.goal = goal;
        this.uuid = uuid;
        this.order = order;
    }

    @Override
    public void run() {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        // Randomly sleep between 10-30 seconds as per requirement
        int randomNumber = random.nextInt(30 - 10) + 10;
        try {
            Thread.sleep(randomNumber * 1000);
        } catch (InterruptedException e) {
            // Ignore and continue without sleep
        }
        for (int i = goal; i >= 0;) {
            result.append(i);
            i = i - step;
            if (i >= 0) {
                result.append(",");
            }
        }
        numbergeneratorService.updateTaskResultMap(uuid, order, result.toString());
    }

}
