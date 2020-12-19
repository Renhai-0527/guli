package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-27
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    //获取上传文件，把文件的内容读到
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        //上传过来的excel文件
        subjectService.saveSubject(file,subjectService);
        return R.ok();
    }

    //添加一个subject 作为一个测试 在执行subjectService.save(eduSubject)后，那么eduSubject会重新赋值
    /*@GetMapping
    public R add(){

        EduSubject eduSubject = new EduSubject();
        eduSubject.setParentId("0");
        eduSubject.setTitle("aaa-test");
        subjectService.save(eduSubject);

        System.out.println(eduSubject.getId());

        return R.ok();
    }*/

    //课程列表分类
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        //list集合泛型是一级分类
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }

}

