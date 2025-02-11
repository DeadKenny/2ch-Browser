package com.vortexwolf.dvach.models.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class ThreadsList {

    @JsonProperty("threads")
    private ThreadInfo[] threads;

    public ThreadInfo[] getThreads() {
        return threads;
    }

    public void setThreads(ThreadInfo[] threads) {
        this.threads = threads;
    }
}
