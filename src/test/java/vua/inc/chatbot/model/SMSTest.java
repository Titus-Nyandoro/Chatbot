package vua.inc.chatbot.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

public class SMSTest {

    private SMS createNewSMS() {
        return SMS.builder()
                .id("1")
                .date("2023-01-01")
                .clintFrom("1234567890")
                .linkId("linkId")
                .text("Hello")
                .receiver("9876543210")
                .networkCode("networkCode")
                .build();
    }

    private SMS createExistingSMS() {
        return SMS.builder()
                .id("2")
                .date("2023-01-02")
                .clintFrom("1234567891")
                .linkId("linkId2")
                .text("Hi")
                .receiver("9876543211")
                .networkCode("networkCode2")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();
    }

    @Test
    public void PersistSMS_SetsCreatedAt() {
        SMS newSms = createNewSMS();

        // Act: invoking the entry point
        newSms.onCreate();

        // Assert: noticeable state change check
        assertNotNull(newSms.getCreatedAt());
    }

    @Test
    public void UpdateSMS_UpdatesUpdatedAt() {
        SMS existingSms = createExistingSMS();
        LocalDateTime initialUpdatedAt = existingSms.getUpdatedAt();

        // Act: invoking the entry point
        existingSms.onUpdate();

        // Assert: noticeable state change check
        assertNotNull(existingSms.getUpdatedAt());
        assertNull(initialUpdatedAt); // Ensure initial was null to see change
    }
}