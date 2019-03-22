package com.yagnenkoff.demo.controller;


import com.yagnenkoff.demo.domain.Folder;
import com.yagnenkoff.demo.exception.NameException;
import com.yagnenkoff.demo.exception.NotFoundException;
import com.yagnenkoff.demo.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
public class FolderController {

    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }


    //create dir
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus (value = HttpStatus.OK)
    public void createFolder(String name, Long parentId) throws NameException {
        folderService.create(name, parentId);
    }

    //delete dir
    @ResponseStatus (value = HttpStatus.OK)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteFolder(Long id){
        folderService.deleteById(id);
    }

    //rename dir
    @ResponseStatus (value = HttpStatus.OK)
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public void editFolder(Long id, String name) throws NameException {
        folderService.edit(id, name);
    }

    //open dir by id and return list folders in this dir
    @ResponseBody
    @RequestMapping(value = "/open", method = RequestMethod.POST)
    public List<Folder> getFolder(Long id) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return folderService.findByParentId(id);
    }

    //open dir by path and return list folders in this dir
    @ResponseBody
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    public List<Folder> getFolder(String path) throws NotFoundException {
        Long id = folderService.selectByPath(path);
        return folderService.findByParentId(id);
    }

    //change path dir
    @ResponseBody
    @RequestMapping(value = "/relocate", method = RequestMethod.PUT)
    public String relocate(Long id, Long parentId) throws NotFoundException {
        return folderService.relocate(id, parentId);
    }

    //start app
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model){
        Long id = 1L;
        List<Folder> folders = folderService.findByParentId(id);
        model.addAttribute("folders", folders);
        return "index";
    }
}
