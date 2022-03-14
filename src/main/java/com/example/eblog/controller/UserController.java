package com.example.eblog.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.eblog.common.lang.Result;
import com.example.eblog.entity.MPost;
import com.example.eblog.entity.MUser;
import com.example.eblog.entity.MUserMessage;
import com.example.eblog.shiro.AccountProfile;
import com.example.eblog.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class UserController extends BaseController {
    @Autowired
    UploadUtil uploadUtil;
    @GetMapping("/user/home")
    public String home(){
        MUser user = userService.getById(getProFileId());
        List<MPost> posts = postService.list(new QueryWrapper<MPost>().eq("user_id", getProFileId())
                //.gt("created", DateUtil.offsetDay(new Date(), -30))
                .orderByDesc("created"));
        req.setAttribute("user",user);
        req.setAttribute("posts",posts);
        return "/user/home";
    }
    @GetMapping("/user/set")
    public String set(){
        MUser user = userService.getById(getProFileId());
        req.setAttribute("user",user);
        return "/user/set";
    }
    @ResponseBody
    @PostMapping("/user/set")
    public Result doSet(MUser user){
        if(StrUtil.isNotBlank(user.getAvatar())){
            MUser temp = userService.getById(getProFileId());
            temp.setAvatar(user.getAvatar());
            userService.updateById(temp);
            AccountProfile proFile = getProFile();
            proFile.setAvatar(user.getAvatar());
            return Result.success().action("/user/set");
        }
        if(StrUtil.isBlank(user.getUsername())){
            return Result.fail("昵称不能为空");
        }
        int count = userService.count(new QueryWrapper<MUser>()
                .eq("username", getProFile().getUsername())
                .ne("id", getProFileId())
        );
        if (count > 0)return Result.fail("该昵称已经被占用");
        MUser temp = userService.getById(getProFileId());
        temp.setUsername(user.getUsername());
        temp.setGender(user.getGender());
        temp.setSign(user.getSign());
        userService.updateById(temp);
        AccountProfile proFile = getProFile();
        proFile.setUsername(temp.getUsername());
        proFile.setSign(temp.getSign());
        return Result.success().action("/user/set");
    }
    @ResponseBody
    @RequestMapping("/user/upload")
    public Result uploadAvatar(@RequestParam(value = "file") MultipartFile file) throws IOException {
        return uploadUtil.upload(UploadUtil.type_avatar,file);
    }

    @ResponseBody
    @PostMapping("/user/repass")
    public Result repass(String nowpass, String pass, String repass) {
        if(!pass.equals(repass)) {
            return Result.fail("两次密码不相同");
        }

        MUser user = userService.getById(getProFileId());

        String nowPassMd5 = SecureUtil.md5(nowpass);
        if(!nowPassMd5.equals(user.getPassword())) {
            return Result.fail("密码不正确");
        }

        user.setPassword(SecureUtil.md5(pass));
        userService.updateById(user);

        return Result.success().action("/user/set#pass");

    }

    @GetMapping("/user/index")
    public String index(){

        return "/user/index";
    }

    @ResponseBody
    @GetMapping("/user/public")
    public Result userPublic(){
        IPage page = postService.page(getPage(), new QueryWrapper<MPost>()
                .eq("user_id", getProFileId())
                .orderByDesc("created")
        );
        return Result.success(page);
    }

    @ResponseBody
    @GetMapping("/user/collection")
    public Result collection(){
        IPage page = postService.page(getPage(), new QueryWrapper<MPost>()
                .inSql("id","select post_id from m_user_collection where user_id = " + getProFileId())
        );
        return Result.success(page);
    }
    @GetMapping("/user/mess")
    public String mess(){
        IPage page = messageService.paging(getPage(), new QueryWrapper<MUserMessage>()
                .eq("to_user_id", getProFileId())
                .orderByDesc("created")
        );
        req.setAttribute("pageData",page);
        return "/user/mess";
    }
    @ResponseBody
    @RequestMapping("/msg/remove")
    public Result msgRemove(Long id,@RequestParam(defaultValue = "false") Boolean all){
        boolean remove = messageService.remove(new QueryWrapper<MUserMessage>()
                .eq("to_user_id", getProFileId())
                .eq(!all, "id", id)
        );

        return remove ? Result.success(null) : Result.fail("删除失败");
    }

    @ResponseBody
    @RequestMapping("/msg/nums/")
    public Map msgNums(){
        int count = messageService.count(new QueryWrapper<MUserMessage>()
                .eq("to_user_id", getProFileId())
                .eq("status","0")
        );
        return MapUtil.builder("status",0).put("count",count).build();
    }

}
