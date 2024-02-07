package com.kk.blog.services;

import com.kk.blog.entities.Comment;
import com.kk.blog.entities.Post;
import com.kk.blog.entities.User;
import com.kk.blog.exceptions.ResourceNotFoundException;
import com.kk.blog.payloads.CommentDto;
import com.kk.blog.respositories.CommentRepo;
import com.kk.blog.respositories.PostRepo;
import com.kk.blog.respositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer userId, Integer postId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "user id", userId));

        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "post Id", postId));

        Comment comment = this.modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);
        comment.setUser(user);

        Comment save = this.commentRepo.save(comment);
         return this.modelMapper.map(save, CommentDto.class);

//        Comment comment = new Comment();
//        comment.setContent(commentDto.getContent());
//        comment.setUser(user);
//        comment.setPost(post);
//
//
//        CommentDto commentCreate =
//        return commentCreate;
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = this.commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "comment Id", commentId));

        this.commentRepo.delete(comment);

    }
}
