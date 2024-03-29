package com.sadalsuud.push.domain.support.file;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @Description 远程读取文件
 * @Author sadalsuud
 * @Blog www.sadalsuud.cn
 * @Date 11/12/2023
 */
public interface FileService {
    File getRemoteUrl2File(String path, String remoteUrl);

    List<File> getRemoteUrl2File(String path, Collection<String> remoteUrls);
}
