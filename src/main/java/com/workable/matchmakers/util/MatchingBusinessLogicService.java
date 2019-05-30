package com.workable.matchmakers.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Business Logic Functions to calculate percentage for attributes
 */
@Service
public class MatchingBusinessLogicService {

    private final static Logger logger = LoggerFactory.getLogger(MatchingBusinessLogicService.class);

    /**
     * Calculates the matching percentage for salary
     *
     * @param candidateSalaryFrom candidate salary from
     * @param candidateSalaryTo   candidate salary to
     * @param compSalaryFrom      company salary from
     * @param compSalaryTo        company salary to
     * @return the percentage of the matching between the candidate salary and company salary
     */
    public Double calculateMatchingSalary(Long candidateSalaryFrom,
                                          Long candidateSalaryTo,
                                          Long compSalaryFrom,
                                          Long compSalaryTo) {

        try {
            if (Objects.equals(compSalaryFrom, candidateSalaryFrom)
                    ||(candidateSalaryTo != null && candidateSalaryTo <= compSalaryFrom)){
                return (double) 100;
            } else if (compSalaryTo != null && Objects.equals(candidateSalaryFrom, compSalaryTo)) {
                return (double) 50;
            } else return (double) 0;
        } catch (Exception ex) {
            logger.error("Candidate Salary from or Company Salary from cannot be null || " + ex.getMessage());
            throw new IllegalArgumentException("Wrong Arguments");


        }
    }

    /**
     * Calculates the matching percentage for Working Experience
     *
     * @param candidateExpYears candidate working experience (years)
     * @param companyExpYears   company working experince (years)
     * @return the percentage of the matching between the candidate experience and company experience
     */
    public Double calculateMatchingExperience(Long candidateExpYears, Long companyExpYears) {
        try {
            if (Objects.equals(candidateExpYears, companyExpYears)) {
                return (double) 100;
            } else if (companyExpYears - 1 == candidateExpYears || companyExpYears + 1 == candidateExpYears) {
                return (double) 50;
            } else return (double) 0;
        } catch (Exception ex) {
            logger.error("Candidate Experience Years or Company Experience Years cannot be null  ||" + ex.getMessage());
            throw new IllegalArgumentException("Wrong Arguments");
        }
    }

}
