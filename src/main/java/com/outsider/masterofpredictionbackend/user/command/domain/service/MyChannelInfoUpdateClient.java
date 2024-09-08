package com.outsider.masterofpredictionbackend.user.command.domain.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelInfoUpdateRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MyChannelInfoUpdateClient {
        void publish(MyChannelInfoUpdateRequestDTO myChannelInfoUpdateRequestDTO) ;
}
