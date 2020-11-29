package com.mahesh.numbergenerator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNumbergeneratorApp extends NumbergeneratorApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void atestGetStatusWrongId() throws Exception {
        mockMvc.perform(get("/api/tasks/a-1/status"))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void btestGetResultWrongId() throws Exception {
        mockMvc.perform(get("/api/tasks/a-1/result?action=get_numlistddd"))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void ctestPostSingleGenerateWithNoStep() throws Exception {
        mockMvc.perform(post("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"goal\":\"9\"}"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void dtestPostSingleGenerateWithGoalInputRenamed() throws Exception {
        mockMvc.perform(post("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"goals\":\"9\",\"step\":\"9\"}"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void etestPostSingleGenerateWithStringStepValue() throws Exception {
        mockMvc.perform(post("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"goal\":\"9\",\"step\":\"SSS\"}"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void ftestPostSingleGenerateWithNegativeGoalValue() throws Exception {
        mockMvc.perform(post("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"goal\":\"-1\",\"step\":\"4\"}"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void gtestPostSingleGenerateWithStepGreaterThanGoal() throws Exception {
        mockMvc.perform(post("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"goal\":\"5\",\"step\":\"7\"}"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void htestPostSingleGenerateSuccess1() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"goal\":\"10\",\"step\":\"2\"}"))
        .andDo(print())
        .andExpect(status().isAccepted())
        .andReturn();
        String content = JsonPath.parse(result.getResponse().getContentAsString()).read("$.task");
        MvcResult result2 = mockMvc.perform(get("/api/tasks/" + content + "/status"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
        Assert.assertEquals("IN_PROGRESS", JsonPath.parse(result2.getResponse().getContentAsString()).read("$.result"));
        Thread.sleep(30000);
        MvcResult result3 = mockMvc.perform(get("/api/tasks/" + content + "/status"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
        Assert.assertEquals("SUCCESS", JsonPath.parse(result3.getResponse().getContentAsString()).read("$.result"));
        MvcResult result4 = mockMvc.perform(get("/api/tasks/" + content + "?action=get_numlist"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
        Assert.assertEquals("10,8,6,4,2,0", JsonPath.parse(result4.getResponse().getContentAsString()).read("$.result"));
    }

    @Test
    public void gtestPostSingleGenerateSuccess2() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"goal\":\"8\",\"step\":\"8\"}"))
        .andDo(print())
        .andExpect(status().isAccepted())
        .andReturn();
        String content = JsonPath.parse(result.getResponse().getContentAsString()).read("$.task");
        MvcResult result2 = mockMvc.perform(get("/api/tasks/" + content + "/status"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
        Assert.assertEquals("IN_PROGRESS", JsonPath.parse(result2.getResponse().getContentAsString()).read("$.result"));
        Thread.sleep(30000);
        MvcResult result3 = mockMvc.perform(get("/api/tasks/" + content + "/status"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
        Assert.assertEquals("SUCCESS", JsonPath.parse(result3.getResponse().getContentAsString()).read("$.result"));
        MvcResult result4 = mockMvc.perform(get("/api/tasks/" + content + "?action=get_numlist"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
        Assert.assertEquals("8,0", JsonPath.parse(result4.getResponse().getContentAsString()).read("$.result"));
    }

    @Test
    public void itestPostBulkGenerateWithNoStep() throws Exception {
        mockMvc.perform(post("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("[{\"goal\":\"10\",\"step\":\"3\"},{\"goal\":\"8\"}]"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void jtestPostBulkGenerateWithNegativeGoalValue() throws Exception {
        mockMvc.perform(post("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("[{\"goal\":\"10\",\"step\":\"3\"},{\"goal\":\"-8\",\"step\":\"7\"},{\"goal\":\"-9\",\"step\":\"5\"}]"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void ktestPostBulkGenerateWithStepGreaterThanGoal1() throws Exception {
        mockMvc.perform(post("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("[{\"goal\":\"10\",\"step\":\"3\"},{\"goal\":\"8\",\"step\":\"9\"}]"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void ltestPostBulkGenerateWithStepGreaterThanGoal2() throws Exception {
        mockMvc.perform(post("/api/generate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("[{\"goal\":\"5\",\"step\":\"7\"}]"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void mtestPostBulkGenerateSuccess() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/bulkGenerate")
        .contentType(MediaType.APPLICATION_JSON)
        .content("[{\"goal\":\"10\",\"step\":\"3\"},{\"goal\":\"8\",\"step\":\"7\"},{\"goal\":\"9\",\"step\":\"5\"}]"))
        .andDo(print())
        .andExpect(status().isAccepted())
        .andReturn();
        String content = JsonPath.parse(result.getResponse().getContentAsString()).read("$.task");
        MvcResult result2 = mockMvc.perform(get("/api/tasks/" + content + "/status"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
        Assert.assertEquals("IN_PROGRESS", JsonPath.parse(result2.getResponse().getContentAsString()).read("$.result"));
        Thread.sleep(30000);
        MvcResult result3 = mockMvc.perform(get("/api/tasks/" + content + "/status"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
        Assert.assertEquals("SUCCESS", JsonPath.parse(result3.getResponse().getContentAsString()).read("$.result"));
        MvcResult result4 = mockMvc.perform(get("/api/tasks/" + content + "?action=get_numlist"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
        DocumentContext cxt = JsonPath.parse(result4.getResponse().getContentAsString());
        Assert.assertEquals("10,7,4,1", cxt.read("$.results[0]"));
        Assert.assertEquals("8,1", cxt.read("$.results[1]"));
        Assert.assertEquals("9,4", cxt.read("$.results[2]"));
    }

}
