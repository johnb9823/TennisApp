package com.example.tennisapp.domain.board.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tennisapp.domain.board.entity.Board;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 게시물 + 작성자(Member)를 한 번의 쿼리로 가져오기
    @Query("SELECT b FROM Board b JOIN FETCH b.member ORDER BY b.createdAt DESC")
    List<Board> findAllWithMember();

    // 페이징용 (Spring Data JPA Pageable 사용 시)
    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member ORDER BY b.createdAt DESC",
            countQuery = "SELECT count(b.boardId) FROM Board b")
    List<Board> findAllWithMemberPaged(@Param("offset") int offset, @Param("limit") int limit);
}
