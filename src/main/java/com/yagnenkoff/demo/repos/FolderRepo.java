package com.yagnenkoff.demo.repos;

import com.yagnenkoff.demo.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepo extends JpaRepository<Folder, Long> {

    void deleteById(Long id);

    Folder findByNameAndParentId(String name, Long parentId);

    List<Folder> findByParentId(Long parentId);

}
