package com.mthree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;

import com.mthree.controllers.TraderController;
import com.mthree.models.Role;
import com.mthree.models.Trader;
import com.mthree.repositories.TraderRepository;
import com.mthree.services.SecurityService;
import com.mthree.services.TraderService;
import com.mthree.utils.TraderValidator;

@SpringBootTest
public class TraderControllerTest {
	
	@Mock
    private TraderRepository tr;
	
	@Mock
    private TraderService ts;
	
	@Mock
	private SecurityService ss; 
	
	@Mock
    private TraderValidator tv;
	
	@InjectMocks
	private TraderController tc = new TraderController();
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private Trader t;
	private BindingResult br;
	
	@BeforeEach
    void setUp() {
		
        t = new Trader();
        t.setUsername("ThisIsATest");
        t.setPassword("ThisIsATest");
        
        br = mock(BindingResult.class);
        
        Trader newTrader = new Trader();
        newTrader.setUsername(t.getUsername());
        newTrader.setPassword(bCryptPasswordEncoder.encode(t.getPassword()));
        newTrader.setRole(Role.ROLE_ADMIN);
        
        when(tr.save(t)).thenReturn(newTrader);
        when(ts.addTrader(t)).thenReturn(newTrader);
        when(ts.findByUsername(t.getUsername())).thenReturn(newTrader);
        when(ss.findLoggedInUsername()).thenReturn(t.getUsername());
    }
    
    @Test
    void testRegisterUser() {
    	
    	// register valid user
        when(br.hasErrors()).thenReturn(false);
        
        tc.registerUser(t, br);
        
        Trader dbTrader = ts.findByUsername(t.getUsername());
        
        assertEquals(t.getUsername(), dbTrader.getUsername());
    	
    	// test password is encrypted
    	String unencryptedPassword = t.getPassword();
    	String encryptedPassword = dbTrader.getPassword();
    	assertNotEquals(unencryptedPassword, encryptedPassword);
    	
    	// test admin role is added
    	assertEquals(Role.ROLE_ADMIN, dbTrader.getRole());
    	
    	// register invalid user 
        
        Trader t2 = new Trader();
        t2.setUsername("Test");
        t2.setPassword("");
        when(br.hasErrors()).thenReturn(true);
        
        tc.registerUser(t2, br);
        
        assertNull(ts.findByUsername(t2.getUsername()));
    }
}