package com.daesang.springbatch.common.util;

import MarkAny.MaSaferJava.MaFileChk;
import MarkAny.MaSaferJava.Madec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * fileName			: MarkAnyUtil
 * author			: 최종민차장
 * date				: 2022-11-30
 * descrition       : Drm 해제 Util
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-30			최종민차장             최초생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarkAnyUtil {

    @Value("${file.datpath}")
    private String MarkAnyDrmInfo;

    /**
     * 복호화.
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public void decrypt(String filePath, String fileName, File fileSample) throws Exception {
        Madec clMadec = null; // 클래스 생성 준비
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        String strRetCode = "";
        long OutFileLength = 0;

        try{
            String extension = FilenameUtils.getExtension(fileName);
            // Sample Parameter
            String FileName = new String( filePath+fileName ); // 복호화 파일 명( 임의의 값 )
            //File fileSample = new File( filePath+fileName ); // 복호화 대상 파일

            File FileOut = new File( FileName+".tmp.markany"); // 복호화 결과로 생성할 파일

            if( fileSample.length( ) == 0 )
            {
                log.info( "MarkAny:ERR 파일크기 에러입니다." );
                throw new Exception("MarkAny:ERR 파일크기 에러입니다.");
            }

            try
            {
                in = new BufferedInputStream( new FileInputStream( fileSample ) );
                out = new BufferedOutputStream( new FileOutputStream( FileOut ) );
            }
            catch( Exception e )
            {
                log.info( "MarkAny:스트림 객체 생성 에러입니다." );
                throw new Exception("MarkAny:스트림 객체 생성 에러입니다.");
            }

            // create instance
            // 복호화 클래스 생성
            try
            {
                clMadec = new Madec( MarkAnyDrmInfo ); // 연동 시 절대경로로 변경

            }
            catch( Exception e )
            {
                log.info( "MarkAny:마크애니 복호화 클래스 생성 에러입니다." );
                log.info( "MarkAny:MarkAnyDrmInfo.dat 파일의 경로와 권한을 확인 해 주세요." );
                throw new Exception("MarkAny:마크애니 복호화 클래스 생성 에러입니다. MarkAnyDrmInfo.dat 파일의 경로와 권한을 확인 해 주세요.");
            }

            // 복호화 대상 파일의 크기를 가져옵니다.
            long lFileLen = fileSample.length( );

            // 복호화 및 파라미터 점검을 합니다.
            OutFileLength = clMadec.lGetDecryptFileSize( FileName, lFileLen, in );

            // 복호화 준비를 합니다.
            if( OutFileLength > 0 )
            {
                // 복호화 합니다.
                strRetCode = clMadec.strMadec( out );
            }
            else // 복호화 시작전 에러가 발생했습니다.
            {
                strRetCode = clMadec.strGetErrorCode( );
                if (!strRetCode.equals("90005")){ // 일반파일이 아닐경우 (90005 일반파일경우)
                    log.info( "MarkAny:복호화 시작 전에 실패 하였습니다." );
                    log.info( "ERR [ErrorCode] = [" + strRetCode + "]"
                            + "[ErrorDescription] = ["
                            + clMadec.strGetErrorMessage(strRetCode) + "]" );
                    throw new Exception("MarkAny:복호화 시작 전에 실패 하였습니다."
                            + "ERR [ErrorCode] = [" + strRetCode + "]"
                            + "[ErrorDescription] = ["
                            + clMadec.strGetErrorMessage(strRetCode) + "]" );
                }
            }

            if( strRetCode.equals( "00000" ) )
            {
                // 복호화를 성공했습니다.
                log.info( "MarkAny:복호화에 성공 하였습니다." );
                log.info( "MarkAny:RetCode = [" + strRetCode + "]" );

                // 암호화 파일을 복호화 파일로 대체한다.
                fileSample.delete();
                FileOut.renameTo(fileSample);
            }
            else if( strRetCode.equals( "90005" ) ){ // 일반파일인 경우
                log.info( "일반 파일 입니다. ");
                FileOut.delete( );
            }
            else
            {
                // 복호화에 실패했습니다.
                FileOut.delete( );
                fileSample.delete();
                log.info( "MarkAny:복호화에 실패 하였습니다." );
                log.info( "MarkAny:ERR [ErrorCode] = [" + strRetCode + "]"
                        + "[ErrorDescription] = ["
                        + clMadec.strGetErrorMessage(strRetCode) + "]" );

                throw new Exception("MarkAny:복호화에 실패 하였습니다."
                        + "ERR [ErrorCode] = [" + strRetCode + "]"
                        + "[ErrorDescription] = ["
                        + clMadec.strGetErrorMessage(strRetCode) + "]" );
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
        finally {
            if (in != null ) in.close();
            if (out != null)  out.close();
        }
        return;

    }

    /**
     * 파일상태 체크한다.
     * @param filePath
     * @param fileName
     * @return
     */
    public String checkFile(String filePath,String fileName){
        MaFileChk clMaFileChk = null; // 클래스 생성 준비
        BufferedInputStream in = null;
        String strRetCode = "";
        long OutFileLength = 0;
        String retValue  = "0";
        // Sample Parameter
        String FileName = new String(filePath+fileName); // 파일체크 대상 파일
        File FileSample = new File( filePath+fileName ); // 파일체크 대상 파일
        if( FileSample.length() == 0 )
        {
            log.info("MarkAny체크:ERR 파일크기 에러입니다.");
            return retValue;
        }

        try
        {
            in = new BufferedInputStream( new FileInputStream( FileSample ) );
        }
        catch( Exception e )
        {
            log.info("MarkAny체크:스트림 객체 생성 에러입니다.");
            return retValue;
        }

        // create instance
        // 파일체크 클래스 생성
        try
        {
            clMaFileChk = new MaFileChk( MarkAnyDrmInfo ); // 연동 시 절대경로로 변경
        }
        catch( Exception e )
        {
            log.info( "MarkAny체크:마크애니 파일체크 클래스 생성 에러입니다." );
            log.info( "MarkAny체크:MarkAnyDrmInfo.dat 파일의 경로와 권한을 확인 해 주세요." );
            return retValue;
        }

        // 파일체크 대상 파일의 크기를 가져옵니다.
        long lFileLen = FileSample.length( );

        // 파일체크 및 파라미터 점검을 합니다.
        log.info("================="+FileName+"========================");

        OutFileLength = clMaFileChk.lGetFileChkFileSize( FileName, lFileLen, in );

        // 파일체크 준비를 합니다.
        if( OutFileLength > 0 )
        {
            // 파일을 체크 합니다.
            strRetCode = clMaFileChk.strMaFileChk( );
        }
        else // 파일체크 시작전 에러가 발생했습니다.
        {
            log.info( "MarkAny체크:파일체크 시작 전에 실패 하였습니다." );
            strRetCode = clMaFileChk.strGetErrorCode( );
            log.info( "MarkAny체크:ERR [ErrorCode] = [" + strRetCode + "]"
                    + "[ErrorDescription] = ["
                    + clMaFileChk.strGetErrorMessage(strRetCode) + "]" );
            return retValue;
        }

        if( strRetCode.equals( "00000" ) )
        {
            // 파일체크를 성공하였습니다.
            log.info( "MarkAny체크:파일체크를 성공 하였습니다. 해당 파일은 암호화 파일 입니다." );
            // 암호 파일의 속성 체크
            log.info( "strGetUserID() = [" + clMaFileChk.strGetUserID() + "]" );
            log.info( "strGetMultiUserID() = [" + clMaFileChk.strGetMultiUserID() + "]" );
            log.info( "strGetEnterpriseID() = [" + clMaFileChk.strGetEnterpriseID() + "]" );
            log.info( "strGetCompanyID() = [" + clMaFileChk.strGetCompanyID() + "]" );
            log.info( "strGetGroupID() = [" + clMaFileChk.strGetGroupID() + "]" );
            log.info( "strGetDeptID() = [" + clMaFileChk.strGetDeptID() + "]" );
            log.info( "strGetPositionID() = [" + clMaFileChk.strGetPositionID() + "]" );
            log.info( "strGetPositionLevel() = [" + clMaFileChk.strGetPositionLevel() + "]" );
            log.info( "strGetSecurityLevel() = [" + clMaFileChk.strGetSecurityLevel() + "]" );
            log.info( "strGetDocumentGrade() = [" + clMaFileChk.strGetDocumentGrade() + "]" );
            log.info( "strGetDocExchangePolicy() = [" + clMaFileChk.strGetDocExchangePolicy() + "]" );
            log.info( "strGetMachineKey() = [" + clMaFileChk.strGetMachineKey() + "]" );
            log.info( "strGetDocumentKey() = [" + clMaFileChk.strGetDocumentKey() + "]" );
            log.info( "strGetCreatorID() = [" + clMaFileChk.strGetCreatorID() + "]" );
            log.info( "strGetCreatorCompanyID() = [" + clMaFileChk.strGetCreatorCompanyID() + "]" );
            log.info( "strGetCreatorDeptID() = [" + clMaFileChk.strGetCreatorDeptID() + "]" );
            log.info( "strGetCreatorGroupID() = [" + clMaFileChk.strGetCreatorGroupID() + "]" );
            log.info( "strGetCreatorPositionID() = [" + clMaFileChk.strGetCreatorPositionID() + "]" );
            log.info( "strGetCreateBy() = [" + clMaFileChk.strGetCreateBy() + "]" );
            retValue = "1";
            return strRetCode;
        }
        else if( strRetCode.equals( "90005" ) ){
            // 암호화 되지 않은 파일일 경우 
            return strRetCode;
        }else
        {
            // 파일체크를 실패했습니다.
            log.info( "MarkAny체크:파일체크를 실패 하였습니다." );
            log.info( "MarkAny체크:ERR [ErrorCode] = [" + strRetCode + "]"
                    + "[ErrorDescription] = ["
                    + clMaFileChk.strGetErrorMessage(strRetCode) + "]" );
            return strRetCode;
        }
    }
}
