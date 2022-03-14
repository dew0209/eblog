package com.example.eblog.shiro;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccountProfile implements Serializable {
    private String username;
    private String email;
    private String avatar;
    private String gender;
    private Date created;
    private Long id;
    private String sign;
    public String getSex(){
        return "0".equals(gender)?"女":"男";
    }
}
