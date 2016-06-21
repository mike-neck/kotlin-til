package com.sample.data

import javax.persistence.*

@Entity
@Table(name = "customers")
data class Customer(
        @Column(nullable = false)
        var firstName: String = "",
        @Column(nullable = false)
        var lastName: String = "",
        @Id @GeneratedValue
        var id: Int = 0)
