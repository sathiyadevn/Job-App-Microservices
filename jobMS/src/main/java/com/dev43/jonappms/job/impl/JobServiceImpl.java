package com.dev43.jonappms.job.impl;


import com.dev43.jonappms.job.Job;
import com.dev43.jonappms.job.JobRepository;
import com.dev43.jonappms.job.JobService;
import com.dev43.jonappms.job.clients.CompanyClient;
import com.dev43.jonappms.job.clients.ReviewClient;
import com.dev43.jonappms.job.dto.JobDTO;
import com.dev43.jonappms.job.external.Company;
import com.dev43.jonappms.job.external.Review;
import com.dev43.jonappms.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private JobRepository jobRepository;

    private CompanyClient companyClient;
    private ReviewClient reviewClient;

    int attempt = 0;        //

    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
    // Resilience4J
//    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    @Retry(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {
        System.out.println("Attempt " + ++attempt);

        List<Job> jobs = jobRepository.findAll();

        return jobs.stream().map(this::convertToDTO)        // imp
                .collect(Collectors.toList());
    }

    public List<String> companyBreakerFallback(Exception e) {
        List<String> list = new ArrayList<>();
        list.add("Default Values");
        return list;
    }

    private JobDTO convertToDTO(Job job) {

        Company company = companyClient.getCompany(job.getCompanyId());

        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());

        JobDTO jobDTO = JobMapper.mapToJobDTO(job, company, reviews);  // imp

        return jobDTO;

    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        return convertToDTO(job);
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
