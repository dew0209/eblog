package com.example.eblog.vo;

import com.example.eblog.entity.MComment;
import lombok.Data;

@Data
public class MCommentVo extends MComment {
    private Long authorId;
    private String authorName;
    private String authorAvatar;

}
