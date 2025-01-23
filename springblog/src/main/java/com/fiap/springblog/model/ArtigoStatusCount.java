package com.fiap.springblog.model;


public class ArtigoStatusCount {

    private Integer status;
    private Long quantidade;

    public ArtigoStatusCount() {}

    public ArtigoStatusCount(Integer status, Long quantidade) {
        this.status = status;
        this.quantidade = quantidade;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
}