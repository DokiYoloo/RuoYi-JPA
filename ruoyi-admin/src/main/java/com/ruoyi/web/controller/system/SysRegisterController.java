package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.service.SysConfigService;

/**
 * 注册验证
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
public class SysRegisterController extends BaseController {
    private final SysRegisterService registerService;
    private final SysConfigService configService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return ResponseEntity.failed("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? ResponseEntity.success() : ResponseEntity.failed(msg);
    }
}
