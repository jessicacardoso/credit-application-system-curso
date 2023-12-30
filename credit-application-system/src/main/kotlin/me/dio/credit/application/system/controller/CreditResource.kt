package me.dio.credit.application.system.controller

import jakarta.validation.Valid
import me.dio.credit.application.system.controller.dto.CreditDto
import me.dio.credit.application.system.controller.dto.CreditView
import me.dio.credit.application.system.controller.dto.CreditViewList
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.service.impl.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService
) {
    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<String> {
        val savedCredit = this.creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Credit ${savedCredit.creditCode} - Customer ${savedCredit.customer?.firstName} saved successfully!")
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long): ResponseEntity<List<CreditViewList>> {
        val creditViewList = this.creditService.findAllByCustomer(customerId).stream()
            .map { credit: Credit -> CreditViewList(credit) }
            .collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(creditViewList)

    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(
        @RequestParam(value = "customerId") customerId: Long,
        @PathVariable(value = "creditCode") creditCode: UUID
    ): ResponseEntity<CreditView> {
        val creditView = CreditView(this.creditService.findByCreditCode(customerId, creditCode))
        return ResponseEntity.status(HttpStatus.OK).body(creditView)
    }

}