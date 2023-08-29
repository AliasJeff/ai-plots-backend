package com.alias.ai.model.entity;

import java.io.Serializable;

public class DevChatRequest implements Serializable {
    private Long modelId;
    private String message;
    private static final long serialVersionUID = 1L;

    public Long getModelId() {
        return this.modelId;
    }

    public String getMessage() {
        return this.message;
    }

    public void setModelId(final Long modelId) {
        this.modelId = modelId;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof com.alias.ai.model.entity.DevChatRequest)) {
            return false;
        } else {
            com.alias.ai.model.entity.DevChatRequest other = (com.alias.ai.model.entity.DevChatRequest)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$modelId = this.getModelId();
                Object other$modelId = other.getModelId();
                if (this$modelId == null) {
                    if (other$modelId != null) {
                        return false;
                    }
                } else if (!this$modelId.equals(other$modelId)) {
                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    return other$message == null;
                } else return this$message.equals(other$message);
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof com.alias.ai.model.entity.DevChatRequest;
    }

    public String toString() {
        return "DevChatRequest(modelId=" + this.getModelId() + ", message=" + this.getMessage() + ")";
    }

    public DevChatRequest() {
    }

    public DevChatRequest(final Long modelId, final String message) {
        this.modelId = modelId;
        this.message = message;
    }
}
