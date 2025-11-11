package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.models.Sport;
import com.sportradar.inesoitzinger.repositories.SportRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SportServiceTest {

    SportRepository repo = mock(SportRepository.class);

    SportService service = new SportService(repo);

    @Test
    void findAll_returnsList() {
        Sport s = new Sport();
        s.setId(1L);

        when(repo.findAll()).thenReturn(List.of(s));

        assertThat(service.findAll()).containsExactly(s);
    }

    @Test
    void getById_returnsSport() {
        Sport s = new Sport();
        s.setId(10L);

        when(repo.findById(10L)).thenReturn(Optional.of(s));

        assertThat(service.getById(10L)).isEqualTo(s);
    }

    @Test
    void getById_notFound_throws() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(999L))
                .isInstanceOf(DomainRuleViolation.class);
    }
}
