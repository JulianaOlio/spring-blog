package com.fiap.springblog.service.impl;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.ArtigoStatusCount;
import com.fiap.springblog.model.Autor;
import com.fiap.springblog.model.AutorTotalArtigo;
import com.fiap.springblog.repository.ArtigoRepository;
import com.fiap.springblog.repository.AutorRepository;
import com.fiap.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArtigoServiceImp implements ArtigoService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    private MongoTransactionManager mongoTransactionManager;

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

    @Transactional(readOnly = true)
    @Override
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository
                .findById(codigo)
                .orElseThrow(()-> new IllegalArgumentException("Artigo não existe"));
    }

    @Override
    public ResponseEntity<?> criarArtigoComAutor(Artigo artigo, Autor autor) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(mongoTransactionManager);
        transactionTemplate.execute(
                status -> {
                    try {
                        // iniciar a trasanção
                        autorRepository.save(autor);
                        artigo.setData(LocalDateTime.now());
                        artigo.setAutor(autor);
                        artigoRepository.save(artigo);
                    } catch(Exception e){
                        // trata o erro e lançar a exceção de volta em caso de exceção
                        status.setRollbackOnly(); // desfaz a gravação em caso de erro
                        throw  new RuntimeException("Erro ao criar um artigo com o autor" + e.getMessage());
                    }
                    return null;
                });
                return null;
    }

    /*@Override
    public ResponseEntity<?> criar(Artigo artigo) {
        if(artigo.getAutor().getCodigo() != null){
            //crio um autor e recupero o autor no banco de dados
            Autor autor = this.autorRepository
                    .findById(artigo.getAutor().getCodigo())
                    .orElseThrow(()-> new IllegalArgumentException("Autor inexistente"));
            artigo.setAutor(autor);
        }else{
            artigo.setAutor(null);
        }
        try{
            this.artigoRepository.save(artigo);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch(DuplicateKeyException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Artigo já existe na coleção");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar artigo" + e.getMessage());
        }
    }
     */

    @Override
    public ResponseEntity<?> atualizarArtigo(String codigo, Artigo artigo) {
        try {
            Artigo existenteArtigo = this.artigoRepository
                    .findById(codigo).orElse(null);

            if(existenteArtigo == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Artigo não encontrado na coleção");
            }
            //Atualizar os dados do artigo existente
            existenteArtigo.setTitulo(artigo.getTitulo());
            existenteArtigo.setData(artigo.getData());
            existenteArtigo.setTexto(artigo.getTexto());
            this.artigoRepository.save(existenteArtigo);
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar o artigo: " + e.getMessage());

        }
    }

   /* @Transactional
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
        // tratamento de exceção: do optismit looking exception
        try{
            return this.artigoRepository.save(artigo);

        } catch (OptimisticLockingFailureException e) {
            // desenvolvo a estratégia

            // 1.recupero o documenta mais recente do banco de dados ( da coleção artigos"
            Artigo atualizado = artigoRepository.findById(artigo.getCodigo()).orElse(null);
            if(atualizado != null) {

                //2. atualizar os campos desejados

                atualizado.setTitulo(artigo.getTitulo());
                atualizado.setTexto(artigo.getTexto());
                atualizado.setStatus(artigo.getStatus());

                //3. incrementar a versão manualmente do documento colocando um acumulador
                atualizado.setVersion(artigo.getVersion() + 1);

                //4. tentar salvar novamente
                return this.artigoRepository.save(artigo);
            }else {
                throw new RuntimeException("Artigo não encontrado" + artigo.getCodigo());
            }
        }
    }*/

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

    @Transactional
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

    @Transactional
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

    @Override
    public List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo) {

        //crio um criterio
        Criteria criteria = new Criteria();

        //filtro artigos com data menor ou igual ao valor fornecido
        criteria.and("data").lte(data);

        // filtro somente artigos com status identificado no banco
        if(status != null){
            criteria.and("status").is(status);
        }
        // filtro os artigos cujo titulo exista no banco
        if(titulo != null && !titulo.isEmpty()){
            criteria.and("titulo").regex(titulo, "i");
        }
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public Page<Artigo> findAll(Pageable pageable) {
        Sort sort = Sort.by("titulo").ascending();
        Pageable paginacao =
                PageRequest.of(pageable.getPageNumber(),
                        pageable.getPageSize(),sort);
        return this.artigoRepository.findAll(paginacao);
    }

    //Query methods
    @Override
    public List<Artigo> findByStatusOrderByTituloAsc(Integer status) {
        return this.artigoRepository.findByStatusOrderByTituloAsc(status);
    }

    @Override
    public List<Artigo> obterArtigosPorStatusComOrdenacao(Integer status) {
        return this.artigoRepository.obterArtigosPorStatusComOrdenacao(status);
    }

    @Override
    public List<Artigo> findByTexto(String searchTerm) {

        TextCriteria criteria =
                TextCriteria.forDefaultLanguage().matchingPhrase(searchTerm);

        Query query = TextQuery.queryText(criteria).sortByScore();
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<ArtigoStatusCount> contarArtigosPorStatus() {
        TypedAggregation<Artigo> aggregation =
                Aggregation.newAggregation(
                        Artigo.class,
                        Aggregation.group("status").count().as("quantidade"),
                        Aggregation.project("quantidade").and("status")
                                .previousOperation()
                );
        AggregationResults<ArtigoStatusCount> result =
                mongoTemplate.aggregate(aggregation, ArtigoStatusCount.class);

        return result.getMappedResults();
    }

    @Override
    public List<AutorTotalArtigo> calcularTotalArtigosAutorPeriodo(LocalDate dataInicio,
                                                                   LocalDate dataFim) {
       TypedAggregation<Artigo> aggregation =
               Aggregation.newAggregation(
                       Artigo.class,
                       Aggregation.match(
                               Criteria.where("data")
                                       .gte(dataInicio.atStartOfDay())
                                       .lt(dataFim.plusDays(1).atStartOfDay())
                       ),
                       Aggregation.group("autor").count().as("totalArtigos"),
                       Aggregation.project("totalArtigos").and("autor")
                                .previousOperation()
               );

       AggregationResults<AutorTotalArtigo> results =
               mongoTemplate.aggregate(aggregation, AutorTotalArtigo.class);

       return results.getMappedResults();
    }

    /**
     *
     * Exclusão do artigo e do autor ao mesmo tempo
     * @param artigo
     */
    @Override
    public void excluirArtigoEAutor(Artigo artigo) {
        TransactionTemplate  transactionTemplate = new TransactionTemplate(mongoTransactionManager);
        transactionTemplate.execute(
                status -> {
                    try{
                        //iniciar a transação de exclusão
                        artigoRepository.delete(artigo);
                        Autor autor = artigo.getAutor();
                        autorRepository.delete(autor);
                    } catch (Exception e){
                        // tratar o erro e lançar a transação de volta em caso se excção
                        status.setRollbackOnly();
                        throw new RuntimeException("Erro ao excluir o artigo e o autor" + e.getMessage());
                    }
                    return null;
                });
    }
}
