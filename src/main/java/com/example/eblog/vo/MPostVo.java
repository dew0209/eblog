package com.example.eblog.vo;

import com.example.eblog.entity.MPost;
import lombok.Data;

@Data
public class MPostVo extends MPost {
    private Long authorId;
    private String authorName;
    private String authorAvatar;

    private String categoryName;
}
