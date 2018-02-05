package com.udemy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.udemy.converter.CourseConverter;
import com.udemy.entity.Course;
import com.udemy.model.CourseModel;
import com.udemy.repository.CourseJPARepository;
import com.udemy.service.CourseService;

@Service("courseServiceImpl")
public class CourseServiceImpl implements CourseService {
    
    private static final Log LOG = LogFactory.getLog(CourseServiceImpl.class);
    
    @Autowired
    @Qualifier("courseJpaRepository")
    private CourseJPARepository courseJPARepository;
    
    @Autowired
    @Qualifier("courseConverter")
    private CourseConverter courseConverter;
    
    @Override
    public List<CourseModel> listAllCourses(){
        LOG.info("Call: " + "listAllCourses()");
        
        List<CourseModel> courseModelList = new ArrayList<>();
        List<Course> courseList = courseJPARepository.findAll();
        
        for (Course course : courseList) {
            courseModelList.add(CourseModel.builder()
                    .name(course.getName())
                    .description(course.getDescription())
                    .price(course.getPrice())
                    .hours(course.getHours())
                    .build());
        }
        return courseModelList;
    }
    
    @Override
    public CourseModel addCourse(
        CourseModel courseModel){
        LOG.info("Call: " + "addCourse()");
        
        Course course = courseConverter.model2entity(courseModel);
        return courseConverter.entity2model(courseJPARepository.save(course));
    }
    
    @Override
    public int removeCourse(
        int id){
        courseJPARepository.delete(id);
        return 0;
    }
    
    @Override
    public CourseModel updateCourse(
        CourseModel courseModel){
        Course course = courseConverter.model2entity(courseModel);
        return courseConverter.entity2model(courseJPARepository.save(course));
    }
    
}
