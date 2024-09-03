package com.example.loja.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileUploadService {
    private final Path diretorioImg = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", "img");

    public String fazerUpload(MultipartFile imagem) throws IOException {
        if (imagem.isEmpty()) {
            System.out.println("imagem vazia");
            return null;
        }


        if (!Files.exists(diretorioImg)) {
            Files.createDirectories(diretorioImg);
        }

        String[] nomeArquivoArray = imagem.getOriginalFilename().split("\\.");
        String novoNome = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
        String extencaoArquivo = nomeArquivoArray[nomeArquivoArray.length - 1];

        String nomeImg = novoNome + "." + extencaoArquivo;

        Path caminhoImagem = diretorioImg.resolve(nomeImg);

        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(caminhoImagem.toFile()))) {
            stream.write(imagem.getBytes());
        }

        return nomeImg;
    }

}
