package com.kk.blog.services;

import com.kk.blog.entities.Post;
import com.kk.blog.payloads.PostDto;
import com.kk.blog.payloads.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto,Integer userId, Integer categoryId);

    PostDto updatePost(PostDto postDto,Integer postId);

    void deletePost(Integer postId);

    List<PostDto> getAllPost();

    PostDto getByPostId(Integer postId);

    List<PostDto> getPostsByCategory(Integer categoryId);

    PostResponse getPostsByCategoryPagination(Integer categoryId, Integer pageNumber,Integer pageSize,String sortBy,String sortDir);

    List<PostDto> getPostsByUser(Integer userId);

    PostResponse getPostsByUserPagination(Integer userId, Integer pageNumber,Integer pageSize,String sortBy,String sortDir);

    List<PostDto> searchPosts(String keyword);

    PostResponse getAllPostByPagination(Integer pageNumber, Integer pageSize, String sortBy,String sortDir);

}
