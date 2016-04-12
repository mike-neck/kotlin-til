package com.sample

import com.sample.data.Customer
import com.sample.repo.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class App : CommandLineRunner {

    @Autowired
    lateinit var repository: CustomerRepository

    override fun run(vararg args: String?) {
        repository.save(Customer("sazae", "fuguta"))
        repository.save(Customer("katsuo", "isono"))
        repository.save(Customer("wakame", "isono"))
        repository.save(Customer("masuo", "fuguta"))
        repository.save(Customer("tarao", "fuguta"))
 
        repository.findAll().forEach { println(it) }
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(App::class.java, *args)
        }
    }
}
