package pers.msidolphin.mblog.helper;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by msidolphin on 2018/3/31.
 */
public class FileHelper {

	private static Logger logger = LoggerFactory.getLogger(FileHelper.class);

	private String host;
	private int port;
	private String user;
	private String ftpRoot;

	private String password;

	private FTPClient ftpClient;

	private static final String separator = "/";

	public FileHelper(String host, String user, String password, String ftpRoot) {
		this(host, 21, user, password, ftpRoot);
	}

	public FileHelper(String host, int port, String user, String password, String ftpRoot) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.ftpRoot = ftpRoot;
	}

	public boolean uploadFile(Map<String, File> fileMap) throws IOException {
		FileHelper ftp = new FileHelper(host, 21, user, password, ftpRoot);
		logger.info("开始连接ftp服务器...");
		//ftp服务器上的img目录
		boolean result = ftp.start(fileMap);
		logger.info("开始连接ftp服务器,结束上传,上传结果:{}", result);
		return result;
	}


	/**
	 *
	 * @Title: uploadFile
	 * @Description: 上传文件
	 * @param fileMap
	 * @throws IOException
	 * @return boolean
	 */
	private boolean start(Map<String, File> fileMap) throws IOException {
		FileInputStream inputStream = null;
		boolean uploaded;
		uploaded = connect();
		//连接FTP服务器
		if(uploaded && ftpClient != null) {

			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			try {
				//向ftp服务器上传文件
				for(String path : fileMap.keySet()) {
					//切换工作空间
					if(!cwd(path)) {
						logger.info("未能成功切换到{}工作空间", path);
						continue;
					}
					File file = fileMap.get(path);
					inputStream = new FileInputStream(file);
					ftpClient.storeFile(file.getName(), inputStream);
					logger.info("{}成功上传...", file.getName());
				}
				uploaded = true;
			}catch(Exception e) {
				uploaded = false;
				logger.error("文件上传异常", e);
			}finally {
				//关闭流和连接
				if(inputStream != null) {
					inputStream.close();
				}
				ftpClient.disconnect();
			}
		}
		return uploaded;
	}


	/**
	 *
	 * @Title: connect
	 * @Description: 连接ftp服务器
	 * @return boolean
	 * @throws
	 */
	private boolean connect() {
		boolean isSuccess = false;
		ftpClient = new FTPClient();
		try {
			ftpClient.connect(this.host);
			ftpClient.login(this.user, this.password);
			isSuccess = true;
		} catch (IOException e) {
			logger.error("连接FTP服务器异常",e);
		}
		return isSuccess;
	}

	/**
	 * 创建目录(有则切换目录，没有则创建目录)
	 * @param dir
	 * @return
	 */
	public boolean cwd(String dir) throws IOException, URISyntaxException {
		if(Util.isEmpty(dir)) return true;
		//工作空间真实路径 ftp服务器根路径 + 待上传文件路径
		String realPath = ftpRoot + dir.substring(0, dir.lastIndexOf(separator) + 1);
		if(ftpClient.changeWorkingDirectory(realPath)) return true;
		File file = new File(realPath);
		if (!file.exists()) {
			file.setWritable(true);
			file.mkdirs();
		}
		if(ftpClient.changeWorkingDirectory(realPath)) return true;
		return false;
	}


	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * 生成目录
	 *  一级目录为当前年份
	 * 二级目录为当前的月份01 - 12
	 * 三级目录为hashCode(文件名) % 26 的对应英文字母值
	 * 四级目录为文件扩展名
	 * @param fileName
	 * @param extName
	 * @return
	 */
	@SuppressWarnings("ALL")
	public static String generatePath(String fileName, String extName) {
		StringBuilder path = new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		//获取当前年份
		Integer year = calendar.get(Calendar.YEAR);
		path.append(year + separator);
		//获取当前月份
		Integer month = calendar.get(Calendar.MONTH) + 1;
		if(month < 10) {
			path.append("0" + month + separator);
		}else {
			path.append(month + separator);
		}
		//计算文件hash值
		int hashCode = fileName.hashCode();
		if (hashCode < 0) {
			hashCode = -hashCode;
		}
		//转换为对应英文字母
		char ch = (char)( 65 + (hashCode % 26));
		path.append(ch).append(separator);
		//扩展名为四级目录
		path.append(extName).append(separator);
		return path.toString();
	}



	public static void main(String[] args) {
		//测试
		Map<String, Integer> maps = new HashMap<>();
		String[] exts = {"jpg", "gif", "png", "bmp"};
		int N = 10000;
		for(int i = 0 ; i < N ; ++i) {
			String path = FileHelper.generatePath(UUID.randomUUID().toString(), exts[i%4]);
			if(maps.containsKey(path)) {
				maps.put(path, maps.get(path) + 1);
			}else {
				maps.put(path, 1);
			}
		}
		
		for(String path : maps.keySet()) {
			System.out.println(path + ": " + maps.get(path));
		}
	}

	public static void transfer(MultipartFile multipartFile, File file) throws IOException {
		BufferedOutputStream bufferedOutputStream = null;
		try {
		bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			byte[] bytes = multipartFile.getBytes();
			bufferedOutputStream.write(bytes);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			if (bufferedOutputStream != null) {
				bufferedOutputStream.close();
			}
		}
	}
}
