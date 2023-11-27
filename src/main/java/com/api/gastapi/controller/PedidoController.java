package com.api.gastapi.controller;

import com.api.gastapi.dto.PedidoAtualizacaoDto;
import com.api.gastapi.dto.PedidoDto;
import com.api.gastapi.dto.PedidoListagemDto;
import com.api.gastapi.model.PedidoModel;
import com.api.gastapi.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
    @Autowired
    private PedidoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrarPedido(@RequestBody @Valid PedidoDto dados){
        repository.save(new PedidoModel(dados));
    }

    @GetMapping
    public List<PedidoListagemDto> listarPedido(){
        return repository.findAll().stream().map(PedidoListagemDto::new).toList();
    }

    @GetMapping("/{id_pedido}")
    public ResponseEntity<Object> buscarPedido(@PathVariable(value = "id_pedido") UUID id_pedido){
        Optional<PedidoModel> pedidoBuscado = repository.findById(id_pedido);

        if (pedidoBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pedidoBuscado.get());
    }

    @PutMapping
    @Transactional
    public void atualizarPedido(@RequestBody @Valid PedidoAtualizacaoDto dados){
        var pedido = repository.getReferenceById(dados.id_pedido());
        pedido.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id_pedido}")
    @Transactional
    public ResponseEntity<Object> deletarPedido(@PathVariable(value = "id_pedido") UUID id_pedido) {
        Optional<PedidoModel> pedidoBuscado = repository.findById(id_pedido);

        if (pedidoBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        repository.deleteById(id_pedido);
        return ResponseEntity.status(HttpStatus.OK).body("Pedido deletado com sucesso!");
    }
}
