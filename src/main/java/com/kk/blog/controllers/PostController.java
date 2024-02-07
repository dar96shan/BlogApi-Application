package com.kk.blog.controllers;

import com.kk.blog.config.AppConst;
import com.kk.blog.payloads.ApiResponse;
import com.kk.blog.payloads.PostDto;
import com.kk.blog.payloads.PostResponse;
import com.kk.blog.services.FileService;
import com.kk.blog.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;


   //Create Post
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(
            @RequestBody PostDto postDto,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId){

                    PostDto create = postService.createPost(postDto, userId, categoryId);
            return new ResponseEntity<PostDto>(create, HttpStatus.CREATED);
    }

    //get by user
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostByUser(@PathVariable Integer userId){
        List<PostDto> byPostId = this.postService.getPostsByUser(userId);
        return new ResponseEntity<>(byPostId,HttpStatus.OK);
    }

    //get by Category
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable Integer categoryId){
        List<PostDto> postsByCategory = this.postService.getPostsByCategory(categoryId);

        return new ResponseEntity<>(postsByCategory,HttpStatus.OK);
    }
    //get All Posts
    @GetMapping("all/posts")
    public ResponseEntity<List<PostDto>> getAllPosts(){
        List<PostDto> allPost = postService.getAllPost();
        return new ResponseEntity<>(allPost,HttpStatus.OK);
    }

    //http://localhost:9090/api/posts?pageNumber=1&pageSize=4&sortBy=postId&sortDir=desc
    // pagination
    @GetMapping("/posts")
    public ResponseEntity<
            PostResponse
            > getAllPostsByPagination(
            @RequestParam(value ="pageNumber",defaultValue = AppConst.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConst.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConst.SORT_BY,required = false)String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConst.SORT_DIR,required = false)String sortDir
    ){

        PostResponse
                allPost = postService.getAllPostByPagination(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(allPost,HttpStatus.OK);
    }

    @GetMapping("/pagination/user/{userId}")
    public ResponseEntity<PostResponse> getPostByUserPagination(
            @PathVariable Integer userId,
            @RequestParam(value = "pageNumber",defaultValue = AppConst.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConst.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConst.SORT_BY,required = false)String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConst.SORT_DIR,required = false)String sortDir
            ){
        PostResponse postsByUserPagination = postService.getPostsByUserPagination(userId, pageNumber, pageSize,sortBy,sortDir);
        return new ResponseEntity<>(postsByUserPagination,HttpStatus.OK);
    }

    @GetMapping("/pagination/category/{categoryId}")
    public ResponseEntity<PostResponse> getPostByCategoryPagination(
            @PathVariable Integer categoryId,
            @RequestParam(value = "pageNumber",defaultValue = AppConst.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConst.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConst.SORT_BY,required = false)String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConst.SORT_DIR,required = false)String sortDir
    ){
        PostResponse postsByCategoryPagination = postService.getPostsByCategoryPagination(categoryId, pageNumber, pageSize,sortBy,sortDir);
        return new ResponseEntity<>(postsByCategoryPagination,HttpStatus.OK);
    }

    //get post by id
    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Integer postId){
        PostDto byPostId = this.postService.getByPostId(postId);
        return new ResponseEntity<>(byPostId,HttpStatus.OK);
    }

    //Delete post
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId){
        this.postService.deletePost(postId);
        return new ResponseEntity<>(new ApiResponse("Post Deleted Successfully",true),HttpStatus.OK);
    }

    //Update post
    @PutMapping("/update/{postId}")
    public ResponseEntity<PostDto> update(@RequestBody PostDto postDto,
                                          @PathVariable Integer postId){
        PostDto postDto1 = this.postService.updatePost(postDto, postId);
        return new ResponseEntity<>(postDto1,HttpStatus.OK);

    }

    //Search by Title
    @GetMapping("/search/post/{keyword}")
    public ResponseEntity<List<PostDto>> searchByTitle(@PathVariable String keyword){
        List<PostDto> postDtos = this.postService.searchPosts(keyword);
        return new ResponseEntity<>(postDtos,HttpStatus.OK);
    }


    //Post Image
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(
            @RequestParam("image")MultipartFile image,
            @PathVariable Integer postId
            ) throws IOException {
        //Throw exception if ID not found
        PostDto byPostId = this.postService.getByPostId(postId);

        String fileName = this.fileService.uploadImage(path, image);

        byPostId.setImageName(fileName);
        PostDto updatedPost = this.postService.updatePost(byPostId, postId);
        return new ResponseEntity<>(updatedPost,HttpStatus.OK);
    }

    //Download Image
    @GetMapping(value = "/post/profiles/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadFile(@PathVariable String imageName,
                             HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }


}

