package com.dev43.jonappms.job;


import com.dev43.jonappms.job.dto.JobWithCompanyDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<JobWithCompanyDTO>> findAll(){
        return ResponseEntity.ok(jobService.findAll());     // 2
    }

    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody Job job){
        jobService.createJob(job);
        return new ResponseEntity<>("Job Added Successfully",HttpStatus.CREATED);   // 1
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobWithCompanyDTO> getJobById(@PathVariable Long id){
        JobWithCompanyDTO jobWithCompanyDTO=jobService.getJobById(id);
        if(jobWithCompanyDTO != null)
            return new ResponseEntity<>(jobWithCompanyDTO,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);      // 3
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable Long id){
        boolean deleted=jobService.deleteJobById(id);
        if(deleted)
            return new ResponseEntity<>("Job Deleted Successfully",HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

//    @PutMapping("/{id}")
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public ResponseEntity<String> updateJobById(@PathVariable Long id,@RequestBody Job updatedJob){
        boolean updated=jobService.updateJobById(id,updatedJob);
        if(updated)
            return new ResponseEntity<>("Job Updated Successfully",HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
