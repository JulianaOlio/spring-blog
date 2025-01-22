package com.fiap.springblog.service.impl;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.Autor;
import com.fiap.springblog.repository.ArtigoRepository;
import com.fiap.springblog.repository.AutorRepository;
import com.fiap.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArtigoServiceImp implements ArtigoService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    private ArtigoRepository artigoRepository;

    @Autowired
    private AutorRepository autorRepository;

    public ArtigoServiceImp(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Artigo> obterTodos() {
        return this.artigoRepository.findAll();
    }

    @Override
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository
                .findById(codigo)
                .orElseThrow(()-> new IllegalArgumentException("Artigo não existe"));
    }

    @Override
    public Artigo criar(Artigo artigo) {

        if( artigo.getAutor().getCodigo() != null){
            Autor autor = this.autorRepository
                    .findById(artigo.getAutor().getCodigo())
                    .orElseThrow(()-> new IllegalArgumentException("Autor inexistente"));

            artigo.setAutor(autor);
        }else{
            artigo.setAutor(null);
        }
        return this.artigoRepository.save(artigo);
    }

    @Override
    public List<Artigo> findByDataGreaterThan(LocalDateTime data) {
        //uso o query para construir consultas complexas.
        // a classe Criteria, podemos entrar com o criterio de busca / especificar igualdade, etc
        Query query = new Query(Criteria.where("data").gt(data));
        return mongoTemplate.find(query,Artigo.class);
    }

    @Override
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status) {
        Query query = new Query(Criteria.where("data")
                .is(data).and("status").is(status));

        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public void atualizar(Artigo updateArtigo) {

        this. artigoRepository.save(updateArtigo);
    }

    @Override
    public void atualizarArtigo(String codigo, String novaURL) {
        // criterio de busca por id
        Query query = new Query(Criteria.where("codigo").is(codigo));
        // definindo os campos que serão atualizados
        Update update = new Update().set("url",novaURL);
        //executa atualização
        this.mongoTemplate.updateFirst(query,update, Artigo.class);
    }
    @Override
    public void deleteById(String codigo){
        this.artigoRepository.deleteById(codigo);
    }

    @Override
    public void deleteArtigoById(String codigo) {
        Query query = new Query(Criteria.where("codigo").is(codigo));
        //executa a remoção do objeto baseado na query
        mongoTemplate.remove(query, Artigo.class);


    }
    //Query methods - example
    @Override
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data) {
        return this.artigoRepository.findByStatusAndDataGreaterThan(status, data);
    }

    @Override
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate) {
        return this.artigoRepository.obterArtigoPorDataHora(de,ate);
    }


}
