package com.atmira.springboot.app.controller.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRender<T> {
    
    private String url;
    private Page<T> page;
    
    private int totalPaginas;
    private int numeElementosPorPagina;
    private int paginaActual;
    
    private List<PageItem> paginas;
    
    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.paginas = new ArrayList<>();
        
        numeElementosPorPagina = page.getSize();
        totalPaginas = page.getTotalPages();
        paginaActual = page.getNumber() + 1;
        
        int desde, hasta = 0;
        if (totalPaginas <= numeElementosPorPagina) {
            desde = 1;
            hasta = totalPaginas;
        } else {
            if (paginaActual <= numeElementosPorPagina / 2) {
                desde = 1;
                hasta = numeElementosPorPagina;
            } else if (paginaActual >= totalPaginas - numeElementosPorPagina / 2) {
                desde = totalPaginas - numeElementosPorPagina;
            } else {
                desde = paginaActual - numeElementosPorPagina / 2;
                hasta = numeElementosPorPagina;
            }
        }
        
        for (int i = 0; i < hasta; i++) {
            paginas.add(new PageItem(desde + i, paginaActual == desde));
        }
    }
    
    public boolean isFirst(){
        return page.isFirst();
    }
    
    public boolean isLast(){
        return page.isLast();
    }
    
    public boolean isHasNext(){
        return page.hasNext();
    }
    
    public boolean isHasPrevious(){
        return page.hasPrevious();
    }
    
}
