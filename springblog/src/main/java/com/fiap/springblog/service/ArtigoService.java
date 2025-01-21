package com.fiap.springblog.service;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.Autor;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {

    public List<Artigo> obterTodos();

    public Artigo obterPorCodigo(String codigo);

    public Artigo criar(Artigo artigo);

    public List<Artigo> findByDataGreaterThan(LocalDateTime data);

    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status);

    public void atualizar( Artigo updateArtigo);

    public void atualizarArtigo(String codigo, String novaURL);

    public void deleteById(String codigo);

    public void deleteArtigoById(String codigo);
}
