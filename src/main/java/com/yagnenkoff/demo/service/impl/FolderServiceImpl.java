package com.yagnenkoff.demo.service.impl;

import com.yagnenkoff.demo.domain.Folder;
import com.yagnenkoff.demo.exception.NameException;
import com.yagnenkoff.demo.exception.NotFoundException;
import com.yagnenkoff.demo.repos.FolderRepo;
import com.yagnenkoff.demo.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class FolderServiceImpl implements FolderService {
    private final FolderRepo folderRepo;

    @Autowired
    public FolderServiceImpl(FolderRepo folderRepo) {
        this.folderRepo = folderRepo;
    }

    //create dir in database
    @Override
    public void create(String name, Long parent_id) throws NameException {
        if(name.matches("\\w+")) {
            Folder folder = Folder.create(name, parent_id);
            if (folderRepo.findByNameAndParentId(name, folder.getParentId()) == null) {
                folderRepo.save(folder);
            } else {
                throw new NameException("Incorrect input");
            }
        } else {
            throw new NameException("Incorrect input");
        }
    }

    //delete dir in database
    @Override
    @Transactional
    public void deleteById(Long id) {
        folderRepo.deleteById(id);
    }

    //rename(update) dir in database
    @Override
    public void edit(Long id, String name) throws NameException {
        if(name.matches("\\w+")) {
            Optional<Folder> optionalFolder = folderRepo.findById(id);
            if (optionalFolder.isPresent()) {
                Folder folder = optionalFolder.get();
                if (folderRepo.findByNameAndParentId(name, folder.getParentId()) == null) {
                    folder.setName(name);
                    folderRepo.save(folder);
                } else {
                    throw new NameException("Incorrect input");
                }
            }
        } else {
            throw new NameException("Incorrect input");
        }
    }

    //open dir by path
    @Override
    public Long selectByPath(String path) throws NotFoundException {
        Folder folder;
        Long parentId = 1L;
        path = path.substring(4);
        if (path.isEmpty()){
            return parentId;
        } else {
            if (path.charAt(0)!='/') {
                throw new NotFoundException("Incorrect path");
            }
        }
        while(!path.isEmpty()){
            path = path.substring(1);
            int i=0;
            StringBuilder folderName = new StringBuilder();
            while((path.charAt(i)!='/')){
                folderName.append(path.charAt(i));
                path = path.substring(1);
                if (path.isEmpty()){
                    break;
                }
            }
            folder = folderRepo.findByNameAndParentId(folderName.toString(), parentId);
            parentId = folder.getId();
        }
        return parentId;
    }

    //open dir by parent id
    @Override
    public List<Folder> findByParentId(Long parentId) {
        return folderRepo.findByParentId(parentId);
    }


    //relocate dir
    @Override
    public String relocate(Long id, Long parentId) throws NotFoundException {
        String name;
        Long pid = parentId;
        while(pid!=1L) {
            if (id.equals(pid)) {
                throw new NotFoundException("Incorrect path");
            }
            if (folderRepo.findById(pid).isPresent()) {
                pid = folderRepo.findById(pid).get().getParentId();
            } else {
                throw new NotFoundException("Not found this directory");
            }
        }

        Optional<Folder> optionalFolder = folderRepo.findById(id);
        Folder folder;
        if (optionalFolder.isPresent()) {
             folder = optionalFolder.get();
        } else {
            throw new NotFoundException("Not found this directory");
        }
        if (folderRepo.findByNameAndParentId(folder.getName(), parentId)==null) {
            name = folder.getName();
            folder.setParentId(parentId);
            folderRepo.save(folder);
            return name;
        }
        else {
            name = folder.getName() + "_1"; //if name conflict example: name -> name_1
            folder.setParentId(parentId);
            folder.setName(name);
            folderRepo.save(folder);
            return name;
        }
    }


}
