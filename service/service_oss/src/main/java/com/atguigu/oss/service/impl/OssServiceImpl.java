package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;


@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        //工具类取值
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try{

            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String fileName = file.getOriginalFilename();
            //System.out.println(fileName);//1541156455818.jpg

            //1 在文件名称中加上随机唯一的值，让每个文件的名都不一样
            String uuid = UUID.randomUUID().toString().replace("-", "");
            fileName = uuid+fileName;
            //System.out.println(fileName);// d5bd01ecdf0a4ac0b177f44ebeb819771541156455818.jpg

            //2 把文件按照日期进行分类
            //获取当前日期
            //2020/12/05
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            //2020/12/05/xxxx.jpg
            fileName = datePath+"/"+fileName;

            //调用oss方法实现上传
            //bucket名称 上传到oss文件路径和文件名称 上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后的文件路径返回
            //需要把上传到阿里云oss路径手动拼接起来
            String url = "http://"+bucketName+"."+endpoint+"/"+fileName;
            //System.out.println("renhai+++"+url);//renhai+++http://edu-renhai.oss-cn-beijing.aliyuncs.com/2020/12/05/d5bd01ecdf0a4ac0b177f44ebeb819771541156455818.jpg
            return url;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
