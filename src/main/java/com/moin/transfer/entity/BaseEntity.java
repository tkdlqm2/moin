package com.moin.transfer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseEntity {
    @Comment("생성일시")
    @CreationTimestamp
    @Column(updatable = false, columnDefinition = "TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)", name = "creation_dtm")
    private LocalDateTime creationDtm;

    @Comment("생성ID")
    @Column(updatable = false, columnDefinition = "VARCHAR(10) NOT NULL DEFAULT 'SYSTEM'", name = "creation_id")
    private String creationId = "SYSTEM";

    @Comment("수정일시")
    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)", name="modify_dtm")
    private LocalDateTime modifyDtm;

    @Comment("수정ID")
    @Column(columnDefinition = "VARCHAR(10) NOT NULL DEFAULT 'SYSTEM'", name="modify_id")
    private String modifyId = "SYSTEM";
}


