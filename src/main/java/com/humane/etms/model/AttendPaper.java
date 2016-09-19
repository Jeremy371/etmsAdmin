package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"attendCd", "paperCd"})})
@Data
public class AttendPaper implements Serializable {
    @Id @GeneratedValue private Long _id;

    @ManyToOne @JoinColumn(name = "attendCd", nullable = false) private Attend attend;
    @Column(nullable = false) private String paperCd;

    @Column(columnDefinition = "int default 1", nullable = false) private int paperNo;
    @ManyToOne @JoinColumn(name = "examineeCd", nullable = false) private Examinee examinee;
    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDttm;

    private String oldPaperCd;
}
