package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.service.dto.CourseDTO;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Convert entity(model) class Course to CourseDTO
 */
@Service
public class CourseMapper {
    public CourseDTO convert(Course course) {
        Random random = new Random();
        random.nextInt();
        return CourseDTO.builder()
            .courseName(course.getCourseName())
            .courseContent(course.getCourseContent())
            .teacherId(course.getTeacherId())
            .courseLocation(course.getCourseLocation())
            .build();
    }
}
