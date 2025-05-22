package com.agogweasellane.interlocking.component;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.agogweasellane.interlocking.controllers.FileDto;
import com.agogweasellane.interlocking.global.DefaultInterface;
import com.agogweasellane.interlocking.global.MediaTypeEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UtilityComponent
{
    public boolean isRegular(int arg)
    {
        return (arg<0)?false:true;
    }
    public boolean isRegular(String arg)
    {
        if(arg==null) return false;
        else if(arg.length()==0) return false;
        
        return true;
    }

    public String generateSalt()
    {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 일반적으로 16바이트?
        random.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }

    public String getSHA256(String argOrig) throws NoSuchAlgorithmException
    {//CASE. 복호화 불필요한 경우.
        return getSHA256(argOrig, generateSalt());
    }
    public String getSHA256(String argOrig, String argSalt) throws NoSuchAlgorithmException
    {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] saltBytes = Base64.getUrlDecoder().decode(argSalt);
        digest.update(saltBytes); // salt 먼저
        byte[] passwordBytes = argOrig.getBytes(StandardCharsets.UTF_8);
        byte[] hashedBytes = digest.digest(passwordBytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(hashedBytes);// Base64 URL 안전 인코딩. 파일명으로 가능하게
        //return Base64.getEncoder().encodeToString(hashedBytes);
    }

    public String getURLEncode(String argString)
    {
        String ret = null;
        try {
            ret = URLEncoder.encode(argString, "UTF-8");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ret;
    }

    public String getURLDecode(String argString)
    {
        String ret = null;
        try {
            if(argString.contains("%")==false)
            {
                return argString;
            }

            ret = URLDecoder.decode(argString, "UTF-8");
        } catch (Exception e) {
            log.error(e.getMessage());
            //e.printStackTrace();
        }
        return ret;
    }

    public FileDto[] multipartFileToFileDto(MultipartFile[] argFiles)
    {
        return Arrays.stream(argFiles)
										.filter(file -> !file.isEmpty())
										.map(file -> {
											try {
												MediaTypeEnum.isMedia(file.getOriginalFilename());
												String origName = file.getOriginalFilename();
												String withoutExtension = origName.substring(0, origName.lastIndexOf(DefaultInterface.TOKEN_DOT));
												String extension = origName.substring(origName.lastIndexOf(DefaultInterface.TOKEN_DOT)).toLowerCase();
												extension = extension.replaceFirst(DefaultInterface.TOKEN_DOT, "");

												byte[] bytes = file.getBytes();

												Path tempFile = Files.createTempFile(null, String.format(DefaultInterface.FORM_FILE, withoutExtension, extension));
                								Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
												Files.deleteIfExists(tempFile);
												return FileDto.builder()
																.fullName(String.format(DefaultInterface.FORM_FILE, withoutExtension, extension))
																.name(withoutExtension).format(extension)
																.contentType( MediaTypeEnum.getContentType(origName) ).bytes(bytes)
																.build();
											} catch (IOException e) {
												log.error(file.getOriginalFilename(), e);
												return null;
											}
										})
										.filter(file -> file != null) // 변환 실패한거 포함X
										.toArray(FileDto[]::new);
    }
}
