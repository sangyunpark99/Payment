package com.example.payment.transfer;

import com.example.payment.transfer.dto.reqeust.TransferRequest;
import com.example.payment.transfer.dto.response.TransferResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private static final String TRANSFER_SUCCESS = "이체를 성공적으로 완료 하였습니다.";

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@RequestBody @Valid TransferRequest request) {
        transferService.transfer(request);
        return ResponseEntity.ok(new TransferResponse(TRANSFER_SUCCESS));
    }
}
