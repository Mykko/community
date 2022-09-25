package life.majiang.community.controller;

import life.majiang.community.dto.FileDTO;
import life.majiang.community.model.User;
import life.majiang.community.provider.FileService;
import life.majiang.community.provider.UFileResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;


@Controller
@Slf4j
public class FileController {
    @Autowired
    private FileService fileService;



    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {

            User user = (User) request.getSession().getAttribute("user");


            UFileResult uFileResult = fileService.upload(file);

            // 返回上传文件的访问路径
            // 例如：http://localhost:9999/2022/02/22/df9a66f1-760b-4f95-9faf-b5a216966718.png
            String filePath = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + request.getContextPath()  + "/uploaddir" + uFileResult.getFileUrl();

            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(filePath);
            return fileDTO;
        } catch (Exception e) {
            log.error("upload error", e);
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
            return fileDTO;
        }
    }
}
