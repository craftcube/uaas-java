package com.cffex.utils;

import com.cffex.entity.Role;
import com.cffex.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

public class JWTUtilsTest {
    private static final String TEST_USERNAME = "testUser";
    private static final Logger logger = LoggerFactory.getLogger(JWTUtilsTest.class);

    @Mock
    private Clock clockMock;

    @InjectMocks
    private JWTUtils jwtTokenUtil;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(jwtTokenUtil, "expirationTime", "604800"); // one hour
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "mySecret11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
    }
    @Test
    public void getCreatedDateFromToken() throws Exception {
        List<Role> roles=new ArrayList<Role>();
        roles.add(Role.ROLE_ADMIN);
//        User user= User.builder().username("uchenm").roles(roles).build();
        User user=User.builder().build();
        final String token = jwtTokenUtil.generateToken(user);
        logger.info("token,{1}",token);

//        assertThat(jwtTokenUtil.getIssuedAtDateFromToken(token)).isInSameMinuteWindowAs(now);
    }

}
