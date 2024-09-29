package com.dockerwebapp.servlets.dto;

import com.dockerwebapp.servlet.dto.MessageDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageDtoTest {

    @Test
    public void testMessageDtoToString() {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(1L);
        messageDto.setText("Test message");
        messageDto.setDateTime("2022-01-01 00:00:00");
        messageDto.setSenderId(1L);
        messageDto.setChatId(1L);
        assertEquals("MessageDto{id=1, text='Test message', dateTime='2022-01-01 00:00:00', senderId='1', chatId='1'}", messageDto.toString());
    }

    @Test
    public void testMessageDtoEquals() {
        MessageDto messageDto1 = new MessageDto();
        messageDto1.setId(1L);
        messageDto1.setText("Test message");
        messageDto1.setDateTime("2022-01-01 00:00:00");
        messageDto1.setSenderId(1L);
        messageDto1.setChatId(1L);
        MessageDto messageDto2 = new MessageDto();
        messageDto2.setId(1L);
        messageDto2.setText("Test message");
        messageDto2.setDateTime("2022-01-01 00:00:00");
        messageDto2.setSenderId(1L);
        messageDto2.setChatId(1L);
        assertEquals(messageDto1, messageDto2);
    }
}
