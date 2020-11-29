package com.mahesh.numbergenerator.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

@NumbergeneratorRequestValidator
public class NumbergeneratorRequest {

    @NotNull(message = "Step value is mandatory")
    @Min(value = 0, message = "Minimum value of step is 0")
    @ApiModelProperty(notes = "Provided step value", required = true)
    private Integer step;
    @NotNull(message = "Goal value is mandatory")
    @Min(value = 0, message = "Minimum value of goal is 0")
    @ApiModelProperty(notes = "Provided goal value", required = true)
    private Integer goal;

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

}
