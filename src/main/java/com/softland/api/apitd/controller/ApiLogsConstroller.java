package com.softland.api.apitd.controller;

import com.softland.api.apitd.domain.ApiLogs;
import com.softland.api.apitd.repository.ApiLogsRepository;
import com.softland.api.apitd.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiLogsConstroller {
    @Autowired
    private ApiLogsRepository repository;

    @GetMapping("/logs")
    @ResponseBody
    private List<ApiLogs> getAllApiLogs(@RequestParam(value = "page",
            defaultValue = "1") int pageNumber){
        List<ApiLogs> apiLogsList = new ArrayList<>();
        //apiLogsList.addAll(repository.getApiLogsByLatest());
        repository.getApiLogsByLatest(PageRequest.of(pageNumber-1, Utils.ROW_PER_PAGE)).forEach(apiLogsList::add);
        //repository.findAll(PageRequest.of(pageNumber-1, Utils.ROW_PER_PAGE)).forEach(apiLogsList::add);
        return apiLogsList;
    }
}
