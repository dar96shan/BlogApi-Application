package com.kk.blog.services;

import com.kk.blog.entities.Comment;
import com.kk.blog.payloads.CommentDto;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto,Integer userId,Integer postId);

    void deleteComment(Integer commentId);
}
