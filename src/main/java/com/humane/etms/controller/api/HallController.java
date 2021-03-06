package com.humane.etms.controller.api;

import com.humane.etms.model.Hall;
import com.humane.etms.model.Staff;
import com.humane.etms.repository.HallRepository;
import com.humane.etms.repository.StaffRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/hall", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HallController {
    private final HallRepository repository;
    private final StaffRepository staffRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<Hall> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Hall> merge(@RequestBody Hall hall) {
        Hall rtn = repository.save(hall);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<Hall>> merge(@RequestBody Iterable<Hall> halls) {
        Iterable<Hall> rtn = repository.save(halls);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "staff", method = RequestMethod.GET)
    public Page<Staff> staff(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return staffRepository.findAll(predicate, pageable);
    }
}