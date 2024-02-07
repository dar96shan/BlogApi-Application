package com.kk.blog.services;

import com.kk.blog.entities.Category;
import com.kk.blog.entities.Comment;
import com.kk.blog.entities.Post;
import com.kk.blog.entities.User;
import com.kk.blog.exceptions.ResourceNotFoundException;
import com.kk.blog.exceptions.ResourceNotFoundExceptionString;
import com.kk.blog.payloads.*;
import com.kk.blog.respositories.CategoryRepo;
import com.kk.blog.respositories.CommentRepo;
import com.kk.blog.respositories.PostRepo;
import com.kk.blog.respositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Override
    public PostDto createPost(PostDto postDto,Integer userId, Integer categoryId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));


        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));


        Post create = this.modelMapper.map(postDto, Post.class);
        create.setImageName("default.png");
        create.setAddedDate(new Date());
        create.setUser(user);
        create.setCategory(category);
        Post newPost = postRepo.save(create);

        // Convert saved Post entity to PostDto
        PostDto newPostDto = this.modelMapper.map(newPost, PostDto.class);

        // Convert User and Category entities to DTOs
        UserDto userDto = this.modelMapper.map(user, UserDto.class);

        CategoryDto categoryDto = this.modelMapper.map(category, CategoryDto.class);

        // Set UserDto and CategoryDto in the new PostDto
        newPostDto.setUser(userDto);
        newPostDto.setCategory(categoryDto);

        return newPostDto;
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());

        Post updated = postRepo.save(post);

        return this.modelMapper.map(updated, PostDto.class);

    }

    @Override
    public void deletePost(Integer postId) {
        Post id = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));
         postRepo.delete(id);
    }

    @Transactional
    @Override
    public List<PostDto> getAllPost() {
//        List<Post> all = postRepo.findAll();
//        return all.stream()
//                .map(post -> modelMapper.map(post, PostDto.class))
//                .collect(Collectors.toList());

        List<Post> allPosts = postRepo.findAll();
        return allPosts.stream().map(post -> {
            PostDto postDto = modelMapper.map(post, PostDto.class);
            Set<Comment> comments = commentRepo.findByPost(post);
            Set<CommentDto> commentDtos = comments.stream()
                    .map(comment -> modelMapper.map(comment, CommentDto.class))
                    .collect(Collectors.toSet());
            postDto.setComments(commentDtos);
            return postDto;
        }).collect(Collectors.toList());

    }

    @Transactional
    @Override
    public PostDto getByPostId(Integer postId) {
        Post id = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));

        Set<Comment> comments = this.commentRepo.findByPost(id);

        PostDto postDto = this.modelMapper.map(id, PostDto.class);
        Set<CommentDto> commentDto = comments.stream().map(comment -> this.modelMapper.map(comment, CommentDto.class))
                .collect(Collectors.toSet());
        postDto.setComments(commentDto);

        return postDto;
    }

    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) {
        Category category= categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","category id",categoryId));

        List<Post> postByCategory = this.postRepo.findByCategory(category);

        return postByCategory.stream()
                .map((e) -> this.modelMapper.map(e, PostDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public PostResponse getPostsByCategoryPagination(Integer categoryId, Integer pageNumber, Integer pageSize,String sortBy,String sortDir) {

        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "Category Id", categoryId));

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Post> byCategory = postRepo.findByCategory(category, pageable);

        List<PostDto> collect = byCategory.stream()
                .map(e -> this.modelMapper.map(e, PostDto.class))
                .collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(collect);
        postResponse.setPageNumber(byCategory.getNumber());
        postResponse.setPageSize(byCategory.getSize());
        postResponse.setTotalElements(byCategory.getNumberOfElements());
        postResponse.setTotalPages(byCategory.getTotalPages());
        postResponse.setLastPage(byCategory.isLast());

        return postResponse;
    }


    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));
        List<Post> postUser = this.postRepo.findByUser(user);

        return postUser.stream()
                .map(e -> this.modelMapper.map(e, PostDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public PostResponse getPostsByUserPagination(Integer userId, Integer pageNumber, Integer pageSize,String sortBy,String sortDir) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "user id", userId));
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Post> userPost = postRepo.findByUser(user, pageable);
        List<Post> content = userPost.getContent();

        List<PostDto> collect = content.stream()
                .map(e -> this.modelMapper.map(e, PostDto.class))
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(collect);
        postResponse.setPageNumber(userPost.getNumber());
        postResponse.setPageSize(userPost.getSize());
        postResponse.setTotalElements(userPost.getNumberOfElements());
        postResponse.setTotalPages(userPost.getTotalPages());
        postResponse.setLastPage(userPost.isLast());

        return postResponse;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {
        List<Post> byTitleContaining = this.postRepo.findByContentContaining(keyword);

        if(byTitleContaining.isEmpty()){
            throw new ResourceNotFoundExceptionString("Content","Keyword",keyword);
        }
        List<PostDto> collect = byTitleContaining.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public PostResponse getAllPostByPagination(Integer pageNumber, Integer pageSize, String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable page = PageRequest.of(pageNumber,pageSize,sort);

        Page<Post> pagePost = this.postRepo.findAll(page);
        List<Post> content = pagePost.getContent();
        List<PostDto> collectByPage = content.stream()
                .map(e -> this.modelMapper.map(e, PostDto.class))
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(collectByPage);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getNumberOfElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }
}
