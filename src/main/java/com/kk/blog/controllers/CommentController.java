package com.kk.blog.controllers;

import com.kk.blog.payloads.ApiResponse;
import com.kk.blog.payloads.CommentDto;
import com.kk.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/user/{userId}/post/{postId}/comment")
    private ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,
                                                     @PathVariable Integer userId,
                                                     @PathVariable Integer postId){
        CommentDto comment = this.commentService.createComment(commentDto, userId, postId);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);

    }

    @DeleteMapping("/comment/{commentId}")
    private ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){
        this.commentService.deleteComment(commentId);

        return new ResponseEntity<>(new ApiResponse("Comment is deleted successfully",true),HttpStatus.OK);

    }
}
