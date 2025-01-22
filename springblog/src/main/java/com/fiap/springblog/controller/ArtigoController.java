package com.fiap.springblog.controller;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.repository.AutorRepository;
import com.fiap.springblog.service.ArtigoService;
import com.fiap.springblog.model.Autor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PutExchange;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/artigos")
public class ArtigoController {

    @Autowired
    private ArtigoService artigoService;

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
                                @RequestBody String novaURL){
        this.artigoService.atualizarArtigo(codigo,novaURL);
    }

    @DeleteMapping("/{codigo}")
    public void deletarArtigo(@PathVariable String codigo){
        this.artigoService.deleteById(codigo);
    }

    @DeleteMapping("/delete")
    public void deletarArtigoById(@RequestParam("codigo") String codigo){
        this.artigoService.deleteArtigoById(codigo);
    }
    @GetMapping("/status-maiordata")
    public List<Artigo> findByStatusAndDataGreaterThan(
            @RequestParam("status") Integer status,
            @RequestParam("data") LocalDateTime data){

        return this.artigoService.findByStatusAndDataGreaterThan(status, data);

    }
}
