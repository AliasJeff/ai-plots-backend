package com.alias.ai.model.entity;

public class DevChatResponse {
    private String content;

    public String getContent() {
        return this.content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof com.alias.ai.model.entity.DevChatResponse)) {
            return false;
        } else {
            com.alias.ai.model.entity.DevChatResponse other = (com.alias.ai.model.entity.DevChatResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$content = this.getContent();
                Object other$content = other.getContent();
                if (this$content == null) {
                    return other$content == null;
                } else return this$content.equals(other$content);
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof com.alias.ai.model.entity.DevChatResponse;
    }

    public String toString() {
        return "DevChatResponse(content=" + this.getContent() + ")";
    }

    public DevChatResponse() {
    }

    public DevChatResponse(final String content) {
        this.content = content;
    }
}
