package com.dev43.companyms.company;

import com.dev43.companyms.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {
    List<Company> getAllCompanies();
    boolean updateCompanyById(Long id,Company company);
    void createCompany(Company company);
    boolean deleteCompanyById(Long id);
    Company getCompanyById(Long ig);

    public void updateCompanyRating(ReviewMessage reviewMessage);
}
