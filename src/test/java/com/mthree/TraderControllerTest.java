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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;

import com.mthree.controllers.TraderController;
import com.mthree.dtos.TraderDTO;
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

    @Mock
    private ModelMapper modelMapperMock;
	
	@InjectMocks
	private TraderController tc = new TraderController();
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ModelMapper modelMapper;
    
    private Trader t;
	private TraderDTO dto;
	private BindingResult br;
	
	@BeforeEach
    void setUp() {
        
        t = new Trader();
        t.setUsername("ThisIsATest");
        t.setPassword("ThisIsATest");
        t.setPasswordConfirm("ThisIsATest");

        dto = new TraderDTO();
        dto.setUsername(t.getUsername());
        dto.setPassword(t.getPassword());
        dto.setPasswordConfirm(t.getPasswordConfirm());
        
        br = mock(BindingResult.class);
        
        Trader newTrader = new Trader();
        newTrader.setUsername(t.getUsername());
        newTrader.setPassword(bCryptPasswordEncoder.encode(t.getPassword()));
        newTrader.setRole(Role.ROLE_ADMIN);
        
        when(tr.save(t)).thenReturn(newTrader);
        when(ts.addTrader(t)).thenReturn(newTrader);
        when(ts.findByUsername(t.getUsername())).thenReturn(newTrader);
        when(ss.findLoggedInUsername()).thenReturn(t.getUsername());
        when(modelMapperMock.map(dto, Trader.class)).thenReturn(t);
    }
    
    @Test
    void testRegisterUser() {
    	
    	// register valid user
        when(br.hasErrors()).thenReturn(false);
        
        tc.registerUser(dto, br);
        
        Trader dbTrader = ts.findByUsername(t.getUsername());
        
        assertEquals(t.getUsername(), dbTrader.getUsername());
    	
    	// test password is encrypted
    	String unencryptedPassword = t.getPassword();
    	String encryptedPassword = dbTrader.getPassword();
    	assertNotEquals(unencryptedPassword, encryptedPassword);
    	
    	// test admin role is added
    	assertEquals(Role.ROLE_ADMIN, dbTrader.getRole());
    	
    	// register invalid user 
        TraderDTO dto2 = new TraderDTO();
        dto2.setId(2);
        dto2.setUsername("ThisIsATest2");
        dto2.setPassword("ThisIsATest2");
        dto2.setPasswordConfirm("ThisIsATest2");
        when(br.hasErrors()).thenReturn(true);
        
        tc.registerUser(dto2, br);
        
        assertNull(ts.findByUsername(dto2.getUsername()));
    }

    @Test
    public void testConvertToEntity() {
        
        TraderDTO traderDTO = new TraderDTO();

        Trader trader = modelMapper.map(traderDTO, Trader.class);
        
        assertEquals(traderDTO.getId(), trader.getId());
        assertEquals(traderDTO.getPassword(), trader.getPassword());
        assertEquals(traderDTO.getPasswordConfirm(), trader.getPasswordConfirm());
    }

    @Test
    public void testConvertToDto() {

        Trader trader = new Trader();
        trader.setId(1);
        trader.setUsername("test");
        trader.setPassword("test");
        trader.setPasswordConfirm("test");

        TraderDTO traderDTO = modelMapper.map(trader, TraderDTO.class);

        assertEquals(trader.getId(), traderDTO.getId());
        assertEquals(trader.getPassword(), traderDTO.getPassword());
        assertEquals(trader.getPasswordConfirm(), traderDTO.getPasswordConfirm());
    }
}