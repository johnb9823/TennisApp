package com.example.tennisapp.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tennisapp.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시물의 댓글 + 작성자(Member)를 한 번에 조회
    @Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.board.boardId = :boardId AND c.parentComment IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findAllByBoardWithMember(Long boardId);
}
