package com.everr.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.everr.reggie_take_out.common.R;
import com.everr.reggie_take_out.entiy.User;
import com.everr.reggie_take_out.service.UserService;
import com.everr.reggie_take_out.utils.SMSUtils;
import com.everr.reggie_take_out.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送手机验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 获取手机号码
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);

            //生生随机验证码

            //调用阿里云APi完成短信发送
//        SMSUtils.sendMessage("",phone,code);


            //把生成的验证码保存到session
            session.setAttribute(phone, code);
            return R.success("手机验证码发送成功");
        }


        return R.error("短信发送失败");
    }

    /**
     * 登录
     * @param map
     * @param session
     * @return
     */

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码

        String code = map.get("code").toString();

        //从session获取验证码
        Object attribute = session.getAttribute(phone);

        // 进行比对，成功就登录成功

        if (attribute != null && attribute.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();


            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);

            }
            session.setAttribute("user",user.getId());
            return R.success(user);

        }

        // 判断当前用户是否注册，若无，自动注册


        return R.error("登录失败");
    }







}
