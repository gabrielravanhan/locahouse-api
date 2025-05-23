package br.com.locahouse.controller;

import br.com.locahouse.controller.doc.ImagemDoImovelControllerDoc;
import br.com.locahouse.service.ImagemDoImovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/imagens-imoveis")
public class ImagemDoImovelController implements ImagemDoImovelControllerDoc {

    private final ImagemDoImovelService service;

    @Autowired
    public ImagemDoImovelController(ImagemDoImovelService service) {
        this.service = service;
    }

    @Override
    @PostMapping("/upload/{imovelId}")
    public ResponseEntity<Void> cadastrar(@PathVariable Integer imovelId, @RequestParam("imagem") MultipartFile imagem) {
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(this.service.cadastrar(imovelId, imagem).getId()).toUri()).build();
    }

    @Override
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        this.service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
