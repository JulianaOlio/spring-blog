package com.fiap.springblog.controller;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.ArtigoStatusCount;
import com.fiap.springblog.model.AutorTotalArtigo;
import com.fiap.springblog.repository.AutorRepository;
import com.fiap.springblog.service.ArtigoService;
import com.fiap.springblog.model.Autor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PutExchange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/artigos")
public class ArtigoController {

    @Autowired
    private ArtigoService artigoService;

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailureException(
            OptimisticLockingFailureException ex
        ){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Erro de concorrência: O artigo foi atualizado por outro usuário. " +
                         "Por favor tente novamente!");
    }

    @GetMapping
    public List<Artigo> obterTodos() {
        return this.artigoService.obterTodos();
    }

    @GetMapping("/{codigo}")
    public Artigo obterPorCodigo(@PathVariable String codigo) {
        return this.artigoService.obterPorCodigo(codigo);
    }

    @PostMapping
    public Artigo criar(@RequestBody Artigo artigo) {
        return this.artigoService.criar(artigo);
    }

    @GetMapping("/maiordata")
    public List<Artigo> findByDataGreaterThan(@RequestParam("data") LocalDateTime data) {
        return this.artigoService.findByDataGreaterThan(data);
    }

    @GetMapping("/data-status")
    public List<Artigo> findByDataAndStatus(
            @RequestParam("data") LocalDateTime data,
            @RequestParam("status") Integer status) {
        return this.artigoService.findByDataAndStatus(data, status);
    }

    @PutMapping
    public void atualizar(@RequestBody Artigo artigo) {
        this.artigoService.atualizar(artigo);
    }

    @PutMapping("/{codigo}")
    public void atualizarArtigo(@PathVariable String codigo,
                                @RequestBody String novaURL) {
        this.artigoService.atualizarArtigo(codigo, novaURL);
    }

    @DeleteMapping("/{codigo}")
    public void deletarArtigo(@PathVariable String codigo) {
        this.artigoService.deleteById(codigo);
    }

    @DeleteMapping("/delete")
    public void deletarArtigoById(@RequestParam("codigo") String codigo) {
        this.artigoService.deleteArtigoById(codigo);
    }

    @GetMapping("/status-maiordata")
    public List<Artigo> findByStatusAndDataGreaterThan(
            @RequestParam("status") Integer status,
            @RequestParam("data") LocalDateTime data) {

        return this.artigoService.findByStatusAndDataGreaterThan(status, data);
    }

    @GetMapping("/periodo")
    public List<Artigo> obterArtigoPorDataHora(
            @RequestParam("de") LocalDateTime de,
            @RequestParam("ate") LocalDateTime ate) {
        return this.artigoService.obterArtigoPorDataHora(de, ate);
    }

    @GetMapping("/artigo-complexo")
    public List<Artigo> encontrarArtigosComplexos(
            @RequestParam("status") Integer status,
            @RequestParam("data") LocalDateTime data,
            @RequestParam("titulo") String titulo
    ){
        return this.artigoService.encontrarArtigosComplexos(status, data, titulo);
    }
    @GetMapping("/pagina-artigo")
    public ResponseEntity<Page<Artigo>> obterArtigosPaginad(Pageable pageable){
        Page<Artigo> artigos = this.artigoService.findAll(pageable);

        return ResponseEntity.ok(artigos);
    }

    @GetMapping("/status-ordenado")
    public List<Artigo> findByStatusOrderByTituloAsc(
            @RequestParam("status")Integer status
    ){
           return this.artigoService.findByStatusOrderByTituloAsc(status);
    }

    @GetMapping("/status-query-ordenacao")
    public List<Artigo> obterArtigosPorStatusComOrdenacao(
            @RequestParam("status") Integer status
    ){
        return this.artigoService.obterArtigosPorStatusComOrdenacao(status);
    }

    @GetMapping("/buscatexto")
    public List<Artigo> findByTexto(
            @RequestParam("searchText") String searchText
    ){
        return this.artigoService.findByTexto(searchText);
    }
    @GetMapping("/contar-artigo")
    public List<ArtigoStatusCount> contarArtigosPorStatus(){
        return this.artigoService.contarArtigosPorStatus();
    }

    @GetMapping("/total-artigo-autor-periodo")
    public List<AutorTotalArtigo> calcularTotalArtigosAutorPeriodo(
            @RequestParam("dataInicio") LocalDate dataInicial,
            @RequestParam("dataFim") LocalDate dataFim
    ){
        return this.artigoService.calcularTotalArtigosAutorPeriodo(dataInicial,dataFim);
    }


}
