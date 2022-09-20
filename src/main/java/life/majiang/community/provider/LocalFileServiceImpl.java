package life.majiang.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import life.majiang.community.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Date: 2022/09/20 01:51
 * @Author: mykko
 */
@Slf4j
@Service
public class LocalFileServiceImpl implements FileService {


    /**
     单独拉出来
     */
    @Value("${file.uploadPath}")
    private String uploadPath;
//    @Value("${file.visitPath}")
//    private String visitPath;

    /**
     * 一二三步是生成文件名
     * 四五六步是生成上传路径
     * 第七步是上传并返回可视路径
     * @param multipartFile
     * @return
     */
    @Override
    public UFileResult upload(MultipartFile multipartFile) {
        // 1、获取真实文件名
        String originalFilename = multipartFile.getOriginalFilename();
        // 2、截取图片的后缀 lastIndexOf最后出现.的位置
        String imgSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 3、生成唯一文件名
        String newFileName = UUID.randomUUID().toString()+imgSuffix;
        // 4、日期目录
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dataPath = dateFormat.format(new Date());
        // 5、指定文件上传后的目录
        File targetPath = new File(uploadPath, dataPath); // 拼接成新目录
        // mkdirs:不存在则创建，mkdir:不存在则返回false
        if(!targetPath.exists()) targetPath.mkdirs(); // 如果目录不存在，则递归创建
        // 6、指定文件上传后的服务器完整路径[包括文件名]
        File TargetFileName = new File(targetPath, newFileName);
        // 7、文件上传到指定目录
        try {
            multipartFile.transferTo(TargetFileName.getAbsoluteFile()); // 文件上传到指定目录
            System.out.println(TargetFileName); // D:\2021\12\05\D:\2021\12\05\68f61bc7-07b9-41c6-876b-22c9a57f5606.png
            String finalFileName = dataPath + "/" + newFileName;
            UFileResult fileResult = new UFileResult();

            fileResult.setFileName(newFileName);
            fileResult.setFileUrl("/"+finalFileName);
            return fileResult;

        } catch (IOException e) {
            log.error("upload error,{}", e);
            return null;
        }
    }


    @Override
    public UFileResult upload(String url) {
        File newFile = FileUtils.newFile(url);
        assert newFile != null;
        UFileResult fileResult;
        try {
            fileResult = upload((MultipartFile) new FileInputStream(newFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("new file exception", e);
        }
        FileUtils.deleteFile(newFile);
        return fileResult;
    }

//    @Override
//    public UFileResult upload(MultipartFile file) {
//
//        // 在 uploadPath 文件夹中通过日期对上传的文件归类保存
//        // 例如：/2022/02/22/df9a66f1-760b-4f95-9faf-b5a216966718.png
////        String format = sdf.format(new Date());
//        File folder = new File(uploadPath);
//        if (!folder.isDirectory()) {
//            folder.mkdirs();
//        }
//
//        // 对上传的文件重命名, 避免文件重名
//        String oldName = file.getOriginalFilename();
//        String newName = UUID.randomUUID().toString()
//                + oldName.substring(oldName.lastIndexOf("."), oldName.length());
//        try {
//            // 文件保存
//            file.transferTo(new File(folder, newName).getAbsoluteFile());
//
//            UFileResult fileResult = new UFileResult();
//
//            fileResult.setFileName(newName);
//            fileResult.setFileUrl(newName);
//            return fileResult;
//
//        } catch (IOException e) {
//            log.error("upload error,{}", e);
//            return null;
//        }
//    }

    @Override
    public UFileResult exchangeFileUrl(String fileName) {
        return null;
    }
}
