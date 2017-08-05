package com.mktj.cn.web.po;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 保存上传的文件
 */
@Entity
@Table(name = "t_task_info")
public class TaskInfo implements Serializable {

    public static final int RAW_FILE_READY = 1;

    public static final int FILE_SPLITED = 2;

    public static final int FILE_FORMATTED = 3;

    public static final int FILE_TOKENIZED = 4;

    public static final int FILE_CODED = 5;

    public static final int FILE_IS_PROCESSING = 99;

    public static final int FILE_IS_PROCESSED = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_size", nullable = false, length = 20)
    private Long fileSize;

    @NotEmpty
    @Column(name = "file_type", nullable = false, length = 10)
    private String fileType;

    @Column(name = "status", nullable = false, length = 10)
    private Integer status = RAW_FILE_READY;


    @Column(name = "update_time", nullable = false, length = 10)
    private Date updateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    @Column(name = "signature", nullable = false, length = 255)
    private String signature;

    public TaskInfo() {
        super();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
