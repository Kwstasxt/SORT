//package com.mthree;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.validation.BindingResult;
//
//import com.mthree.controllers.TraderController;
//import com.mthree.models.Role;
//import com.mthree.models.Trader;
//import com.mthree.repositories.TraderRepository;
//import com.mthree.services.TraderService;
//
//@SpringBootTest
//public class TraderServiceTest {
//	
//	@Mock
//	private TraderRepository tr;
//	
//	@Mock
//    private TraderService ts;
//	
//	@InjectMocks
//	private TraderController tc;
//	
//	@Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//	
//	private Trader t;
//	
//	@BeforeEach
//    void setUp() {
//		
//		initMocks(this);
//		
//        t = new Trader();
//        t.setUsername("ThisIsATest");
//        t.setPassword("ThisIsATest");
//        
//        Trader newTrader = new Trader();
//        newTrader.setUsername(t.getUsername());
//        newTrader.setPassword(bCryptPasswordEncoder.encode(t.getPassword()));
//        newTrader.setRole(Role.ROLE_ADMIN);
//        
//        when(ts.addTrader(t)).thenReturn(newTrader);
//        when(ts.findByUsername(t.getUsername())).thenReturn(newTrader);
//    }
//    
//    @Test
//    void testAddTrader() {
//    	
//    	// add a valid user
//    	BindingResult br = mock(BindingResult.class);
//        when(br.hasErrors()).thenReturn(false);
//    	tc.registerUser(t, br);
//    	
//    	// test trader is added and can be found
//    	Trader dbTrader = ts.findByUsername(t.getUsername());
//    	assertEquals(dbTrader, ts.findByUsername(t.getUsername()));
//    	
//    	// test password is encrypted
//    	String unencryptedPassword = t.getPassword();
//    	String encryptedPassword = dbTrader.getPassword();
//    	assertNotEquals(unencryptedPassword, encryptedPassword);
//    	
//    	// test admin role is added
//    	assertEquals(dbTrader.getRole(), Role.ROLE_ADMIN);
//    	
//    	// do not add an invalid user
//        when(br.hasErrors()).thenReturn(true);
//        Trader t2 = new Trader();
//        tc.registerUser(t2, br);
//        
//        // test trader is not added
//        assertEquals(null, ts.findByUsername(t.getUsername()));
//    }
//    
//    @Test
//    void testFindByUsername() {
//    	
//    	// add trader to db
//    	Trader expected = ts.addTrader(t);
//    	Trader actual = ts.findByUsername(t.getUsername());
//    	
//    	assertEquals(expected, actual);
//    }
//	
//}