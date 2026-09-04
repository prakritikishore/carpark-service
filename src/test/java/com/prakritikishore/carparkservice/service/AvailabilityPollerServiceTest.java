package com.prakritikishore.carparkservice.service;

import com.prakritikishore.carparkservice.repository.CarParkAvailabilityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityPollerServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CarParkAvailabilityRepository availabilityRepository;

    @InjectMocks
    private AvailabilityPollerService pollerService;

    @Test
    void testGracefulDegradationOnApiFailure() {
        ReflectionTestUtils.setField(pollerService, "availabilityUrl", "https://api.data.gov.sg/fail");
        when(restTemplate.getForObject(anyString(), eq(Object.class)))
                .thenThrow(new RestClientException("Connection Timeout"));

        pollerService.fetchAvailability();

        // Verify fallback to marking cache as stale
        verify(availabilityRepository, times(1)).markAllAsStale();
    }
}
