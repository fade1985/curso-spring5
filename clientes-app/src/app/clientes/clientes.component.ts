import { Component, OnInit } from '@angular/core';
import { Cliente } from './cliente';
import { ClienteService } from './cliente.service';

@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html'
})
export class ClientesComponent implements OnInit {

  clientes: Cliente[];

  constructor(private clienteService: ClienteService) { console.log("hola puto soy el constructor")}

  ngOnInit() {
    console.log("hola puto soy el ngInit")

    this.clienteService.getClientes().subscribe(
      clientes => this.clientes = clientes
      /*function (clientes) {
        this.clientes = clientes;
      }*/
    );
  }

}
