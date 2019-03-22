package com.yagnenkoff.demo.service;

import com.yagnenkoff.demo.domain.Folder;
import com.yagnenkoff.demo.exception.NameException;
import com.yagnenkoff.demo.exception.NotFoundException;

import java.util.List;

public interface FolderService {
    void create(String name, Long parent_id) throws NameException;

    void deleteById(Long id);

    void edit(Long id, String name) throws NameException;

    Long selectByPath(String path) throws NotFoundException;

    List<Folder> findByParentId(Long parentId);

    String relocate(Long id, Long parentId) throws NotFoundException;
}
