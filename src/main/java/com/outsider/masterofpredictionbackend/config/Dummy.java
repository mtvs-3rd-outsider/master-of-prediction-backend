package com.outsider.masterofpredictionbackend.config;

import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.DeleteUserService;
import com.outsider.masterofpredictionbackend.user.command.application.service.UserRegistService;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.*;

@Configuration
public class Dummy {
    private final UserRegistService registUserService;
    private final DeleteUserService deleteUserService;
    private Long id ;
    private Long id2 ;
    private Long id3 ;

    private final String email= DEFAULT_USER_EMAIL;
    private final String userName= DEFAULT_USER_NAME;
    private final String password = DEFAULT_USER_PASSWORD;
    UserCommandRepository userCommandRepository;


    public Dummy(UserCommandRepository userCommandRepository,UserRegistService registUserService, DeleteUserService deleteUserService) {
        this.registUserService = registUserService;
        this.userCommandRepository = userCommandRepository;
        this.deleteUserService = deleteUserService;

    }


    @Bean
    @Transactional
    public ApplicationRunner init() {
        return args -> {
            if (userCommandRepository.findByEmail(email).isEmpty()) {

                System.out.println("생성 1");

                UserRegistDTO dto = new UserRegistDTO(
                        email,
                        password,
                        userName,
                        Authority.ROLE_USER
                );

                // when
                id= registUserService.registUser(dto);


            }
            if (userCommandRepository.findByEmail(email+"1").isEmpty()) {

                System.out.println("생성 2");
                UserRegistDTO dto = new UserRegistDTO(
                        email+"1",
                        password,
                        userName,
                        Authority.ROLE_USER
                );

                // when
                id2= registUserService.registUser(dto);


            }
            if (userCommandRepository.findByEmail(email+"2").isEmpty()) {

                System.out.println("생성 2");
                UserRegistDTO dto = new UserRegistDTO(
                        email+"2",
                        password,
                        "User",
                        Authority.ROLE_USER
                );

                // when
                id3= registUserService.registUser(dto);


            }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                deleteUserService.deleteUser(id);
                deleteUserService.deleteUser(id2);
                deleteUserService.deleteUser(id3);
            }));
        };
    }
}
