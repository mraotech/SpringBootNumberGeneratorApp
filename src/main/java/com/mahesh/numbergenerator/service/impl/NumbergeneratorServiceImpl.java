package com.mahesh.numbergenerator.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;

import com.mahesh.numbergenerator.model.NumbergeneratorRequest;
import com.mahesh.numbergenerator.model.ResultListResponse;
import com.mahesh.numbergenerator.model.StatusResponse;
import com.mahesh.numbergenerator.model.StatusResponse.STATUS;
import com.mahesh.numbergenerator.model.TaskResponse;
import com.mahesh.numbergenerator.service.NumbergeneratorService;

@Service
@Qualifier("primaryNumberGeneratorService")
public class NumbergeneratorServiceImpl implements NumbergeneratorService {

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext applicationContext;

    // Maintain two maps. One for task (UUID) to status and another for task (UUID) to result
    private final ConcurrentHashMap<String, ConcurrentSkipListMap<String, String>> taskResultMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, STATUS> taskStatusMap = new ConcurrentHashMap<>();

    // Handles execution for both single and bulk updates. The Thread config defines the core pool, queue and max pool size and based on that config seprate threads are spawned and handled 
    public TaskResponse executeNumGeneratorAsynchronously(List<NumbergeneratorRequest> numbergenRequests) {
        UUID uuid = UUID.randomUUID();
        String uuidVal = uuid.toString();
        int i = 1;
        boolean taskStatusUpdated = false;
        for (NumbergeneratorRequest req : numbergenRequests) {
            ConcurrentSkipListMap<String, String> orderedMap = null;
            if (!taskResultMap.containsKey(uuidVal)) {
                orderedMap = new ConcurrentSkipListMap<>();
            } else {
                orderedMap = taskResultMap.get(uuidVal);
            }
            orderedMap.put(Integer.toString(i), "");
            taskResultMap.put(uuidVal, orderedMap);
            if (!taskStatusUpdated) {
                taskStatusMap.put(uuidVal, STATUS.IN_PROGRESS);
                taskStatusUpdated = true;
            }
            NumbergeneratorThread numThread = applicationContext.getBean(NumbergeneratorThread.class, req.getStep(), req.getGoal(), uuidVal, i);
            try {
                taskExecutor.execute(numThread);
            } catch (TaskRejectedException e) {
                taskStatusMap.put(uuidVal, STATUS.ERROR);
                throw e;
            }
            i++;
        }
        TaskResponse response = new TaskResponse();
        response.setTask(uuidVal);
        return response;
    }

    public void updateTaskStatusMap(String uuid, STATUS status) {
        taskStatusMap.put(uuid, status);
    }

    public void updateTaskResultMap(String uuid, int order, String result) {
        if (taskResultMap.containsKey(uuid)) {
            ConcurrentSkipListMap<String, String> orderedMap = taskResultMap.get(uuid);
            orderedMap.put(Integer.toString(order), result);
            // For single OR bulk updates, once we don't have any empty results, it means we are done and time to make the result as success.
            if (!orderedMap.values().contains("")) {
                updateTaskStatusMap(uuid, STATUS.SUCCESS);
            }
        }
    }

    public StatusResponse getTaskStatus(String uuid) {
        StatusResponse response = null;
        if (taskStatusMap.containsKey(uuid)) {
            response = new StatusResponse();
            response.setResult(taskStatusMap.get(uuid));
        }
        return response;
    }

    public ResultListResponse getTaskResult(String uuid) {
        ResultListResponse response = null;
        if (taskResultMap.containsKey(uuid)) {
            response = new ResultListResponse();
            ConcurrentSkipListMap<String, String> orderedMap = taskResultMap.get(uuid);
            if (orderedMap.size() == 1) {
                response.setResult(orderedMap.firstEntry().getValue());
            } else {
                List<String> results = new ArrayList<String>();
                for (Map.Entry<String, String> entry : orderedMap.entrySet()) {
                    results.add(entry.getValue());
                }
                response.setResults(results);
            }
            
        }
        return response;
    }

}
