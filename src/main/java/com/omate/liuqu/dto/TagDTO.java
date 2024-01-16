package com.omate.liuqu.dto;

public class TagDTO {
    private Long tagId;
    private String tagName;

    // 构造函数
    public TagDTO(Long tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    // Getters 和 Setters
    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    // 可以根据需要添加其他方法和逻辑
}
