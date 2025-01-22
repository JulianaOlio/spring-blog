package com.fiap.springblog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Artigo {

    @Id
    private String codigo;
    private String titulo;
    private LocalDateTime data;
    private String texto;
    private String url;
    private Integer status;

    @DBRef
    private Autor autor;

    public Artigo() {};

    public Artigo(String codigo, String titulo, LocalDateTime data, String texto, String url, Integer status, Autor autor) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.data = data;
        this.texto = texto;
        this.url = url;
        this.status = status;
        this.autor = autor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
