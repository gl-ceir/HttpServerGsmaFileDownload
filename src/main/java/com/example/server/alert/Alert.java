package com.example.server.alert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class Alert {
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Value("${eirs.alert.url}")
    String alertUrl;


    private RestTemplate restTemplate = null;

    public void raiseAnAlert(final String alertId, final String alertMessage, final String alertProcess, final int userId) {

        AlertDto alertDto = new AlertDto();
        alertDto.setAlertId(alertId);
        alertDto.setUserId(String.valueOf(userId));
        alertDto.setAlertMessage(alertMessage);
        alertDto.setAlertProcess(alertProcess);


        long start = System.currentTimeMillis();
        try {
            SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
            clientHttpRequestFactory.setConnectTimeout(1000);
            clientHttpRequestFactory.setReadTimeout(1000);
            restTemplate = new RestTemplate(clientHttpRequestFactory);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AlertDto> request = new HttpEntity<AlertDto>(alertDto, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(alertUrl, request, String.class);
            logger.info("Alert Sent Request:{}, TimeTaken:{} Response:{}", alertDto, responseEntity, (System.currentTimeMillis() - start));
        } catch (org.springframework.web.client.ResourceAccessException resourceAccessException) {
            logger.error("Error while Sending Alert resourceAccessException:{} Request:{}", resourceAccessException.getMessage(), alertDto, resourceAccessException);
        } catch (Exception e) {
            logger.error("Error while Sending Alert Error:{} Request:{}", e.getMessage(), alertDto, e);
        }

    }
}


class AlertDto {

    private String alertId;

    private String alertMessage;

    private String alertProcess;

    private String userId;

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }


    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getAlertProcess() {
        return alertProcess;
    }

    public void setAlertProcess(String alertProcess) {
        this.alertProcess = alertProcess;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}


//
//public class Alert {
//
//
//    final private static Logger logger = LogManager.getLogger(Alert.class);
//
//
//    public void raiseAnAlert(String alertCode, String alertMessage, String alertProcess, int userId) {
//        try {   // <e>  alertMessage    //      <process_name> alertProcess
//            String path = System.getenv("APP_HOME") + "alert/start.sh";
//            ProcessBuilder pb = new ProcessBuilder(path, alertCode, alertMessage, alertProcess, String.valueOf(userId));
//            Process p = pb.start();
//            logger.error(p);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String line = null;
//            String response = null;
//            while ((line = reader.readLine()) != null) {
//                response += line;
//            }
//            logger.info("Alert is generated :response " + response);
//        } catch (Exception ex) {
//            logger.error("Not able to execute Alert management jar ", ex.getLocalizedMessage() + " ::: " + ex.getMessage());
//        }
//    }
//
//}
