package com.jhon.rain.user.common;

/**
 * <p>功能描述</br>返回码</p>
 *
 * @author jiangy19
 * @version v1.0
 * @projectName rainbow-house-v2
 * @date 2018/3/20 21:59
 */
public enum RestCode {
  OK(0,"ok"),
  UNKNOWN_ERROR(100001,"未知异常"),
  TOKEN_INVALID(200001,"TOKEN失效"),
  USER_NOT_EXIST(200002,"用户不存在"),
  WRONG_PAGE(200003,"页码不合法"),
  LACK_PARAMS(200004,"缺少参数");

  public final int code;
  public final String msg;

  private RestCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }
}
