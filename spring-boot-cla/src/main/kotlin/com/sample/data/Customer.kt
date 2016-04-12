package com.sample.data

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Customer(
        @Column(nullable = false)
        var firstName: String = "",
        @Column(nullable = false)
        var lastName: String = "",
        @Id @GeneratedValue
        var id: Int = 0)
