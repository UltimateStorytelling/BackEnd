package com.ultimatestorytelling.backend.novel.command.domain.repository;


import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NovelRepository extends JpaRepository<Novel, Long> {

    //삭제안된 전체 소설 리스트 페이징 조회
    Page<Novel> findNovelByNovelIsDeletedFalse(Pageable pageable);

    //소설 번호로 소설 조회
    Novel findByNovelNo(Long novelNo);
}
