import { Component, OnInit } from '@angular/core';
import { ApiService } from 'src/app/service/api.service';
import { Customer } from 'src/app/model/customer.model';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-customer-show-all',
  templateUrl: './customer-show-all.component.html',
  styleUrls: ['./customer-show-all.component.css'],
  providers: [ApiService]
})

export class CustomerShowAllComponent implements OnInit {
  title = 'angular-shop';
  customers: Customer[];
  dataSource = new MatTableDataSource<Customer>([]);

  constructor(private apiService: ApiService) { }

  ngOnInit() {
    this.apiService.getAllCustomers()
      .subscribe( data => {
        this.customers = data;
      });
  }

  deleteCustomer(id: number) {
    this.apiService.deleteCustomer(id).subscribe(() => this.ngOnInit());
  }

}
