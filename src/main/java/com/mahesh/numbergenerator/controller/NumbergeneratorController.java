package com.mahesh.numbergenerator.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mahesh.numbergenerator.model.NumbergeneratorRequest;
import com.mahesh.numbergenerator.model.ResultListResponse;
import com.mahesh.numbergenerator.model.StatusResponse;
import com.mahesh.numbergenerator.model.TaskResponse;
import com.mahesh.numbergenerator.service.NumbergeneratorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@RestController
@RequestMapping("/api")
@Api(value = "Number Generator", description = "Rest API for number generator", tags = "Number Generator API")
public class NumbergeneratorController {

    @Autowired
    @Qualifier("primaryNumberGeneratorService")
    private NumbergeneratorService numbergeneratorService;

    @RequestMapping(value = "/generate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Generate a task to find the single range of numbers based on step and goal and returns a task id.", response = TaskResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Task Accepted"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public ResponseEntity<?> generateNumbers(HttpServletRequest httpServletRequest, @RequestBody @Valid NumbergeneratorRequest payload) {
        List<NumbergeneratorRequest> reqs = new ArrayList<>();
        reqs.add(payload);
        TaskResponse response = numbergeneratorService.executeNumGeneratorAsynchronously(reqs);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/bulkGenerate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Generate a task to find multiple range of numbers based on the bulk requests each having step and goal and returns a task id.", response = TaskResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Task Accepted"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public ResponseEntity<?> generateNumbersInBulk(HttpServletRequest httpServletRequest, @RequestBody @NotNull @NotEmpty List<@Valid NumbergeneratorRequest> payload) {
        TaskResponse response = numbergeneratorService.executeNumGeneratorAsynchronously(payload);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/tasks/{id}/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get the status of the number generation task.", response = StatusResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Task Not Found")
    })
    public ResponseEntity<?> getTaskStatus(HttpServletRequest httpServletRequest, @PathVariable(name="id", required = true) String uuid) {
        StatusResponse response = numbergeneratorService.getTaskStatus(uuid);
        if (response == null) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", new Date());
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
            body.put("message", "Task not found matching the UUID");
            body.put("path", httpServletRequest.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get the result in the form of a single OR multiple range of numbers based on the number generation request.", response = ResultListResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Task Not Found")
    })
    public ResponseEntity<?> getTaskResult(HttpServletRequest httpServletRequest, @PathVariable(name="id", required = true) String uuid,
            @RequestParam(name = "action", required = true) @Pattern(regexp="get_numlist", message="Only get_numlist Request Parameter is allowed") String action) {
        ResultListResponse response = null;
        response = numbergeneratorService.getTaskResult(uuid);
        if (response == null) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", new Date());
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
            body.put("message", "Task not found matching the UUID");
            body.put("path", httpServletRequest.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
