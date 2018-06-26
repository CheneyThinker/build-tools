package com.cheney.thinker.core;

import com.cheney.thinker.mapping.CheneyThinkerMapping;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

/**
 * @description Generator of Response
 * @author CheneyThinker
 * @date 2018-06-26
 */
public final class ResponseGenerator {

  private static final String DEFAULT_YES = "YES";
  private static final String format = "\nclass:\t{}\nmethod:\t{}\nline:\t{}\n";

  public static Response genYes() {
    return new Response()
            .setCode(ResponseCode.YES)
            .setMsg(DEFAULT_YES);
  }

  public static Response genYes(Object data) {
    return genYes()
            .setData(data);
  }

  public static Response genNo(Logger logger, Exception e, CheneyThinkerMapping mapping) {
    StackTraceElement[] elements = e.getCause().getStackTrace();
    for (int i = elements.length - 1; i >= 0; i--) {
      StackTraceElement element = elements[i];
      if (element.toString().startsWith("com.cheney.thinker"))
        logger.error(format, element.getClassName(), element.getMethodName(), element.getLineNumber());
    }
    return new Response()
            .setCode(ResponseCode.NO)
            .setMsg(StringUtils.isEmpty(e.getCause()) ? mapping.getContent() : StringUtils.isEmpty(e.getCause().getMessage()) ? mapping.getContent() : e.getCause().getMessage());
  }

  public static Response genNo(Logger logger, Object data, Exception e, CheneyThinkerMapping mapping) {
    return genNo(logger, e, mapping)
            .setData(data);
  }

}