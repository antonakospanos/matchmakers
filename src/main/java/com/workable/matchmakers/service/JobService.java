package com.workable.matchmakers.service;

import com.google.common.collect.Lists;
import com.workable.matchmakers.adapter.CandidateAdapter;
import com.workable.matchmakers.adapter.JobsAdapter;
import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.web.dto.jobs.JobDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class JobService {

    @Autowired
    JobsAdapter jobsAdapter;

    @Autowired
    CandidateAdapter candidateAdapter;

    public List<JobDto> list(Candidate candidate) throws InterruptedException {
        AtomicInteger retries = new AtomicInteger(0);
        if (cvFound(candidate, retries)) {
            return jobsAdapter.list(candidate);
        } else {
            return Lists.newArrayList();
        }
    }

    private boolean cvFound(Candidate candidate, AtomicInteger retries) throws InterruptedException {
        if (!candidateAdapter.found(candidate) && retries.intValue() < 10) {
            log.info(retries.intValue()  + ". Trying to fetch candidate: " + candidate.getEmail());
            Thread.sleep(500);
            retries.incrementAndGet();
            return cvFound(candidate, retries);
        } else {
            return true;
        }
    }

    public void apply(Candidate candidate, String account, String jobId) {
        jobsAdapter.apply(candidate, account, jobId);
    }
}
