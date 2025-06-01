package com.fiap.springblog.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Artigo {

    @Id
    private String codigo;

    private String titulo;

    @NotNull(message = "A data do artigo não pode ser nula.")
    private LocalDateTime data;

    @NotBlank(message = "O Texto do Artigo não pode estar em branco.")
    @TextIndexed
    private String texto;

    @NotBlank(message = "O Título do Artigo não pode estar em branco.")
    private String url;

    @NotNull(message = "O status do artigo não pode ser nulo.")
    private Integer status;

    @DBRef
    private Autor autor;

    // para controle de concorrência. Verifica a versão acessada por cada usuário
    @Version
    private Long version;

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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
