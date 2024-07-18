package com.dev43.jonappms.job.impl;


import com.dev43.jonappms.job.Job;
import com.dev43.jonappms.job.JobRepository;
import com.dev43.jonappms.job.JobService;
import com.dev43.jonappms.job.dto.JobWithCompanyDTO;
import com.dev43.jonappms.job.external.Company;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobWithCompanyDTO> findAll() {

        List<Job> jobs = jobRepository.findAll();

        return jobs.stream().map(this::convertToDTO)        //
                .collect(Collectors.toList());
    }

    private JobWithCompanyDTO convertToDTO(Job job){

        JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
        jobWithCompanyDTO.setJob(job);      // 1

        RestTemplate restTemplate = new RestTemplate();     // imp
        Company company = restTemplate.getForObject(
                "http://localhost:8081/companies/" + job.getCompanyId(),
                Company.class);
        jobWithCompanyDTO.setCompany(company);      // 2

        return jobWithCompanyDTO;

    }


    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteJobById(Long id) {
        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean updateJobById(Long id, Job updatedJob) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();        //

            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setLocation(updatedJob.getLocation());

            jobRepository.save(job);            // imp

            return true;
        }
        return false;
    }
}
