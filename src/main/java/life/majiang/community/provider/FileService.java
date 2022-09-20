package life.majiang.community.provider;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Date: 2022/09/20 01:48
 * @Author: mykko
 */

public interface FileService {
    UFileResult upload(String url);

    UFileResult upload(MultipartFile file);

    UFileResult exchangeFileUrl(String fileName);
}
