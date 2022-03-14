package com.example.eblog.common.lang;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class Consts {
    @Value("${file.upload.dir}")
    private String uploadDir;
}
