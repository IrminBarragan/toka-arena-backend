package toka.tokagotchi.tokaarenabackend.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toka.tokagotchi.tokaarenabackend.payment.model.PaymentRecord;

import java.util.Optional;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Optional<PaymentRecord> findByPaymentId(String paymentId);
}

