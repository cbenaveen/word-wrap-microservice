package com.naveen.microservice.wordwrap.controller.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@Getter
@ToString
public class PaginatedWrappedResponse extends WrappedResponse {
    private final long contentId;
    private final int nextOffset;

    public PaginatedWrappedResponse(Collection<String> lines, final long contentId, final int nextOffset) {
        super(lines);
        this.contentId = contentId;
        this.nextOffset = nextOffset;
    }

    public static PaginatedWrappedResponseBuilder paginatedBuilder() {
        return new PaginatedWrappedResponseBuilder();
    }

    public PaginatedWrappedResponseBuilder toBuilder() {
        return new PaginatedWrappedResponseBuilder().lines(this.lines).nextOffset(this.nextOffset);
    }

    public static class PaginatedWrappedResponseBuilder {
        private Collection<String> lines;
        private long contentId;
        private int nextOffset;

        PaginatedWrappedResponseBuilder() {
        }

        public PaginatedWrappedResponseBuilder lines(Collection<String> lines) {
            this.lines = lines;
            return this;
        }

        public PaginatedWrappedResponseBuilder contentId(long contentId) {
            this.contentId = contentId;
            return this;
        }

        public PaginatedWrappedResponseBuilder nextOffset(int nextOffset) {
            this.nextOffset = nextOffset;
            return this;
        }

        public PaginatedWrappedResponse paginatedBuilder() {
            return new PaginatedWrappedResponse(lines, contentId, nextOffset);
        }

        public String toString() {
            return "PaginatedWrappedResponse.PaginatedWrappedResponseBuilder(lines=" + this.lines
                    + ", currentPage=" + this.nextOffset + ")";
        }
    }
}
