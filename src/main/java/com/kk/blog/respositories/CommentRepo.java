package com.kk.blog.respositories;

import com.kk.blog.entities.Comment;
import com.kk.blog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Integer> {

    Set<Comment> findByPost(Post post);

}
