package com.example.eblog.vo;

import com.example.eblog.entity.MUserMessage;
import lombok.Data;

@Data
public class MUserMessageVo extends MUserMessage {
    private String toUserName;
    private String fromUserName;
    private String postTitle;
    private String commentContent;
}
