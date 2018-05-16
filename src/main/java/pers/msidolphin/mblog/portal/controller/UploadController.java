package pers.msidolphin.mblog.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.helper.PropertiesHelper;
import pers.msidolphin.mblog.service.FileService;

/**
 * Created by msidolphin on 2018/4/3.
 */
@RestController
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:8080", "http://localhost:9200", "http://localhost:9000"}, maxAge = 3600)
@RequestMapping("/upload")
public class UploadController {

	@Autowired
	private FileService fileService;

	@Autowired
	private PropertiesHelper propertiesHelper;

	@PostMapping("")
	public ServerResponse<?> upload(@RequestParam("file") MultipartFile multipartFile) {
		return ServerResponse.success(propertiesHelper.getValue("blog.image.server") + fileService.uploadImage(multipartFile));
	}
}
