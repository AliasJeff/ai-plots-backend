package com.alias.ai.config;

import com.alias.ai.client.GPTClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("gpt.client")
@ComponentScan
public class GPTClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public GPTClient yuApiClient() {
        return new GPTClient(this.accessKey, this.secretKey);
    }

    public GPTClientConfig() {
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setAccessKey(final String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof com.alias.ai.config.GPTClientConfig)) {
            return false;
        } else {
            com.alias.ai.config.GPTClientConfig other = (com.alias.ai.config.GPTClientConfig)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$accessKey = this.getAccessKey();
                Object other$accessKey = other.getAccessKey();
                if (this$accessKey == null) {
                    if (other$accessKey != null) {
                        return false;
                    }
                } else if (!this$accessKey.equals(other$accessKey)) {
                    return false;
                }

                Object this$secretKey = this.getSecretKey();
                Object other$secretKey = other.getSecretKey();
                if (this$secretKey == null) {
                    return other$secretKey == null;
                } else return this$secretKey.equals(other$secretKey);
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof com.alias.ai.config.GPTClientConfig;
    }

    public String toString() {
        return "ClientConfig(accessKey=" + this.getAccessKey() + ", secretKey=" + this.getSecretKey() + ")";
    }
}