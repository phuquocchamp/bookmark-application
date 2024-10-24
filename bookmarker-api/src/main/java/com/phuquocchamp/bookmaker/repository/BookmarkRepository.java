package com.phuquocchamp.bookmaker.repository;

import com.phuquocchamp.bookmaker.domain.Bookmark;
import com.phuquocchamp.bookmaker.dto.BookmarkDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("select new com.phuquocchamp.bookmaker.dto.BookmarkDTO(b.id, b.title, b.url, b.createAt) from Bookmark b")
    Page<BookmarkDTO> findBookmarks(Pageable pageable);
}
