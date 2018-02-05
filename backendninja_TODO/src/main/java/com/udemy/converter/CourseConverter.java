package com.udemy.converter;

import org.springframework.stereotype.Component;

import com.udemy.entity.Course;
import com.udemy.model.CourseModel;

@Component("courseConverter")
public class CourseConverter {
    
    // Entity --> Model
    public CourseModel entity2model(
        Course course){
        return CourseModel.builder()
                .name(course.getName())
                .description(course.getDescription())
                .price(course.getPrice())
                .hours(course.getHours())
                .build();
    }
    
    // Model --> Entity
    public Course model2entity(
        CourseModel courseModel){
        return Course.builder()
                .name(courseModel.getName())
                .description(courseModel.getDescription())
                .price(courseModel.getPrice())
                .hours(courseModel.getHours())
                .build();
    }
}
