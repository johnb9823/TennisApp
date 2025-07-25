package com.example.tennisapp.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tennisapp.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
