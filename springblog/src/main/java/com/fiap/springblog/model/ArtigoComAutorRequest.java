package com.fiap.springblog.model;

import lombok.Data;

@Data
public class ArtigoComAutorRequest {

    private Autor autor;
    private Artigo artigo;

}
