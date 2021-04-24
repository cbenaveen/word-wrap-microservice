package com.naveen.microservice.wordwrap.controller.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@Getter
@ToString
public class PaginatedWrappedResponse extends WrappedResponse {
    private final int totalPage;
    private final int currentPage;

    public PaginatedWrappedResponse(Collection<String> lines, final int totalPage, final int currentPage) {
        super(lines);
        this.totalPage = totalPage;
        this.currentPage = currentPage;
    }

    public static PaginatedWrappedResponseBuilder paginatedBuilder() {
        return new PaginatedWrappedResponseBuilder();
    }

    public PaginatedWrappedResponseBuilder toBuilder() {
        return new PaginatedWrappedResponseBuilder().lines(this.lines).totalPage(this.totalPage).currentPage(this.currentPage);
    }

    public static class PaginatedWrappedResponseBuilder {
        private Collection<String> lines;
        private int totalPage;
        private int currentPage;

        PaginatedWrappedResponseBuilder() {
        }

        public PaginatedWrappedResponseBuilder lines(Collection<String> lines) {
            this.lines = lines;
            return this;
        }

        public PaginatedWrappedResponseBuilder totalPage(int totalPage) {
            this.totalPage = totalPage;
            return this;
        }

        public PaginatedWrappedResponseBuilder currentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public PaginatedWrappedResponse paginatedBuilder() {
            return new PaginatedWrappedResponse(lines, totalPage, currentPage);
        }

        public String toString() {
            return "PaginatedWrappedResponse.PaginatedWrappedResponseBuilder(lines=" + this.lines + ", totalPage=" + this.totalPage + ", currentPage=" + this.currentPage + ")";
        }
    }
}
