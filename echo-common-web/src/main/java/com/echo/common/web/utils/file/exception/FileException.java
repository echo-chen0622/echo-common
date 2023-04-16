package com.echo.common.web.utils.file.exception;

import com.echo.common.web.exception.ApiException;

/**
 * 文件信息异常类
 *
 * @author echo
 */
public class FileException extends ApiException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
