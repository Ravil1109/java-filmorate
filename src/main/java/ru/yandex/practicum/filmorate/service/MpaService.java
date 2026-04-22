package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dto.MpaResponseDTO;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage storage;

    public MpaResponseDTO getMpa(Long mpaId) {

        Mpa mpa = storage.get(mpaId);
        return MpaResponseDTO.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
    }

    public List<MpaResponseDTO> listMpa() {
        List<Mpa> listMpa = storage.getList();
        return listMpa.stream().map(mpa -> MpaResponseDTO.builder()
                        .id(mpa.getId())
                        .name(mpa.getName())
                        .build())
                        .toList();
    }
}
