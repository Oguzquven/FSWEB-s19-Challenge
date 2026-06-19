package com.workintech.twitter;

import com.workintech.twitter.dto.Dtos;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.ResourceNotFoundException;
import com.workintech.twitter.exception.UnauthorizedException;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import com.workintech.twitter.service.TweetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TweetService tweetService;

    private User testUser;
    private Tweet testTweet;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@test.com");
        testUser.setPassword("encoded_password");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setTweets(new ArrayList<>());
        testUser.setComments(new ArrayList<>());
        testUser.setLikes(new ArrayList<>());
        testUser.setRetweets(new ArrayList<>());

        testTweet = new Tweet();
        testTweet.setId(1L);
        testTweet.setContent("Test tweet icerigi");
        testTweet.setUser(testUser);
        testTweet.setCreatedAt(LocalDateTime.now());
        testTweet.setUpdatedAt(LocalDateTime.now());
        testTweet.setComments(new ArrayList<>());
        testTweet.setLikes(new ArrayList<>());
        testTweet.setRetweets(new ArrayList<>());
    }

    @Test
    void createTweet_Success() {
        Dtos.TweetRequest request = new Dtos.TweetRequest();
        request.setContent("Test tweet");
        request.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(testTweet);

        Dtos.TweetResponse response = tweetService.createTweet(request);

        assertNotNull(response);
        assertEquals("Test tweet icerigi", response.getContent());
        assertEquals("testuser", response.getUsername());
        verify(tweetRepository, times(1)).save(any(Tweet.class));
    }

    @Test
    void createTweet_UserNotFound_ThrowsException() {
        Dtos.TweetRequest request = new Dtos.TweetRequest();
        request.setContent("Test tweet");
        request.setUserId(999L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                tweetService.createTweet(request)
        );

        verify(tweetRepository, never()).save(any(Tweet.class));
    }

    @Test
    void getTweetsByUserId_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(tweetRepository.findByUserId(1L)).thenReturn(List.of(testTweet));

        List<Dtos.TweetResponse> tweets = tweetService.getTweetsByUserId(1L);

        assertNotNull(tweets);
        assertEquals(1, tweets.size());
        assertEquals("Test tweet icerigi", tweets.get(0).getContent());
    }

    @Test
    void getTweetsByUserId_UserNotFound_ThrowsException() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                tweetService.getTweetsByUserId(999L)
        );
    }

    @Test
    void deleteTweet_UnauthorizedUser_ThrowsException() {
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));

        assertThrows(UnauthorizedException.class, () ->
                tweetService.deleteTweet(1L, 2L)
        );

        verify(tweetRepository, never()).delete(any(Tweet.class));
    }

    @Test
    void deleteTweet_AuthorizedUser_Success() {
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));

        tweetService.deleteTweet(1L, 1L);

        verify(tweetRepository, times(1)).delete(testTweet);
    }

    @Test
    void updateTweet_Success() {
        Dtos.TweetRequest request = new Dtos.TweetRequest();
        request.setContent("Guncellenmis icerik");
        request.setUserId(1L);

        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(testTweet);

        Dtos.TweetResponse response = tweetService.updateTweet(1L, request);

        assertNotNull(response);
        verify(tweetRepository, times(1)).save(any(Tweet.class));
    }
}