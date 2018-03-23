package com.jhon.rain.api.controller;

import com.jhon.rain.api.common.CommonConstants;
import com.jhon.rain.api.common.ResultMsg;
import com.jhon.rain.api.interceptor.UserContext;
import com.jhon.rain.api.model.UserDO;
import com.jhon.rain.api.service.AccountsService;
import com.jhon.rain.api.service.AgencyService;
import com.jhon.rain.api.util.UserHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>功能描述</br>账号体系的控制器</p>
 *
 * @author jiangy19
 * @version v1.0
 * @projectName rainbow-house-v2
 * @date 2018/3/23 9:37
 */
@Slf4j
@Controller
@RequestMapping("accounts")
public class AccountsAPIController {

  @Autowired
  private AccountsService accountsService;

  @Autowired
  private AgencyService agencyService;

  /** 1.注册 **/
  /**
   * <pre>用户注册</pre>
   *
   * @param account  用户信息
   * @param modelMap
   * @return
   */
  @RequestMapping("/register")
  public String register(UserDO account, ModelMap modelMap) {
    if (account == null || account.getName() == null) {
      modelMap.put("agencyList", agencyService.getAllAgency());
      return "user/accounts/register";
    }
    /** 用户验证 **/
    ResultMsg resultMsg = UserHelper.validate(account);

    if (resultMsg.isSuccess()) {
      boolean exist = accountsService.isExist(account.getEmail());
      if (!exist) {
        accountsService.addAccount(account);
        modelMap.put("email", account.getEmail());
        return "user/accounts/registerSubmit";
      } else {
        return "redirect:accounts/register?" + ResultMsg.errorMsg("邮箱已被占用").asUrlParams();
      }
    } else {
      return "redirect:accounts/register?" + ResultMsg.errorMsg("参数错误").asUrlParams();
    }
  }

  /** 2.激活 **/
  /**
   * <pre>用户激活</pre>
   *
   * @param key 激活用户的key
   * @return
   */
  @GetMapping("/verify")
  public String activateCodeVerify(@RequestParam(name = "key", required = true) String key) {
    boolean result = accountsService.enable(key);
    if (result) {
      return "redirect:/index?" + ResultMsg.successMsg("激活成功").asUrlParams();
    } else {
      return "redirect:/accounts/register?" + ResultMsg.errorMsg("激活失败,请确认链接是否过期");
    }
  }

  /** 3.登录 **/
  /**
   * <pre>用户登录</pre>
   *
   * @param email    邮箱
   * @param password 密码
   * @param target   目标
   * @param request  请求对象
   * @return 视图名称
   */
  @RequestMapping("/signin")
  public String signIn(@RequestParam(name = "email", required = false) String email,
                       @RequestParam(name = "password", required = false) String password,
                       @RequestParam(name = "target", required = false) String target,
                       HttpServletRequest request) {
    if (email == null || password == null) {
      request.setAttribute("target", target);
      return "/user/accounts/signin";
    }
    /** 用户的认证 **/
    UserDO user = accountsService.auth(email, password);
    if (user == null) {
      return "redirect:/accounts/signin?target=" + target + "&email=" + email + "&"
              + ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
    } else {
      HttpSession session = request.getSession(true);
      session.setAttribute(CommonConstants.USER_ATTRIBUTE, user);
      return StringUtils.isNoneBlank(target) ? "redirect:" + target : "redirect:/index";
    }
  }

  /**
   * 4.注销
   **/
  /**
   * <pre>注销登录</pre>
   *
   * @return
   */
  @GetMapping("/logout")
  public String logout() {
    UserDO user = UserContext.getUser();
    accountsService.logout(user.getToken());
    return "redirect:/index";
  }

}
