package com.dev43.jonappms.job.mapper;

import com.dev43.jonappms.job.Job;
import com.dev43.jonappms.job.dto.JobDTO;
import com.dev43.jonappms.job.external.Company;
import com.dev43.jonappms.job.external.Review;

import java.util.List;

public class JobMapper {
    public static JobDTO mapToJobDTO(Job job, Company company, List<Review> reviews){

        JobDTO jobDTO = new JobDTO();      // imp

        jobDTO.setId(job.getId());
        jobDTO.setTitle(job.getTitle());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setMinSalary(job.getMinSalary());

        jobDTO.setCompany(company);     //

        jobDTO.setReviews(reviews);      //

        return jobDTO;
    }
}
