package com.nptel.courses.online.entities;

import java.io.Serializable;

import androidx.annotation.Keep;

//@Entity(tableName = "module")
@Keep
public class Module implements Serializable {

    //    @NonNull
//    @PrimaryKey
    private String moduleID;

    private String moduleName;
    private String moduleDescription;
    private Long numberOfVideos;
    private String thumbnail;
    private String courseID;

    public Module() {
        moduleID = "123";
        moduleName = "Name";
    }

    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public Long getNumberOfVideos() {
        return numberOfVideos;
    }

    public void setNumberOfVideos(Long numberOfVideos) {
        this.numberOfVideos = numberOfVideos;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }
}
