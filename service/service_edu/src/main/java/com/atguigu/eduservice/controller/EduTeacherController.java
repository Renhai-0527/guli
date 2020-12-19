package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;

import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-27
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    /*public List<EduTeacher> list(){
        return teacherService.list(null);
    }*/
    //1 查询讲师所有数据
    public R findAllTeacher(){
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(
            @ApiParam(name = "id",value = "讲师ID",required = true)
            @PathVariable String id
    ){
        teacherService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "分页查询讲师")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(
            @ApiParam(name = "current",value = "当前页面",required = true)
            @PathVariable Long current,

            @ApiParam(name = "limit",value = "每页记录数",required = true)
            @PathVariable Long limit
        ){
        //创建page对象
        Page<EduTeacher>  pageTeacher = new Page<>(current,limit);


        /*try {
            int i=1/0;
        }catch (Exception e){
            //执行自定义异常
            //不会自动抛出，要手动抛出
            throw new GuliException(20001,"执行了自定义异常处理");
        }*/


        teacherService.page(pageTeacher,null);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();

        return R.ok().data("total",total).data("rows",records);
    }

    //4.条件查询带分页的方法
    @ApiOperation(value = "条件查询带分页")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(
            @ApiParam(name = "current",value = "当前页",required = true)@PathVariable long current,
            @ApiParam(name = "limit",value = "每页记录数",required = true)@PathVariable long limit,
            @RequestBody(required = false) TeacherQuery teacherQuery
     ){
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        //多条件组合查询 动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        System.out.println(name);
        //判断条件值是否为空，如果不为空则拼接

        //注意 name有可能有中文，所有要在数据库指定编码格式
        if(!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }

        wrapper.orderByDesc("gmt_create");
        teacherService.page(pageTeacher,wrapper);

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合

        return R.ok().data("total",total).data("rows",records);
    }

    //添加讲师接口
    @ApiOperation(value = "添加讲师接口")
    @PostMapping("addTeacher")
    public R addTeacher(
            @RequestBody EduTeacher eduTeacher
    ){
        boolean save = teacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //根据讲师id进行查询
    @ApiOperation(value = "根据讲师id进行查询")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(
            @ApiParam(name = "id",value = "讲师id",required = true)@PathVariable String id
    ){
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    //修改讲师功能
    @ApiOperation(value = "根据id修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(
            @RequestBody EduTeacher eduTeacher
    ){
        boolean flag = teacherService.updateById(eduTeacher);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

}

