package com.example.tennisapp.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tennisapp.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
