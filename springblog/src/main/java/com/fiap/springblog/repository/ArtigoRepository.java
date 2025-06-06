package com.fiap.springblog.repository;

import com.fiap.springblog.model.Artigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {

    public void deleteById(String codigo);

    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    //operador anda recebe um array com os parametros que vamos usar para comparar
    @Query("{$and: [ {'data' : {$gte: ?0}}, {'data' :{$lte: ?1 } } ] }")
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate);

    Page<Artigo> findAll(Pageable pageable);

    public List<Artigo> findByStatusOrderByTituloAsc(Integer status);

    @Query(value = "{'status' : {$eq: ?0} }", sort = "{'titulo' : 1}")
    public List<Artigo> obterArtigosPorStatusComOrdenacao(Integer status);
}
