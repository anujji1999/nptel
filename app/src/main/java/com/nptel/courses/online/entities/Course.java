package com.nptel.courses.online.entities;


import java.io.Serializable;

import androidx.annotation.Keep;

@Keep
public class Course implements Serializable {


    private String courseID;
    private String courseName;
    private String courseImage;
    private Integer courseImageResourceID;

    public Course() {

    }


    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public Integer getCourseImageResourceID() {
        return courseImageResourceID;
    }

    public void setCourseImageResourceID(Integer courseImageResourceID) {
        this.courseImageResourceID = courseImageResourceID;
    }
}
