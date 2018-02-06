package com.atmira.springboot.app.util.paginator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageItem {
    
    private int numero;
    private boolean actual;
    
}
