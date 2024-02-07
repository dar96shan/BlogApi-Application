package com.kk.blog.respositories;

import com.kk.blog.entities.Category;
import com.kk.blog.entities.Post;
import com.kk.blog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post,Integer> {

    //Derived Methods JPA
    List<Post> findByUser(User user);

    Page<Post> findByUser(User user, Pageable pageable);

    Page<Post> findByCategory(Category category, Pageable pageable);

    List<Post> findByCategory(Category category);
    List<Post> findByContentContaining(String title);

}
