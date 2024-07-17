package com.dev43.reviewms.review;

import java.util.List;

public interface ReviewService {
    List<Review> getAllReviews(Long companyId);

    boolean addReview(Long companyId, Review review);

    Review getReviewById(Long reviewId);

    boolean updateReviewById(Long reviewId, Review updatedReview);

    boolean deleteReview(Long reviewId);
}
