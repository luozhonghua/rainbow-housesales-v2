package com.jhon.rain.house.common;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * <p>功能描述</br></p>
 *
 * @author jiangy19
 * @version v1.0
 * @projectName rainbow-house-v2
 * @date 2018/3/20 21:59
 */
public class ResultMsg {

  public static final String errorMsgKey = "errorMsg";

  public static final String successMsgKey = "successMsg";

  private String errorMsg;
  private String successMsg;

  public boolean isSuccess() {
    return errorMsg == null;
  }


  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public String getSuccessMsg() {
    return successMsg;
  }

  public void setSuccessMsg(String successMsg) {
    this.successMsg = successMsg;
  }

  public static ResultMsg errorMsg(String msg) {
    ResultMsg resultMsg = new ResultMsg();
    resultMsg.setErrorMsg(msg);
    return resultMsg;
  }

  public static ResultMsg successMsg(String msg) {
    ResultMsg resultMsg = new ResultMsg();
    resultMsg.setSuccessMsg(msg);
    return resultMsg;
  }


  public static ResultMsg success() {
    return new ResultMsg();
  }

  public Map<String, String> asMap() {
    Map<String, String> map = Maps.newHashMap();
    map.put(successMsgKey, successMsg);
    map.put(errorMsgKey, errorMsg);
    return map;
  }

  public String asUrlParams() {
    Map<String, String> map = asMap();
    Map<String, String> newMap = Maps.newHashMap();
    map.forEach((k, v) -> {
      if (v != null)
        try {
          newMap.put(k, URLEncoder.encode(v, "utf-8"));
        } catch (UnsupportedEncodingException e) {

        }
    });
    return Joiner.on("&").useForNull("").withKeyValueSeparator("=").join(newMap);
  }

  @Override
  public String toString() {
    return "ResultMsg [errorMsg=" + errorMsg + ", successMsg=" + successMsg + "]";
  }
}
