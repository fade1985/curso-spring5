package com.atmira.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@ToString
@Entity
@Table(name = "facturas")
public class Factura implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String descripcion;
    
    private String observacion;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    private Date createAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id")
    List<ItemFactura> items;
    
    public Factura() {
        this.items = new ArrayList<>();
    }
    
    public void addItemFactura(
        ItemFactura item){
        this.items.add(item);
    }
    
    public Double getTotal(){
        Double total = 0.0;
        
        for (ItemFactura item : items) {
            total += item.calcularImporte();
        }
        
        return total;
    }
    
    @PrePersist
    public void prePersist(){
        this.createAt = new Date();
    }
}
