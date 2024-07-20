package com.dev43.jonappms.job;

import com.dev43.jonappms.job.dto.JobWithCompanyDTO;

import java.util.List;

public interface JobService {
    List<JobWithCompanyDTO> findAll();

    void createJob(Job job);

    JobWithCompanyDTO getJobById(Long id);

    boolean deleteJobById(Long id);

    boolean updateJobById(Long id, Job updatedJob);
}
