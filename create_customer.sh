#!/bin/sh

grpcurl -d '{"customerId": "customer1", "email": "myemail", "name": "SHISHIR", "balance": 43.0, "address": {
                                                "street": "Road", "city": "KTM"}}' -plaintext localhost:9000 customer.api.CustomerService/Create

grpcurl -d '{"customerId": "customer2", "email": "neil@email.com", "name": "NEIL", "balance": 12.0, "address": {
                                                "street": "Road", "city": "KTM"}}' -plaintext localhost:9000 customer.api.CustomerService/Create

grpcurl -d '{"customerId": "customer3", "email": "bini@email.com", "name": "BINI", "balance": 21.0, "address": {
                                                "street": "Road", "city": "KTM"}}' -plaintext localhost:9000 customer.api.CustomerService/Create

grpcurl -d '{"customerId": "customer4", "email": "aama@email.com", "name": "Aama", "balance": 26.0, "address": {
                                                "street": "Road", "city": "KTM"}}' -plaintext localhost:9000 customer.api.CustomerService/Create

grpcurl -d '{"customerId": "customer5", "email": "ba@email.com", "name": "Ba", "balance": 4.0, "address": {
                                                "street": "Road", "city": "KTM"}}' -plaintext localhost:9000 customer.api.CustomerService/Create

