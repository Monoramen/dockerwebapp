package com.dockerwebapp.servlets.dto;

import com.dockerwebapp.servlet.dto.ChatDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ChatDtoTest {

    @Test
    void testChatDtoToString() {
        ChatDto chatDto = new ChatDto();
        chatDto.setId(1L);
        chatDto.setName("Test chat");
        assertEquals("ChatDto{id=1, name='Test chat', participantIds=[]}", chatDto.toString());
    }

    @Test
    void testChatDtoEquals() {
        ChatDto chatDto1 = new ChatDto();
        chatDto1.setId(1L);
        chatDto1.setName("Test chat");
        ChatDto chatDto2 = new ChatDto();
        chatDto2.setId(1L);
        chatDto2.setName("Test chat");
        assertEquals(chatDto1, chatDto2);
    }

    @Test
    void testChatDtoNotEqualsHashCode() {
        ChatDto chatDto = new ChatDto();
        chatDto.setId(1L);
        chatDto.setName("Test chat");
        assertNotEquals(1, chatDto.hashCode());
    }

    @Test
    void testChatDtoEqualsHashCode() {
        ChatDto chatDto = new ChatDto();
        chatDto.setId(1L);
        chatDto.setName("Test chat");
        ChatDto chatDto2 = new ChatDto();
        chatDto2.setId(1L);
        chatDto2.setName("Test chat");
        assertEquals(chatDto.hashCode(), chatDto2.hashCode());

    }

}
