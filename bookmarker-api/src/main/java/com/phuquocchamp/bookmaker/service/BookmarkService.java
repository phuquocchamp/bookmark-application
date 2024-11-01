package com.phuquocchamp.bookmaker.service;

import com.phuquocchamp.bookmaker.domain.Bookmark;
import com.phuquocchamp.bookmaker.dto.BookmarkDTO;
import com.phuquocchamp.bookmaker.dto.BookmarksDTO;
import com.phuquocchamp.bookmaker.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository repository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public BookmarksDTO getBookmarks(Integer page){
        int pageNo = page < 1 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.Direction.DESC, "createAt");
        Page<BookmarkDTO> bookmarkDTOs = repository.findBookmarks(pageable);
        return new BookmarksDTO(bookmarkDTOs);
    }

}
