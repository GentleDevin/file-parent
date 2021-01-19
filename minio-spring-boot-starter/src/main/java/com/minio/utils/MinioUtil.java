package com.minio.utils;

import com.minio.common.FileInfoResult;
import com.minio.common.ResponseResult;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Title: Minio工具类
 * @Description: 封装Minio对桶、文件上传、下载、删除等操作
 * @Author: Devin
 * @CreateDate: 2021/01/07 17:26:46
 **/
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    private static final int DEFAULT_EXPIRY_TIME = 7 * 24 * 3600;

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    public boolean bucketExists(String bucketName) throws InvalidKeyException, ErrorResponseException,
            IllegalArgumentException, InsufficientDataException, InternalException, InvalidBucketNameException,
            InvalidResponseException, NoSuchAlgorithmException, XmlParserException, IOException {
        boolean flag = minioClient.bucketExists(bucketName);
        if (flag) {
            return true;
        }
        return false;
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     * @throws RegionConflictException
     */
    public boolean makeBucket(String bucketName)
            throws InvalidKeyException, ErrorResponseException, IllegalArgumentException, InsufficientDataException,
            InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException,
            XmlParserException, IOException, RegionConflictException {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            minioClient.makeBucket(bucketName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 列出所有存储桶名称
     *
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    public List<String> listBucketNames() throws InvalidKeyException, ErrorResponseException, IllegalArgumentException,
            InsufficientDataException, InternalException, InvalidBucketNameException, InvalidResponseException,
            NoSuchAlgorithmException, XmlParserException, IOException {
        List<Bucket> bucketList = listBuckets();
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 列出所有存储桶
     *
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    public List<Bucket> listBuckets() throws InvalidKeyException, ErrorResponseException, IllegalArgumentException,
            InsufficientDataException, InternalException, InvalidBucketNameException, InvalidResponseException,
            NoSuchAlgorithmException, XmlParserException, IOException {
        return minioClient.listBuckets();
    }

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    public boolean removeBucket(String bucketName) throws InvalidKeyException, ErrorResponseException,
            IllegalArgumentException, InsufficientDataException, InternalException, InvalidBucketNameException,
            InvalidResponseException, NoSuchAlgorithmException, XmlParserException, IOException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                // 有对象文件，则删除失败
                if (item.size() > 0) {
                    return false;
                }
            }
            // 删除存储桶，注意，只有存储桶为空时才能删除成功。
            minioClient.removeBucket(bucketName);
            flag = bucketExists(bucketName);
            if (!flag) {
                return true;
            }

        }
        return false;
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    public List<String> listObjectNames(String bucketName) throws InvalidKeyException, ErrorResponseException,
            IllegalArgumentException, InsufficientDataException, InternalException, InvalidBucketNameException,
            InvalidResponseException, NoSuchAlgorithmException, XmlParserException, IOException {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * 列出存储桶中的所有对象
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    public Iterable<Result<Item>> listObjects(String bucketName) throws InvalidKeyException, ErrorResponseException,
            IllegalArgumentException, InsufficientDataException, InternalException, InvalidBucketNameException,
            InvalidResponseException, NoSuchAlgorithmException, XmlParserException, IOException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.listObjects(bucketName);
        }
        return null;
    }


    /**
     * 根据bucketName获取信息
     *
     * @param bucketName bucket名称
     */
    @SneakyThrows
    public Optional<Bucket> getBucket(String bucketName) {
        return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    @SneakyThrows
    public List getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        List<Item> list = new ArrayList<>();
        Iterable<Result<Item>> objectsIterator = minioClient.listObjects(bucketName, prefix, recursive);
        if (objectsIterator != null) {
            Iterator<Result<Item>> iterator = objectsIterator.iterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    Result<Item> result = iterator.next();
                    Item item = result.get();
                    list.add(item);
                }
            }
        }
        return list;
    }


    /**
     * 通过文件上传到对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param fileName   File name
     * @return
     * @throws InvalidKeyException
     * @throws ErrorResponseException
     * @throws IllegalArgumentException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws InvalidBucketNameException
     * @throws InvalidResponseException
     * @throws NoSuchAlgorithmException
     * @throws XmlParserException
     * @throws IOException
     */
    public boolean putObject(String bucketName, String objectName, String fileName)
            throws InvalidKeyException, ErrorResponseException, IllegalArgumentException, InsufficientDataException,
            InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException,
            XmlParserException, IOException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            minioClient.putObject(bucketName, objectName, fileName, null);
            ObjectStat statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.length() > 0) {
                return true;
            }
        }
        return false;

    }

    /**
     * 通过InputStream上传对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param stream     要上传的流
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    public boolean putObject(String bucketName, String objectName, InputStream stream)
            throws InvalidKeyException, ErrorResponseException, IllegalArgumentException, InsufficientDataException,
            InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException,
            XmlParserException, IOException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            minioClient.putObject(bucketName, objectName, stream, new PutObjectOptions(stream.available(), -1));
            ObjectStat statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.length() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 以流的形式获取一个文件对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    public InputStream getObject(String bucketName, String objectName)
            throws InvalidKeyException, ErrorResponseException, IllegalArgumentException, InsufficientDataException,
            InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException,
            XmlParserException, IOException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            ObjectStat statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.length() > 0) {
                InputStream stream = minioClient.getObject(bucketName, objectName);
                return stream;
            }
        }
        return null;
    }

    /**
     * 以流的形式获取一个文件对象（断点下载）
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度 (可选，如果无值则代表读到文件结尾)
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    public InputStream getObject(String bucketName, String objectName, long offset, Long length)
            throws InvalidKeyException, ErrorResponseException, IllegalArgumentException, InsufficientDataException,
            InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException,
            XmlParserException, IOException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            ObjectStat statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.length() > 0) {
                InputStream stream = minioClient.getObject(bucketName, objectName, offset, length);
                return stream;
            }
        }
        return null;
    }

    /**
     * 下载并将文件保存到本地
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param fileName   File name
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    public boolean getObject(String bucketName, String objectName, String fileName)
            throws InvalidKeyException, ErrorResponseException, IllegalArgumentException, InsufficientDataException,
            InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException,
            XmlParserException, IOException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            ObjectStat statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.length() > 0) {
                minioClient.getObject(bucketName, objectName, fileName);
                return true;
            }
        }
        return false;
    }

    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    @SneakyThrows
    public boolean removeObject(String bucketName, String objectName){
        boolean flag = bucketExists(bucketName);
        if (flag) {
            minioClient.removeObject(bucketName, objectName);
            return true;
        }
        return false;
    }

    /**
     * 删除指定桶的多个文件对象,返回删除错误的对象列表，全部删除成功，返回空列表
     *
     * @param bucketName  存储桶名称
     * @param objectNames 含有要删除的多个object名称的迭代器对象
     * @return
     * @throws InvalidKeyException
     * @throws ErrorResponseException
     * @throws IllegalArgumentException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws InvalidBucketNameException
     * @throws InvalidResponseException
     * @throws NoSuchAlgorithmException
     * @throws XmlParserException
     * @throws IOException
     */
    public List<String> removeObject(String bucketName, List<String> objectNames)
            throws InvalidKeyException, ErrorResponseException, IllegalArgumentException, InsufficientDataException,
            InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException,
            XmlParserException, IOException {
        List<String> deleteErrorNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(bucketName, objectNames);
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                deleteErrorNames.add(error.objectName());
            }
        }
        return deleteErrorNames;
    }

    /**
     * 生成一个给HTTP GET请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     * @throws InvalidExpiresRangeException
     */
    public String presignedGetObject(String bucketName, String objectName, Integer expires)
            throws InvalidKeyException, ErrorResponseException, IllegalArgumentException, InsufficientDataException,
            InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException,
            XmlParserException, IOException, InvalidExpiresRangeException {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            if (expires < 1 || expires > DEFAULT_EXPIRY_TIME) {
                throw new InvalidExpiresRangeException(expires,
                        "expires must be in range of 1 to " + DEFAULT_EXPIRY_TIME);
            }
            url = minioClient.presignedGetObject(bucketName, objectName, expires);
        }
        return url;
    }

    /**
     * 生成一个给HTTP PUT请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行上传，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     * @throws InvalidKeyException
     * @throws ErrorResponseException
     * @throws IllegalArgumentException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws InvalidBucketNameException
     * @throws InvalidResponseException
     * @throws NoSuchAlgorithmException
     * @throws XmlParserException
     * @throws IOException
     * @throws InvalidExpiresRangeException
     */
    public String presignedPutObject(String bucketName, String objectName, Integer expires)
            throws InvalidKeyException, ErrorResponseException, IllegalArgumentException, InsufficientDataException,
            InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException,
            XmlParserException, IOException, InvalidExpiresRangeException {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            if (expires < 1 || expires > DEFAULT_EXPIRY_TIME) {
                throw new InvalidExpiresRangeException(expires,
                        "expires must be in range of 1 to " + DEFAULT_EXPIRY_TIME);
            }
            url = minioClient.presignedPutObject(bucketName, objectName, expires);
        }
        return url;
    }

    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    public ObjectStat statObject(String bucketName, String objectName)
            throws InvalidKeyException, ErrorResponseException, IllegalArgumentException, InsufficientDataException,
            InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException,
            XmlParserException, IOException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            ObjectStat statObject = minioClient.statObject(bucketName, objectName);
            return statObject;
        }
        return null;
    }

    /**
     * 文件访问路径
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     * @throws IOException
     * @throws XmlParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidResponseException
     * @throws InvalidBucketNameException
     * @throws InternalException
     * @throws InsufficientDataException
     * @throws IllegalArgumentException
     * @throws ErrorResponseException
     * @throws InvalidKeyException
     */
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            url = minioClient.getObjectUrl(bucketName, objectName);
        }
        return url;
    }

    /**
     * @Description: 单文件上传
     * @CreateDate: 2021/01/11 16:25:51
     * @param file: 文件类
     * @param bucketName: 桶名称
     * @param configDir: 文件配置路径
     * @return: java.lang.String
     **/
    public ResponseResult fileUpload(MultipartFile file, String bucketName, String configDir) {
        ResponseResult responseResult = new ResponseResult();
        if (file.isEmpty() || file.getSize() == 0) {
            responseResult.put("code", HttpStatus.NO_CONTENT.value());
            responseResult.put("msg", HttpStatus.NO_CONTENT.getReasonPhrase());
            ResponseResult okResponseResult = ResponseResult.ok(responseResult);
            return okResponseResult;
        }
        try {
            if (!bucketExists(bucketName)) {
                makeBucket(bucketName);
            }
            String fileName = file.getOriginalFilename();
            String newName = configDir + UUID.randomUUID().toString().replaceAll("-", "")
                    + fileName.substring(fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            putObject(bucketName, newName, inputStream);
            inputStream.close();
            String url = getObjectUrl(bucketName, newName);
            responseResult.put("code", HttpStatus.OK.value());
            responseResult.put("msg", HttpStatus.OK.getReasonPhrase());
            responseResult.put("data",url);
            ResponseResult okResponseResult = ResponseResult.ok(responseResult);
            return okResponseResult;
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseResult.put("msg", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            ResponseResult okResponseResult = ResponseResult.ok(responseResult);
            return okResponseResult;
        }
    }

    /**
     * @Description: 文件上传
     * @param bucketName: 桶名称
     * @param file: 文件类
     * @param configDir: 文件配置路径
     * @param responseResults: 上传结果信息集合
     * @return: java.util.List<com.minio.common.ResponseResult> 返回上传结果信息
       @CreateDate: 2021/01/08 17:41:42
     **/
    public List<ResponseResult> fileUpload(MultipartFile file,String bucketName, String configDir,List<ResponseResult> responseResults) {
        ResponseResult responseResult = new ResponseResult();
        if (file.isEmpty() || file.getSize() == 0) {
            responseResult.put("code", HttpStatus.NO_CONTENT.value());
            responseResult.put("msg", HttpStatus.NO_CONTENT.getReasonPhrase());
            responseResult.put("data", null);
            responseResults.add(responseResult);
            return responseResults;
        }
        try {
            if (!bucketExists(bucketName)) {
                makeBucket(bucketName);
            }
            FileInfoResult fileInfo = new FileInfoResult();
            initFileInfo(file,fileInfo);
            InputStream inputStream = file.getInputStream();
            //文件名称(相对路径)
            String objectName = configDir+fileInfo.getFileSaveName();
            putObject(bucketName, objectName, inputStream);
            inputStream.close();
            fileInfo.setFileSavePath(getObjectUrl(bucketName, objectName));
            responseResult.put("code", HttpStatus.OK.value());
            responseResult.put("msg", HttpStatus.OK.getReasonPhrase());
            responseResult.put("data",fileInfo);
            responseResults.add(responseResult);
            return responseResults;
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseResult.put("msg", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            responseResult.put("data", null);
            responseResults.add(responseResult);
            return responseResults;
        }
    }

    /**
     * @Description: 多文件上传
     * @CreateDate: 2021/01/13 11:33:03
     * @param file: 文件类
     * @param bucketName: 桶名称
     * @param configDir: 文件配置路径
     * @return: java.lang.String
     **/
    public List<ResponseResult>  multiFileUpload(MultipartFile[] file,String bucketName, String configDir) {
        List<ResponseResult> responseResults = new ArrayList<>();
        for (MultipartFile multipartFile: file) {
            fileUpload(multipartFile,bucketName,configDir,responseResults);
        }
        return responseResults;
    }

    /**
     * @Description: 初始化文件信息类
     * @param file: 文件类
     * @param fileInfo: 文件信息类
     * @return: com.minio.common.FileInfo
     * @CreateDate: 2021/01/13 17:42:21
     **/
    public FileInfoResult initFileInfo(MultipartFile file, FileInfoResult fileInfo) {
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileUploadTime(DateUtils.getSystemCurrentDate());
        fileInfo.setFileSaveName(UUID.randomUUID().toString().replaceAll("-", "")
                + fileInfo.getFileName().substring(fileInfo.getFileName().lastIndexOf(".")));
        fileInfo.setFileStatus(1);
        return fileInfo;
    }

    /**
     * @Description: 文件下载
     * @param bucketName: 文件类
     * @param fileName: 文件信息类
     * @return: com.minio.common.FileInfo
     * @CreateDate: 2021/01/13 17:42:21
     **/
    @SneakyThrows
    public  void download(String bucketName, String fileName, HttpServletResponse response,String openStyle) {
        //获取对象的元数据
        final ObjectStat stat = minioClient.statObject(bucketName, fileName);
        if(openStyle.equals("attachment")) {
            response.setContentType(stat.contentType());
        }else{
            // 在浏览器中打开
            response.setContentType(MediaType.ALL_VALUE);
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, openStyle+";"+"filename=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream is = minioClient.getObject(bucketName, fileName);
        ServletOutputStream os = response.getOutputStream();
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }
}