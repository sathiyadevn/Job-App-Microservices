package com.dev43.jonappms.job.impl;


import com.dev43.jonappms.job.Job;
import com.dev43.jonappms.job.JobRepository;
import com.dev43.jonappms.job.JobService;
import com.dev43.jonappms.job.dto.JobDTO;
import com.dev43.jonappms.job.external.Company;
import com.dev43.jonappms.job.external.Review;
import com.dev43.jonappms.job.mapper.JobMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private JobRepository jobRepository;

    private RestTemplate restTemplate;      // InterService Communication

    public JobServiceImpl(JobRepository jobRepository, RestTemplate restTemplate) {
        this.jobRepository = jobRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<JobDTO> findAll() {

        List<Job> jobs = jobRepository.findAll();

        return jobs.stream().map(this::convertToDTO)        // imp
                .collect(Collectors.toList());
    }

    private JobDTO convertToDTO(Job job){

        //  getForObject()
        Company company = restTemplate.getForObject(       // restTemplate <- AppConfig.java
                "http://COMPANY-SERVICE:8081/companies/" + job.getCompanyId(),
                Company.class);     //

        //  exchange()
        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
                "http://REVIEW-SERVICE:8083/reviews?companyId=" + job.getCompanyId(),
                HttpMethod.GET,           // url from GET method
                null,       // no RequestBody - not a POST method
                new ParameterizedTypeReference<List<Review>>() {    // return List of Generic type(Review)
                });

        List<Review> reviews = reviewResponse.getBody();

        JobDTO jobDTO = JobMapper.mapToJobDTO(job,company,reviews);  // imp

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
