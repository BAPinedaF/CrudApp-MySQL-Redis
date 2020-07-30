import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  baseUrl: string = 'http://localhost:8888/';

  protected generateBasicHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'text/html',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET'
    });
  }


  constructor(private http: HttpClient) { }

  getAllCustomers(): Observable<any> {
    return this.http.get<any>(this.baseUrl + 'customers');
  }

  deleteCustomer(id: number): Observable<any> {
    return this.http.get(this.baseUrl + 'customers/delete/' + id,
      {
        headers: this.generateBasicHeaders()
      }
    );
  }

  createCustomer(customer: object): Observable<object> {  
    return this.http.post(this.baseUrl + 'customers/add/', customer);  
  } 


}
