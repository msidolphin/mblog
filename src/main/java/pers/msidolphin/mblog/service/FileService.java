package pers.msidolphin.mblog.service;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pers.msidolphin.mblog.exception.ServiceException;
import pers.msidolphin.mblog.helper.FileHelper;
import pers.msidolphin.mblog.helper.MD5Helper;
import pers.msidolphin.mblog.helper.PropertiesHelper;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by msidolphin on 2018/3/31.
 */
@Service
public class FileService {

	private Logger logger = LoggerFactory.getLogger(FileService.class);

	@Autowired
	private FileHelper fileHelper;

	@Autowired
	private PropertiesHelper propertiesHelper;

	@Autowired
	private ServletContext servletContext;

	public String uploadImage(MultipartFile multipartFile) {
		String fileName   = multipartFile.getOriginalFilename();
		String extName    = fileName.substring(fileName.lastIndexOf(".") + 1);
		String uploadName = MD5Helper.md5EncodeWithUtf8(fileName) + "." + extName;
		//临时目录
		String tmpdir = propertiesHelper.getValue("blog.upload.tmpdir");
		File tmpFile = new File(servletContext.getRealPath(tmpdir));
		if (!tmpFile.exists()) {
			tmpFile.setWritable(true);
			tmpFile.mkdirs();
		}

		File file = new File(tmpFile.getAbsolutePath() + "/" + uploadName);
		String remoteFilePath = null;
		try {
			FileHelper.transfer(multipartFile, file);
			logger.info("上传文件{}, 上传路径{}, 上传文件名{}", fileName, tmpdir + uploadName, uploadName);
			//上传到FTP服务器
			Map<String, File> fileMap = new HashMap<>();
			remoteFilePath = FileHelper.generatePath(fileName, extName) + uploadName;
			fileMap.put(remoteFilePath, file);
			fileHelper.uploadFile(fileMap);
			//把文件从upload目录删除
			file.delete();
			logger.info("文件{}已从{}目录下删除", uploadName, tmpdir + uploadName);
		}catch (Exception e) {
			throw new ServiceException(e);
		}

		return remoteFilePath;
	}

	public String uploadImage(File file) throws IOException {
		String fileName   = file.getName();
		String extName    = fileName.substring(fileName.lastIndexOf(".") + 1);
		//生成上传至ftp服务器的路径
		String remoteFilePath = FileHelper.generatePath(fileName, extName) + fileName;
		Map<String, File> fileMap = new HashMap<>();
		fileMap.put(remoteFilePath, file);
		fileHelper.uploadFile(fileMap);
		//把文件从upload目录删除
		file.delete();
		return remoteFilePath;
	}



}
