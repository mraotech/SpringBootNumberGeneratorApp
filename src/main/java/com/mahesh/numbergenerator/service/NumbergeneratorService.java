package com.mahesh.numbergenerator.service;

import java.util.List;

import com.mahesh.numbergenerator.model.NumbergeneratorRequest;
import com.mahesh.numbergenerator.model.ResultListResponse;
import com.mahesh.numbergenerator.model.StatusResponse;
import com.mahesh.numbergenerator.model.StatusResponse.STATUS;
import com.mahesh.numbergenerator.model.TaskResponse;

public interface NumbergeneratorService {

    TaskResponse executeNumGeneratorAsynchronously(List<NumbergeneratorRequest> numbergenRequests);

    void updateTaskStatusMap(String uuid, STATUS status);

    void updateTaskResultMap(String uuid, int order, String result);

    StatusResponse getTaskStatus(String uuid);

    ResultListResponse getTaskResult(String uuid);

}
