package com.cheney.thinker.mapping;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-26
 */
public enum CheneyThinkerMapping {

  INVOKE("Invocation invoke fail!");

  final String content;

  CheneyThinkerMapping(final String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

}
