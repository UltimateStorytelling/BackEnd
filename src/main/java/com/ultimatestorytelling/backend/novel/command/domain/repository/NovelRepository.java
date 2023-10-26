package com.ultimatestorytelling.backend.novel.command.domain.repository;

import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.NovelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelRepository extends JpaRepository<NovelEntity, Long> {

}
