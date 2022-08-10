package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.UserCourse;
import com.mycompany.myapp.repository.CourseRepository;
import com.mycompany.myapp.repository.UserCourseRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.CourseDTO;
import com.mycompany.myapp.service.mapper.CourseMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private UserCourseRepository userCourseRepository;
    private CourseMapper courseMapper;

    public void enrollCourse(String userName, String courseName) {
        // 1+2 check User & Course exist
        UserCourse userCourse = getUserCourse(userName, courseName);
        // 3. Check if UserCourse not exists
        Optional<UserCourse> optionalUserCourse = userCourseRepository.findOneByUserAndCourse(userCourse.getUser(), userCourse.getCourse());
        optionalUserCourse.ifPresent(existingUserCourse -> {
            throw new IllegalArgumentException("UserCourse already exists: " + existingUserCourse.toString());
        });
        // 4. Save new relationship (userCourse) into user_course table
        userCourseRepository.save(userCourse);
    }

    /**
     * Helper function to check:
     * 1. Check if User exists -> I want it exists
     * 2. Check if Course exists -> I want it exists
     * 3. Return such UserCourse with User and Course
     * @param userName
     * @param courseName
     * @return
     */
    private UserCourse getUserCourse(String userName, String courseName) {
        //1. User exists?
        Optional<User> optionalUser = userRepository.findOneByLogin(userName);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("No such user: " + userName));
        //2. Course exists?
        Optional<Course> optionalCourse = courseRepository.findOneByCourseName(courseName);
        Course course = optionalCourse.orElseThrow(() -> new IllegalArgumentException("No such course: " + courseName));
        //3. Return such UserCourse.
        return new UserCourse(user, course);
    }

    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
//        List<CourseDTO> courseDTOList = new ArrayList<>();
//        for (Course course: courses) {
//            courseDTOList.add(courseMapper.convert(course));
//        }
//        return courseDTOList;
        // Java 8 特性 stream
        return courses.stream().map(course -> courseMapper.convert(course)).collect(Collectors.toList());
    }

    public List<CourseDTO> getEnrolledCourses(String userName) {
        //1. User exists?
        Optional<User> optionalUser = userRepository.findOneByLogin(userName);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("No such user: " + userName));
        List<UserCourse> userCourseList = userCourseRepository.findAllByUser(user);
//        List<CourseDTO> courseDTOList = new ArrayList<>();
//        for (UserCourse userCourse: userCourseList) {
//            courseDTOList.add(courseMapper.convert(userCourse.getCourse()));
//        }
//        return courseDTOList;
        return userCourseList.stream()
                .map(userCourse -> userCourse.getCourse())
                .map(course -> courseMapper.convert(course))
                .collect(Collectors.toList());
    }

    public void dropCourse(String userName, String courseName) {
        // 1+2 check User & Course exist
        UserCourse userCourse = getUserCourse(userName, courseName);
        // 3. Delete such userCourse
        userCourseRepository.deleteByUserAndCourse(userCourse.getUser(), userCourse.getCourse());
    }
}
