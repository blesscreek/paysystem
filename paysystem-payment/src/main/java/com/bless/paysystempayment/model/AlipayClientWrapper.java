package com.bless.paysystempayment.model;

import com.alipay.api.*;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.model.params.alipay.AlipayNormalMchParams;
import com.bless.paysystemcore.utils.SpringBeansUtil;
import com.bless.paysystempayment.exception.ChannelException;
import com.bless.paysystempayment.util.ChannelCertConfigKitBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static com.bless.paysystemcore.model.params.alipay.AlipayConfig.*;

/**
 * @Author bless
 * @Version 1.0
 * @Description 支付宝Client 包装类
 * @Date 2024-08-10 11:54
 */
@Slf4j
@Data
@AllArgsConstructor
public class AlipayClientWrapper {

    //默认为 不使用证书方式
    private Byte useCert = CS.NO;

    /** 缓存支付宝client 对象 **/
    private AlipayClient alipayClient;

    /** 封装支付宝接口调用函数 **/
    public <T extends AlipayResponse> T execute(AlipayRequest<T> request){

        try {

            T alipayResp = null;

            if(useCert != null && useCert == CS.YES){ //证书加密方式
                alipayResp = alipayClient.certificateExecute(request);

            }else{ //key 或者 空都为默认普通加密方式
                alipayResp = alipayClient.execute(request);
            }

            return alipayResp;

        } catch (AlipayApiException e) { // 调起接口前出现异常，如私钥问题。  调起后出现验签异常等。

            log.error("调起支付宝execute[AlipayApiException]异常！", e);
            //如果数据返回出现验签异常，则需要抛出： UNKNOWN 异常。
            throw ChannelException.sysError(e.getMessage());

        } catch (Exception e) {
            log.error("调起支付宝execute[Exception]异常！", e);
            throw ChannelException.sysError("调用支付宝client服务异常");
        }
    }



    /*
     * 构建支付宝client 包装类
     *
     * @author terrfly
     * @site https://www.jeequan.com
     * @date 2021/6/8 17:46
     */
    public static AlipayClientWrapper buildAlipayClientWrapper(Byte useCert, Byte sandbox, String appId, String privateKey, String alipayPublicKey, String signType, String appCert,
                                                               String alipayPublicCert, String alipayRootCert){

        //避免空值
        sandbox = sandbox == null ? CS.NO : sandbox;

        AlipayClient alipayClient = null;
//        if(useCert != null && useCert == CS.YES){ //证书的方式
//
//            ChannelCertConfigKitBean channelCertConfigKitBean = SpringBeansUtil.getBean(ChannelCertConfigKitBean.class);
//
//            CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
//            certAlipayRequest.setServerUrl(sandbox == CS.YES ? SANDBOX_SERVER_URL : PROD_SERVER_URL);
//            certAlipayRequest.setAppId(appId);
//            certAlipayRequest.setPrivateKey(privateKey);
//            certAlipayRequest.setFormat(FORMAT);
//            certAlipayRequest.setCharset(CHARSET);
//            certAlipayRequest.setSignType(signType);
//
//            certAlipayRequest.setCertPath(channelCertConfigKitBean.getCertFilePath(appCert));
//            certAlipayRequest.setAlipayPublicCertPath(channelCertConfigKitBean.getCertFilePath(alipayPublicCert));
//            certAlipayRequest.setRootCertPath(channelCertConfigKitBean.getCertFilePath(alipayRootCert));
//
//
//            try {
//                alipayClient = new DefaultAlipayClient(certAlipayRequest);
//            } catch (AlipayApiException e) {
//                log.error("error" ,e);
//                alipayClient = null;
//            }
//        }else{
            alipayClient = new DefaultAlipayClient(sandbox == CS.YES ? SANDBOX_SERVER_URL : PROD_SERVER_URL
                    , appId, privateKey, FORMAT,  CHARSET,
                    alipayPublicKey, signType);
//        }

        return new AlipayClientWrapper(useCert, alipayClient);
    }




//    public static AlipayClientWrapper buildAlipayClientWrapper(AlipayIsvParams alipayParams){
//
//        return buildAlipayClientWrapper(
//                alipayParams.getUseCert(), alipayParams.getSandbox(), alipayParams.getAppId(), alipayParams.getPrivateKey(),
//                alipayParams.getAlipayPublicKey(), alipayParams.getSignType(), alipayParams.getAppPublicCert(),
//                alipayParams.getAlipayPublicCert(), alipayParams.getAlipayRootCert()
//        );
//    }

    public static AlipayClientWrapper buildAlipayClientWrapper(AlipayNormalMchParams alipayParams){

        return buildAlipayClientWrapper(
                alipayParams.getUseCert(), alipayParams.getSandbox(), alipayParams.getAppId(), alipayParams.getPrivateKey(),
                alipayParams.getAlipayPublicKey(), alipayParams.getSignType(), alipayParams.getAppPublicCert(),
                alipayParams.getAlipayPublicCert(), alipayParams.getAlipayRootCert()
        );
    }
}
