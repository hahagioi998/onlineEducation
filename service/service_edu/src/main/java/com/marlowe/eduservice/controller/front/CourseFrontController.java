package com.marlowe.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marlowe.commonutils.R;
import com.marlowe.eduservice.entity.EduCourse;
import com.marlowe.eduservice.entity.frontvo.CourseFrontVo;
import com.marlowe.eduservice.service.EduCourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: onlineEducation
 * @description:
 * @author: Marlowe
 * @create: 2021-07-27 20:45
 **/
@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    /**
     * 条件查询带分页查询课程
     *
     * @return
     */
    @ApiOperation("条件查询带分页查询课程")
    @PostMapping("getFrontCourseList/{pageNo}/{pageSize}")
    public R getFrontCourseList(@PathVariable long pageNo, @PathVariable long pageSize, @RequestBody(required = false) CourseFrontVo courseFrontVo) {
        // 创建一个page对象
        Page<EduCourse> pageCourse = new Page<>(pageNo, pageSize);

        // 构造条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        // 判断条件值是否为空，不为空拼接
        // 一二级分类排序
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) {
            wrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());
            if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())) {
                wrapper.eq("subject_id", courseFrontVo.getSubjectId());
            }
        }

        // 销量排序
        if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())) {
            wrapper.orderByDesc("buy_count");
        }else {
            wrapper.orderByAsc("buy_count");
        }

        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) {
            wrapper.orderByDesc("gmt_create");
        }else {
            wrapper.orderByAsc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) {
            wrapper.orderByDesc("price");
        }else {
            wrapper.orderByAsc("price");
        }

        courseService.page(pageCourse, wrapper);

        long total = pageCourse.getTotal();
        List<EduCourse> records = pageCourse.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

}
