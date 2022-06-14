package br.com.pay.equalspay.controller;

import br.com.pay.equalspay.domain.Arquivo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class EqualsPayController {

    @Value("${file.upload-dir}")
    private String DIRETORIO_UPLOAD;

    @PostMapping("/api/conciliacao/processa-arquivo")
    @ResponseBody
    public ResponseEntity processaArquivo(@RequestParam("arquivo") MultipartFile arquivoEnviado) throws IOException {

        File arquivoRecebido = getArquivoRecebido(arquivoEnviado);

        //Demonstracao do conteudo do arquivo no console
        Files.lines(Paths.get(arquivoRecebido.getAbsolutePath()))
                .forEach(System.out::println);

        //TODO

        return ResponseEntity.ok("Arquivo enviado!");
    }

    @GetMapping("/api/conciliacao/obtem-arquivo/{identificadorArquivo}")
    @ResponseBody
    public ResponseEntity obtemArquivo(@PathVariable("identificadorArquivo") Integer identificadorArquivo) {

        Arquivo arquivoExtraido = new Arquivo();

        //TODO

        return ResponseEntity.ok("Conteudo Arquivo!");
    }

    @GetMapping("/api/conciliacao/obtem-venda/{identificadorVenda}")
    @ResponseBody
    public ResponseEntity obtemVendaDoArquivo(@PathVariable("identificadorVenda") Integer identificadorVenda) {

        //TODO

        return ResponseEntity.ok("Venda!");
    }

    @GetMapping("/api/conciliacao/obtem-vendas-pagas")
    @ResponseBody
    public ResponseEntity obtemVendasPagas() {

        //TODO

        return ResponseEntity.ok("Listagem de vendas pagas!");
    }

    @GetMapping("/api/conciliacao/obtem-vendas-pendentes")
    @ResponseBody
    public ResponseEntity obtemVendasPendentes() {

        //TODO

        return ResponseEntity.ok("Listagem de vendas pendentes!");
    }

    @GetMapping("/api/conciliacao/obtem-taxa-praticada-venda/{identificadorVenda}")
    @ResponseBody
    public ResponseEntity obtemTaxaPraticaDaVenda(@PathVariable("identificadorVenda") Integer identificadorVenda) {

        //TODO

        return ResponseEntity.ok("Taxa praticada da venda!");
    }

    private File getArquivoRecebido(MultipartFile arquivoEnviado) throws IOException {

        Path fileStorageLocation = Paths.get(DIRETORIO_UPLOAD)
                .toAbsolutePath().normalize();

        Files.createDirectories(fileStorageLocation);

        String fileName = arquivoEnviado.getOriginalFilename();

        File arquivoRecebido = new File(DIRETORIO_UPLOAD + fileName);
        try {
            if (arquivoRecebido.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(arquivoRecebido)) {
                    fos.write(arquivoEnviado.getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path filePath = fileStorageLocation.resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if(resource.exists())
           return resource.getFile();

        return null;
    }

}
