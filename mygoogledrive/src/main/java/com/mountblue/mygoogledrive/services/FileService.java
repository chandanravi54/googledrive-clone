package com.mountblue.mygoogledrive.services;

import com.mountblue.mygoogledrive.entities.File;
import com.mountblue.mygoogledrive.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public File createFile(File file) {
        return fileRepository.save(file);
    }

    public List<File> getAllFiles(String q) {
        if(q.length()>=5) {
            if (q.substring(5).equals("image")) {
                List<String> fileTypes = Arrays.asList("jpg", "jpeg", "png", "svg", "raw", "psd", "bmp",
                        "tiff", "tif", "exif", "ppm", "pgm", "hdr", "webp");
                return fileRepository.findByFileTypeIn(fileTypes);
            } else if (q.substring(5).equals("video")) {
                List<String> fileTypes = Arrays.asList(
                        "mp4", "avi", "mov", "wmv", "flv", "mkv", "mpeg", "mpg",
                        "webm", "3gp", "m4v", "ogv", "rm", "swf", "vob", "asf",
                        "m2v", "ts", "mxf", "mpg2", "mpg4");
                return fileRepository.findByFileTypeIn(fileTypes);
            } else if (q.substring(5).equals("pdf")) {
                List<String> fileTypes = Arrays.asList("pdf");
                return fileRepository.findByFileTypeIn(fileTypes);
            } else if (q.substring(5).equals("zip")) {
                List<String> fileTypes = Arrays.asList("zip", "gzip");
                return fileRepository.findByFileTypeIn(fileTypes);
            } else if (q.substring(5).equals("document")) {
                List<String> fileTypes = Arrays.asList("html", "css", "js", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "odt", "ods", "odp", "txt", "rtf");
                return fileRepository.findByFileTypeIn(fileTypes);
            } else if (q.substring(5).equals("all")) {
                return fileRepository.findByIsTrashedFalse();
            }else if(q.equals("today")){
                return fileRepository.getDataOnOrAfterUpdatedAt(LocalDate.now());
            }else if(q.equals("last-weak")){
                LocalDate specificDate = LocalDate.now().minus(7, ChronoUnit.DAYS);
                return fileRepository.getDataOnOrAfterUpdatedAt(specificDate);
            }else if(q.equals("last-month")){
                return fileRepository.getDataOnOrAfterUpdatedAt(LocalDate.now().minus(30, ChronoUnit.DAYS));
            }
        }else if(q.length()==0){
            return fileRepository.findByIsTrashedFalse();
        }
            return fileRepository.findByFileNameContaining(q);
        }



    public File findFileByFileId(long id) {
        return fileRepository.findById(id).get();
    }

    public boolean move(long id) {
        File file = fileRepository.findById(id).get();
        if (file.getTrashed()) {
            file.setTrashed(false);
        } else {
            file.setTrashed(true);
        }
        file.setUpdatedAt(LocalDate.now());
        fileRepository.save(file);
        return true;
    }

    public boolean delete(long id) {
        fileRepository.deleteById(id);
        return true;
    }

    public List<File> getTrashedFiles() {
        return fileRepository.findByIsTrashedTrue();
    }


    public void renameFile(long id, String name) {
        File file = fileRepository.findById(id).get();
        file.setFileName(name);
        file.setUpdatedAt(LocalDate.now());
        fileRepository.save(file);
    }
}
