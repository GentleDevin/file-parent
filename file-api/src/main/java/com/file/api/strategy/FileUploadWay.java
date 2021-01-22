package com.file.api.strategy;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @Title:
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/20 10:47
 */
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class FileUploadWay {
    @NonNull
    private IFileUpload fileUpload;



}
