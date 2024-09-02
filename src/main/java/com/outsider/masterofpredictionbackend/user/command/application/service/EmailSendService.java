package com.outsider.masterofpredictionbackend.user.command.application.service;


import com.outsider.masterofpredictionbackend.config.RedisConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.outsider.masterofpredictionbackend.user.command.application.dto.EmailAuthDTO;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
@Profile("prod")
public class EmailSendService {
    private JavaMailSender javaMailSender;
    private RedisConfig redisConfig;

    /* 이메일 인증에 필요한 정보 */
    @Value("${spring.mail.username}")
    private String serviceName;

    public EmailSendService(JavaMailSender javaMailSender, RedisConfig redisConfig) {
        this.javaMailSender = javaMailSender;
        this.redisConfig = redisConfig;
    }

    /* 랜덤 인증번호 생성 */
    public Integer makeRandomNum() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        return Integer.parseInt(randomNumber);
    }
    public void setData(String key, EmailAuthDTO data) {
        ValueOperations<String, Object> ops = redisConfig.redisTemplate().opsForValue();
        ops.set(key, data , 180, TimeUnit.SECONDS);
    }
    public void updateData(String key, EmailAuthDTO data) {
        ValueOperations<String, Object> ops = redisConfig.redisTemplate().opsForValue();
        ops.set(key, data);
    }
    public EmailAuthDTO getData(String key) {
        ValueOperations<String, Object> ops = redisConfig.redisTemplate().opsForValue();
        return (EmailAuthDTO) ops.get(key);
    }
    /* 인증번호 확인 */
    public Boolean checkAuthNum(String email, String authNum) {
        EmailAuthDTO emailAuthDto = getData(email);
        if (emailAuthDto.getCode().equals(authNum)) {
            emailAuthDto.setFlag(true);
            updateData(email,emailAuthDto);
            return true;
        } else return false;
    }
    /* 이메일 전송 */
    public void mailSend(String setFrom, String toMail, String title, String content,Integer authNumber) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom); // service name
            helper.setTo(toMail); // customer email
            helper.setSubject(title); // email title
            helper.setText(content,true); // content, html: true
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // 에러 출력
        }
        // redis에 3분 동안 이메일과 인증 코드 저장
        ValueOperations<String, Object> valOperations = redisConfig.redisTemplate().opsForValue();
        setData(toMail, new EmailAuthDTO( Integer.toString(authNumber),false));
    }
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$"
    );

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    /* 이메일 작성 */
    public boolean joinEmail(String email) {
        Integer authNumber =  makeRandomNum();
        String customerMail = email;
        String title = "회원 가입을 위한 이메일입니다!";
        String content =
                "이메일을 인증하기 위한 절차입니다." +
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "회원 가입 폼에 해당 번호를 입력해주세요.";
        if(isValidEmail(email))
        {
            mailSend(serviceName, customerMail, title, content ,authNumber);
            return true;

        }else
        {
            return false;
        }
    }
}