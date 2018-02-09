package com.atmira.springboot.app.service.impl;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.atmira.springboot.app.service.IUploadFileService;

public class UploadFileServiceImpl implements IUploadFileService {
    
    private static final String UPLOADS_FOLDER = "uploads";
    
    private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
    
    @Override
    public Resource load(
        String filename) throws MalformedURLException{
        
        Path pathFoto = getPath(filename);
        log.info("pathFoto: " + pathFoto);
        Resource recurso = null;
        
        try {
            recurso = new UrlResource(pathFoto.toUri());
            if (!recurso.exists() || !recurso.isReadable()) {
                throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return recurso;
    }
    
    @Override
    public String copy(
        MultipartFile file){
        return null;
    }
    
    @Override
    public boolean delete(
        String filename){
        return false;
    }
    
    public Path getPath(
        String filename){
        return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }
}
