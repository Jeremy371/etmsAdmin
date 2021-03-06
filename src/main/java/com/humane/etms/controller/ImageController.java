package com.humane.etms.controller;

import com.humane.etms.model.*;
import com.humane.etms.repository.AttendHallRepository;
import com.humane.etms.repository.AttendMapRepository;
import com.humane.etms.service.ImageService;
import com.humane.util.file.FileNameEncoder;
import com.humane.util.file.FileUtils;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "image", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageController {

    @Value("${path.image.examinee:C:/api/image/examinee}")
    String pathExaminee;
    @Value("${path.image.noIdCard:C:/api/image/noIdCard}")
    String pathNoIdCard;
    @Value("${path.image.recheck:C:/api/image/recheck}")
    String pathRecheck;
    @Value("${path.image.signature:C:/api/image/signature}")
    String pathSignature;
    @Value("${path.image.univLogo:C:/api/image/univLogo}")
    String pathUnivLogo;

    private final ImageService imageService;
    private final AttendHallRepository attendHallRepository;
    private final AttendMapRepository attendMapRepository;

    public String toPath(String admissionCd) {

        String path;

        if (admissionCd == null || admissionCd.isEmpty()) {
            path = pathExaminee;
        } else {
            path = pathExaminee + "/" + admissionCd;
        }

        return path;
    }

    @RequestMapping(value = "examinee/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> examinee(@RequestParam(value = "admissionCd", required = false) String admissionCd, @PathVariable("fileName") String fileName) {

        String path;

        if (admissionCd == null || admissionCd.isEmpty()) {
            List<AttendMap> attendMapList = (List<AttendMap>) attendMapRepository.findAll(new BooleanBuilder()
                    .and(QAttendMap.attendMap.examinee.examineeCd.eq(fileName.substring(0, fileName.length() - ".jpg".length())))
            );

            path = toPath(attendMapList.get(0).getAttend().getAdmission().getAdmissionCd());

        } else {
            path = toPath(admissionCd);
        }

        return imageService.toResponseEntity(path, fileName);
    }

    @RequestMapping(value = "univLogo", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)

    public ResponseEntity<InputStreamResource> univLogo() {
        return imageService.toResponseEntity(pathUnivLogo, "univLogo.png");
    }

    @RequestMapping(value = "noIdCard", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> noIdCard(@RequestParam("file") MultipartFile file) {
        try {
            FileUtils.saveFile(pathNoIdCard, file, false);
            return ResponseEntity.ok(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "noIdCard/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> noIdCard(@PathVariable("fileName") String fileName) {
        return imageService.toResponseEntity(pathNoIdCard, fileName);
    }

    @RequestMapping(value = "signature", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> signature(@RequestHeader("attendCd") String attendCd, @RequestHeader("hallCd") String hallCd, @RequestParam("file") MultipartFile file) {
        try {
            FileUtils.saveFile(pathSignature, file, false);

            QAttendHall qAttendHall = QAttendHall.attendHall;

            AttendHall attendHall = attendHallRepository.findOne(
                    new BooleanBuilder()
                            .and(qAttendHall.attend.attendCd.eq(attendCd))
                            .and(qAttendHall.hall.hallCd.eq(hallCd)));

            if (attendHall != null) {
                attendHall.setSignDttm(new Date());
                attendHallRepository.save(attendHall);
            }

            return ResponseEntity.ok(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "signature/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> signature(@PathVariable("fileName") String fileName) {
        return imageService.toResponseEntity(pathSignature, fileName);
    }
}
