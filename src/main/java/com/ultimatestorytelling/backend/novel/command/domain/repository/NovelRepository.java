package com.ultimatestorytelling.backend.novel.command.domain.repository;


import com.ultimatestorytelling.backend.novel.command.application.dto.NovelRequestDto;
import com.ultimatestorytelling.backend.novel.command.application.dto.NovelResponseDto;
import com.ultimatestorytelling.backend.novel.command.application.service.NovelService;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NovelRepository extends JpaRepository<Novel, Long> {

}
