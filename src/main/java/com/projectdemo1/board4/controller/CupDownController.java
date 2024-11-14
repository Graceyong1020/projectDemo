package com.projectdemo1.board4.controller;


import com.projectdemo1.board4.dto.CboardDTO;
import com.projectdemo1.board4.dto.upload.CuploadFileDTO;
import com.projectdemo1.board4.dto.upload.CuploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/upload")
public class CupDownController {

    @Value("${com.projectdemo1.board4.upload.path}")
    private String uploadPath;

   /* @Autowired
    private CboardService cboardService;*/

    @GetMapping("/uploadForm")
    public void uploadForm() {
    }

    @PostMapping( "/uploadPro")
    public void uploadPro(CuploadFileDTO cuploadFileDTO, CboardDTO cboardDTO,
                          Model model) {
        log.info(cboardDTO);

        if (cuploadFileDTO.getFiles() != null) {
            final List<CuploadResultDTO> list = new ArrayList<>();

            cuploadFileDTO.getFiles().forEach(multipartFile -> {
                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);

                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);
                boolean image = false;
                try {
                    multipartFile.transferTo(savePath);
                    if (Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 100, 100);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(CuploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalName)
                        .image(image)
                        .build()
                );
                model.addAttribute("list", list);
                model.addAttribute("uploadPath", uploadPath);


            });
        }

    }
    @GetMapping("/view/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();


        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }
    @GetMapping("/remove")
    public String removeFile(@RequestParam("fileName") String fileName){
        log.info(fileName);


        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();
        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;
        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();
            if(contentType.startsWith("image")){
                String fileName1 = fileName.replace("s_", "");
                File originalFile = new File(uploadPath+File.separator + fileName1);
                originalFile.delete();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        resultMap.put("result", removed);
        return "/upload/uploadForm";
    }
}