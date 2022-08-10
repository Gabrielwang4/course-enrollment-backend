package com.mycompany.myapp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_course")
@Data
@NoArgsConstructor
public class UserCourse {
    public UserCourse(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne //多对一
    private User user;

    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @ManyToOne //多对一
    private Course course;
}
