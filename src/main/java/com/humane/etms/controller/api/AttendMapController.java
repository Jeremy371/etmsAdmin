package com.humane.etms.controller.api;

import com.google.common.collect.Iterables;
import com.humane.etms.mapper.DataMapper;
import com.humane.etms.model.*;
import com.humane.etms.repository.AttendMapRepository;
import com.humane.etms.repository.AttendPaperRepository;
import com.humane.etms.repository.DeviceRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "api/attendMap", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AttendMapController {
    private final AttendMapRepository repository;
    private final AttendPaperRepository paperRepository;
    private final DeviceRepository deviceRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AttendMap> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Iterable<AttendMap> list(@QuerydslPredicate Predicate predicate, Sort sort) {
        return repository.findAll(predicate, sort);
    }

    @RequestMapping(value = "hall", method = RequestMethod.GET)
    public ResponseEntity<?> findByHall(@RequestParam(defaultValue = "") String attendCd, @RequestParam(defaultValue = "") String hallCd) {
        if (StringUtils.isAnyEmpty(attendCd, hallCd))
            return new ResponseEntity<>("parameters empty!", HttpStatus.BAD_REQUEST);

        QAttendMap attendMap = QAttendMap.attendMap;
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(attendMap.attend.attendCd.eq(attendCd));
        predicate.and(attendMap.hall.hallCd.eq(hallCd).or(attendMap.attendHall.hallCd.eq(hallCd)));

        return ResponseEntity.ok(repository.findAll(predicate));
    }

    @RequestMapping(value = "find", method = RequestMethod.GET)
    public ResponseEntity<?> findByExaminee(@RequestParam(defaultValue = "") String admissionCd, @RequestParam(defaultValue = "") String attendDate, @RequestParam(defaultValue = "") String examineeCd, @RequestParam(defaultValue = "") String examineeNm) throws ParseException {
        if (StringUtils.isEmpty(admissionCd) || (StringUtils.isEmpty(examineeCd) && StringUtils.isEmpty(examineeNm)))
            return new ResponseEntity<>("parameters empty!", HttpStatus.BAD_REQUEST);

        QAttendMap attendMap = QAttendMap.attendMap;
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(attendMap.attend.admission.admissionCd.eq(admissionCd));
        if (StringUtils.isNotEmpty(attendDate))
            predicate.and(attendMap.attend.attendDate.eq(new SimpleDateFormat("yyyy-MM-dd").parse(attendDate)));
        if (StringUtils.isNotEmpty(examineeCd))
            predicate.and(attendMap.examinee.examineeCd.like(examineeCd.concat("%")));
        if (StringUtils.isNotEmpty(examineeNm))
            predicate.and(attendMap.examinee.examineeNm.like(examineeNm.concat("%")));

        return ResponseEntity.ok(repository.findAll(predicate));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AttendMap> merge(@RequestBody AttendMap attendMap
            , @RequestHeader(name = "uuid") String uuid
            , @RequestHeader(name = "packagename") String packageName
            , @RequestHeader(name = "versionname") String versionName) { // 출결
        return new ResponseEntity<>(save(uuid, packageName, versionName, attendMap), HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendMap>> merge(@RequestBody Iterable<AttendMap> attendMaps
            , @RequestHeader(name = "uuid") String uuid
            , @RequestHeader(name = "packagename") String packageName
            , @RequestHeader(name = "versionname") String versionName) {
        ArrayList<AttendMap> rtn = new ArrayList<>();
        attendMaps.forEach(attendMap -> rtn.add(save(uuid, packageName, versionName, attendMap)));

        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "device", method = RequestMethod.GET)
    //public ResponseEntity<?> findByDevice(@RequestParam(defaultValue = "") String packageName, @RequestParam(defaultValue = "") String deviceUuid, @RequestParam(defaultValue = "") String versionName) {
    public ResponseEntity<?> findByDevice(@RequestHeader(name = "uuid") String uuid, @RequestHeader(name = "packagename") String packageName, @RequestHeader(name = "versionname") String versionName) {
        // if (StringUtils.isAnyEmpty(packageName, uuid))
        //     return new ResponseEntity<>(packageName + " " + uuid, HttpStatus.BAD_REQUEST);

        QDevice device = QDevice.device;
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(device.packageName.eq(packageName));
        predicate.and(device.uuid.eq(uuid));
        predicate.and(device.versionName.eq(versionName));

        return ResponseEntity.ok(deviceRepository.findOne(predicate).getDeviceId());
    }

    private AttendMap save(String uuid, String packageName, String versionName, AttendMap attendMap) {

        // 기존 여부 확인
        AttendMap find = repository.findOne(new BooleanBuilder()
                .and(QAttendMap.attendMap.attend.eq(attendMap.getAttend()))
                .and(QAttendMap.attendMap.examinee.eq(attendMap.getExaminee()))
        );
        if (find != null) attendMap.set_id(find.get_id());

        if (attendMap.getAttendHall() == null) { // 기존에 존재한 답안지 삭제
            Iterable<AttendPaper> list = paperRepository.findAll(new BooleanBuilder()
                    .and(QAttendPaper.attendPaper.attend.attendCd.eq(attendMap.getAttend().getAttendCd()))
                    .and(QAttendPaper.attendPaper.examinee.examineeCd.eq(attendMap.getExaminee().getExamineeCd()))
            );

            if (list != null && Iterables.size(list) > 0) paperRepository.delete(list);
        }

        // 헤더에 포함된 아래 세 개의 값으로
        // device 테이블의 _id 값을 찾아 저장함
        Device tmp = deviceRepository.findOne(new BooleanBuilder()
                .and(QDevice.device.versionName.eq(versionName))
                .and(QDevice.device.uuid.eq(uuid))
                .and(QDevice.device.packageName.eq(packageName))
        );

        Long deviceId = null;
        if (tmp != null) deviceId = tmp.getDeviceId();

        attendMap.setDeviceId(deviceId);
        return repository.save(attendMap);
    }

    @RequestMapping(value = "detect", method = RequestMethod.GET)
    public Page<AttendMap> detect(@RequestParam(defaultValue = "") String attendCd, @RequestParam(defaultValue = "") String attendHallCd, @PageableDefault Pageable pageable) {

        QAttendMap attendMap = QAttendMap.attendMap;

        BooleanBuilder predicate = new BooleanBuilder()
                .and(attendMap.attend.attendCd.eq(attendCd))
                .and(attendMap.attendHall.hallCd.eq(attendHallCd));

        return repository.findAll(predicate, pageable);
    }
}