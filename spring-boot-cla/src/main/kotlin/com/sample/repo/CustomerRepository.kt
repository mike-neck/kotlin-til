package com.sample.repo

import com.sample.data.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Int> {
}
