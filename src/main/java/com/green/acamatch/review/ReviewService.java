package com.green.acamatch.review;

import com.green.acamatch.acaClass.ClassRepository;
import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.academy.model.HB.GeneralReviewDto;
import com.green.acamatch.academy.model.HB.MediaReviewDto;
import com.green.acamatch.accessLog.dailyVisitorStatus.CustomUserDetails;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.exception.*;
import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.review.ReviewPic;
import com.green.acamatch.entity.review.ReviewPicIds;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.joinClass.JoinClassRepository;

import com.green.acamatch.review.dto.ReviewDto;

import com.green.acamatch.review.model.*;
import com.green.acamatch.user.repository.RelationshipRepository;
import com.green.acamatch.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor

public class ReviewService {

    private final ReviewMapper mapper;
    private final UserMessage userMessage;
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);
    private final RelationshipRepository relationshipRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JoinClassRepository joinClassRepository;
    private final ClassRepository acaClassRepository;
    private final AcademyRepository academyRepository;
    private final ReviewMapper reviewMapper;
    private final MyFileUtils myFileUtils;
    private final ReviewPicRepository reviewPicRepository;

    @Transactional
    public Integer postReview(List<MultipartFile> pics, ReviewPostReq p) {
        JoinClass joinClass = joinClassRepository.findById(p.getJoinClassId()).orElseThrow(()
                -> new CustomException(AcaClassErrorCode.NOT_FOUND_JOIN_CLASS));
        User user = userRepository.findById(p.getUserId()).orElseThrow(()
                -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        Review review = new Review();
        review.setJoinClass(joinClass);
        review.setUser(user);
        review.setComment(p.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        review.setStar(p.getStar());
        review.setBanReview(p.getBanReview());

        reviewRepository.save(review);

        long reviewId = review.getReviewId();
        String middlePath = String.format("review/%d", reviewId);
        myFileUtils.makeFolders(middlePath);

        List<String> picNameList = new ArrayList<>();

        for (MultipartFile pic : pics) {
            if (pic != null && !pic.isEmpty()) {
                String savedPicName = myFileUtils.makeRandomFileName(pic);
                String filePath = String.format("%s/%s", middlePath, savedPicName);
                picNameList.add(savedPicName);

                try {
                    myFileUtils.transferTo(pic, filePath);

                    ReviewPicIds reviewPicIds = new ReviewPicIds();
                    reviewPicIds.setReviewId(reviewId);
                    reviewPicIds.setReviewPic(savedPicName);

                    ReviewPic reviewPic = new ReviewPic();
                    reviewPic.setReviewPicIds(reviewPicIds);
                    reviewPic.setReview(review);

                    reviewPicRepository.save(reviewPic);
                } catch (IOException e) {
                    myFileUtils.deleteFolder(middlePath, true);
                    log.error("파일 저장 실패: " + e.getMessage());
                    throw new CustomException(ReviewErrorCode.IMAGE_UPLOAD_FAILED);
                }
            }
        } return 1;
    }




    //학원 전체 리뷰 조회(추가)
    public List<ReviewAcademyAllGetRes> getAcademyReviewsAll(ReviewAcademyAllGetReq req) {
        List<ReviewAcademyAllGetRes> resList = reviewMapper.getAcademyReviewsAll(req);
        if(resList == null || resList.isEmpty()) {
            userMessage.setMessage("작성된 리뷰가 없습니다.");
            return null;
        }
        userMessage.setMessage("작성된 리뷰를 조회하였습니다.");
        return resList;
    }

    //내가 작성한 리뷰 조회(추가)
    public List<ReviewMeGetRes> getMeReviews(ReviewMeGetReq req) {
        List<ReviewMeGetRes> resList = reviewMapper.getMeReviews(req);
        if(resList == null || resList.isEmpty()) {
            userMessage.setMessage("작성한 리뷰가 없습니다.");
            return null;
        }
        userMessage.setMessage("작성한 리뷰를 조회하였습니다.");
        return resList;
    }

    //내가 작성한 리뷰 조회(사진있는거)(추가)
    public List<ReviewMeGetRes> getMeNoPicReviews(ReviewMeGetReq req) {
        List<ReviewMeGetRes> resList = reviewMapper.getMeNoPicReviews(req);
        if(resList == null || resList.isEmpty()) {
            userMessage.setMessage("작성한 리뷰가 없습니다.");
            return null;
        }
        userMessage.setMessage("작성한 리뷰를 조회하였습니다.");
        return resList;
    }

    //내가 작성한 리뷰 조회(사진없는거)(추가)
    public List<ReviewMeGetRes> getMePicReviews(ReviewMeGetReq req) {
        List<ReviewMeGetRes> resList = reviewMapper.getMePicReviews(req);
        if(resList == null || resList.isEmpty()) {
            userMessage.setMessage("작성한 리뷰가 없습니다.");
            return null;
        }
        userMessage.setMessage("작성한 리뷰를 조회하였습니다.");
        return resList;
    }




}