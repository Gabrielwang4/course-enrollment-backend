package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.CourseService;
import com.mycompany.myapp.service.dto.CourseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourseController {
    private CourseService courseService;

    /**
     * 1. Requirement: 实现学生选课 - operation
     * 2. Http method: POST(create)
     * 3. URL: /student/course/{courseName}
     * 4. Http status code: 200, 201
     * 5. Request body:{courseName}
     * 6. Response body: None
     * 7. Request Header: JWT Token
     * http request -> URL + HTTP method => Java method映射
     */
    @PostMapping(path = "/student/course/{courseName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void enrollCourse(@PathVariable String courseName) {
        String userName = getUserName();
        this.courseService.enrollCourse(userName, courseName);
    }

    /**
     * 1. Requirement: 列出所有学校课程 - operation
     * 2. Http method: GET
     * 3. URL: /courses
     * 4. Http status code: 200
     * 5. Request body: None
     * 6. Response body: List<CourseDTO>
     * 7. Request Header: JWT Token
     */
    @GetMapping(path = "/courses")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    /**
     * 1. Requirement: 列出学生已选课程 - operation
     * 2. Http method: GET
     * 3. URL: /student/courses
     * 4. Http status code: 200
     * 5. Request body: None
     * 6. Response body: List<CourseDTO>
     * 7. Request Header: JWT Token
     */
    @GetMapping(path = "/student/courses")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<CourseDTO> getEnrolledCourses() {
        String userName = getUserName();
        return courseService.getEnrolledCourses(userName);
    }

    /**
     * 1. Requirement: 提供学生drop课程 - operation
     * 2. Http method: DELETE
     * 3. URL: /student/course/{courseName}
     * 4. Http status code: 200, 204
     * 5. Request body: courseName(pathVariable)
     * 6. Response body: None
     * 7. Request Header: JWT Token
     */
    @DeleteMapping(path = "/student/course/{courseName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dropCourse(@PathVariable String courseName) {
        String userName = getUserName();
        courseService.dropCourse(userName, courseName);
    }



    /**
     * Extract username from JWT token
     * @return username (String)
     */
    private String getUserName() {
        return SecurityUtils.getCurrentUserLogin().orElseThrow(() -> {
            throw new UsernameNotFoundException("Username not found");
        });
    }

}
