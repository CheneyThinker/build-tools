package com.cheney.thinker.mapping;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-20
 */
public enum CheneyThinkerMapping {

  INDEX("Call index fail!");

  final String content;

  CheneyThinkerMapping(final String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

}
