package com.workable.matchmakers.adapter;

import com.workable.matchmakers.dao.model.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class ProfileDbAdapter {

    public boolean wait(Candidate candidate) {
        boolean found = false;

        Instant start = Instant.now();
        Duration gap = Duration.ofSeconds(5);
        Instant later = start.plus(gap);

        while (!found || Instant.now().isBefore(later)) {
            found = true; // FIXME polling ProfileDB
        }

        return found;
    }
}
