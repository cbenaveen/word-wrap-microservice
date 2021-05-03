package com.naveen.microservice.wordwrap.controller.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@Getter
@ToString
public class PaginatedWrappedResponse extends WrappedResponse {
    private final int nextOffset;

    public PaginatedWrappedResponse(Collection<String> lines, final int nextOffset) {
        super(lines);
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
        private int nextOffset;

        PaginatedWrappedResponseBuilder() {
        }

        public PaginatedWrappedResponseBuilder lines(Collection<String> lines) {
            this.lines = lines;
            return this;
        }

        public PaginatedWrappedResponseBuilder nextOffset(int nextOffset) {
            this.nextOffset = nextOffset;
            return this;
        }

        public PaginatedWrappedResponse paginatedBuilder() {
            return new PaginatedWrappedResponse(lines, nextOffset);
        }

        public String toString() {
            return "PaginatedWrappedResponse.PaginatedWrappedResponseBuilder(lines=" + this.lines
                    + ", currentPage=" + this.nextOffset + ")";
        }
    }
}
