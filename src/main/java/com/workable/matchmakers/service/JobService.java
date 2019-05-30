package com.workable.matchmakers.service;

import com.workable.matchmakers.adapter.JobsAdapter;
import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.web.dto.jobs.JobDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    JobsAdapter adapter;

    public List<JobDto> list(Candidate candidate) {
        return adapter.list(candidate);
    }
}
