package com.fiap.springblog.model;

public class AutorTotalArtigo {

    private Autor autor;
    private Long totalArtigos;

    public AutorTotalArtigo() {
    }

    public AutorTotalArtigo(Autor autor, Long totalArtigos) {
        this.autor = autor;
        this.totalArtigos = totalArtigos;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Long getTotalArtigos() {
        return totalArtigos;
    }

    public void setTotalArtigos(Long totalArtigos) {
        this.totalArtigos = totalArtigos;
    }
}
