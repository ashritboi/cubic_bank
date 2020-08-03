package com.rab3tech.customer.ui.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rab3tech.customer.service.AddressService;
import com.rab3tech.customer.service.CustomerRequestService;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.customer.service.LoginService;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.customer.service.TransactionService;
import com.rab3tech.customer.service.impl.CustomerEnquiryService;
import com.rab3tech.customer.service.impl.SecurityQuestionService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.vo.AddressVO;
import com.rab3tech.vo.ChangePasswordVO;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerSaveAnswerVO;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.EmailVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeVO;
import com.rab3tech.vo.RequestTypeVO;
import com.rab3tech.vo.SecurityQuestionsVO;
import com.rab3tech.vo.TransactionVO;

/**
 * 
 * @author nagendra
 * This class for customer GUI
 *
 */
@Controller
public class CustomerUIController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerUIController.class);

		   
	    
	@Autowired
	private CustomerEnquiryService customerEnquiryService;

	
	@Autowired
	private SecurityQuestionService securityQuestionService;
	
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
   private LoginService loginService;
	
	@Autowired
	   private PayeeInfoService payeeInfoService;	
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	   private AddressService addressService;
	
	@Autowired
	private CustomerRequestService customerRequestService;
	
	@PostMapping("/customer/changePassword")
	public String saveCustomerQuestions(@ModelAttribute ChangePasswordVO changePasswordVO, Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		String loginid=loginVO2.getUsername();
		changePasswordVO.setLoginid(loginid);
		String viewName ="customer/dashboard";
		boolean status=loginService.checkPasswordValid(loginid,changePasswordVO.getCurrentPassword());
		if(status) {
			if(changePasswordVO.getNewPassword().equals(changePasswordVO.getConfirmPassword())) {
				 viewName ="customer/dashboard";
				 loginService.changePassword(changePasswordVO);
			}else {
				model.addAttribute("error","Sorry , your new password and confirm passwords are not same!");
				return "customer/login";	//login.html	
			}
		}else {
			model.addAttribute("error","Sorry , your username and password are not valid!");
			return "customer/login";	//login.html	
		}
		model.addAttribute("username", loginid);
		return viewName;
	}
	
	@PostMapping("/customer/securityQuestion")
	public String saveCustomerQuestions(@ModelAttribute("customerSecurityQueAnsVO") CustomerSecurityQueAnsVO customerSecurityQueAnsVO, Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		String loginid=loginVO2.getUsername();
		customerSecurityQueAnsVO.setLoginid(loginid);
		securityQuestionService.save(customerSecurityQueAnsVO);
		//
		return "customer/changePassword";
	}
	
	@PostMapping("/customer/editInfos")
	public String showCustomerInfo(@RequestParam String username ,Model model) {
		CustomerVO customerVO = customerService.editAccount(username);
		CustomerSecurityQueAnsVO customerSecurityQueAnsVO=new CustomerSecurityQueAnsVO();
		List<SecurityQuestionsVO> questionsVOs=securityQuestionService.findAll();
		Collections.shuffle(questionsVOs);
		customerSecurityQueAnsVO.setQuestionsVOs(questionsVOs);
		
		List<CustomerSaveAnswerVO>customerSaveAnswerVOs = (List<CustomerSaveAnswerVO>) securityQuestionService.findbyusername(username);
		model.addAttribute("customerSaveAnswerVOs", customerSaveAnswerVOs);
		model.addAttribute("username", customerVO.getEmail());

		model.addAttribute("questionsVOs", questionsVOs);
		model.addAttribute("customerVO", customerVO);
		return "customer/customerEditInfo"; 
	}
	
	@PostMapping("/customer/editInfo")
	public String editCustomer(@ModelAttribute CustomerVO customerVO, Model model) {
		customerService.update(customerVO);		
		model.addAttribute("username", customerVO.getEmail());
		model.addAttribute("message", "Your Information is updated successfully.");
		return "customer/dashboard";
		
	}
	

	// http://localhost:444/customer/account/registration?cuid=1585a34b5277-dab2-475a-b7b4-042e032e8121603186515
	@GetMapping("/customer/account/registration")
	public String showCustomerRegistrationPage(@RequestParam String cuid, Model model) {

		logger.debug("cuid = " + cuid);
		Optional<CustomerSavingVO> optional = customerEnquiryService.findCustomerEnquiryByUuid(cuid);
		CustomerVO customerVO = new CustomerVO();

		if (!optional.isPresent()) {
			return "customer/error";
		} else {
			// model is used to carry data from controller to the view =- JSP/
			CustomerSavingVO customerSavingVO = optional.get();
			customerVO.setEmail(customerSavingVO.getEmail());
			customerVO.setName(customerSavingVO.getName());
			customerVO.setMobile(customerSavingVO.getMobile());
			customerVO.setAddress(customerSavingVO.getLocation());
			customerVO.setToken(cuid);
			logger.debug(customerSavingVO.toString());
			// model - is hash map which is used to carry data from controller to thyme
			// leaf!!!!!
			// model is similar to request scope in jsp and servlet
			model.addAttribute("customerVO", customerVO);
			return "customer/customerRegistration"; // thyme leaf
		}
	}

	
	
	@PostMapping("/customer/account/registration")
	public String createCustomer(@ModelAttribute CustomerVO customerVO, Model model) {
		// saving customer into database
		logger.debug(customerVO.toString());
		customerVO = customerService.createAccount(customerVO);
		// Write code to send email

		EmailVO mail = new EmailVO(customerVO.getEmail(), "javahunk2020@gmail.com",
				"Regarding Customer " + customerVO.getName() + "  userid and password", "", customerVO.getName());
		mail.setUsername(customerVO.getUserid());
		mail.setPassword(customerVO.getPassword());
		emailService.sendUsernamePasswordEmail(mail);
		System.out.println(customerVO);
		model.addAttribute("loginVO", new LoginVO());
		model.addAttribute("message", "Your account has been setup successfully , please check your email.");
		return "customer/login";
	}

	@GetMapping(value = { "/customer/account/enquiry", "/", "/mocha", "/welcome" })
	public String showCustomerEnquiryPage(Model model) {
		CustomerSavingVO customerSavingVO = new CustomerSavingVO();
		// model is map which is used to carry object from controller to view
		model.addAttribute("customerSavingVO", customerSavingVO);
		return "customer/customerEnquiry"; // customerEnquiry.html
	}

	@PostMapping("/customer/account/enquiry")
	public String submitEnquiryData(@ModelAttribute CustomerSavingVO customerSavingVO, Model model) {
		boolean status = customerEnquiryService.emailNotExist(customerSavingVO.getEmail());
		logger.info("Executing submitEnquiryData");
		if (status) {
			CustomerSavingVO response = customerEnquiryService.save(customerSavingVO);
			logger.debug("Hey Customer , your enquiry form has been submitted successfully!!! and appref "
					+ response.getAppref());
			model.addAttribute("message",
					"Hey Customer , your enquiry form has been submitted successfully!!! and appref "
							+ response.getAppref());
		} else {
			model.addAttribute("message", "Sorry , this email is already in use " + customerSavingVO.getEmail());
		}
		return "customer/success"; // customerEnquiry.html

	}
	
	@GetMapping("/customer/payee")
	public String addPayee(@ModelAttribute CustomerSavingVO customerSavingVO, Model model) {
		
		return "customer/addPayee"; // customerEnquiry.html

	}
	
	@GetMapping("/customer/payeeData")
	public String listPayee( Model model) {
		
		
		List<PayeeInfo> payee = payeeInfoService.findPayee();
		model.addAttribute("payee", payee);
		
		return "customer/listPayee"; // customerEnquiry.html

	}
	
	
	
	@PostMapping("/customer/savePayee")
	public String savePayee(@ModelAttribute PayeeVO payeeVO, Model model, HttpSession session) {
		LoginVO loginVO = (LoginVO)session.getAttribute("userSessionVO");
		if(loginVO != null){
			payeeVO.setCustomerId(loginVO.getUsername());
			String message = payeeInfoService.save(payeeVO);
			model.addAttribute("message", message);
		}
		return "redirect:/customer/payeeData"; // customerEnquiry.html

	}
//	@PostMapping("/customer/deletePayee")
//	public String deletePayee(@RequestParam int id) {
//		payeeInfoService.deletePayee(id);
//		
//		return "redirect:/customer/payeeData";
//	}
//	
	@PostMapping("/customer/editPayee")
	public String editCustomer(@RequestParam String id, @ModelAttribute PayeeVO payeeVO, Model model) {
		
		
		System.out.println("This is idddddddddddddddddd" + id);
		Optional<PayeeVO> optional=payeeInfoService.findPayeeData(id);
		System.out.printf("This is kjhdskhjsdkjsd" + optional.get().toString());
 		
		PayeeVO payeeVO1 = payeeInfoService.editPayee(id);
		model.addAttribute("username", payeeVO1.getCustomerId());

		model.addAttribute("payee", optional.get());

		return "customer/updatePayee";
		
	}
	

	
	@PostMapping("/customer/upPayee")
	public String updateCustomer( @RequestParam String payeeAccountNo, @RequestParam String payeeName, @RequestParam String payeeNickName, @RequestParam String remarks,  @ModelAttribute PayeeVO payeeVO, Model model) {
		
		
		PayeeVO payeeVO1 = new PayeeVO();
		payeeVO1.setPayeeAccountNo(payeeAccountNo);
		payeeVO1.setPayeeName(payeeName);
		payeeVO1.setPayeeNickName(payeeNickName);
		payeeVO1.setRemarks(remarks);
		
		
		System.out.println("I am in Up Payee controller. taking data to service.");
		payeeInfoService.update(payeeVO);		

		return "redirect:/customer/payeeData";
		
	}
	
	@GetMapping("/customer/account/loadTransferFund")
	public String loadTransferFund(Model model, HttpSession session) {
		LoginVO loginVO2 = (LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2 != null){
			List<PayeeInfo> payeeInfo = payeeInfoService.findPayee();
			System.out.println("llllllllllkkllklklklklklklklklklklklklklklklklk" + payeeInfo.toString());
			model.addAttribute("beneficiariesList", payeeInfo);
			
			return "customer/fundTransfer";
		}else{
			return "customer/login"; 
		}
	}

	@PostMapping("/customer/account/transferFund")
	public String transferFund(@ModelAttribute TransactionVO transactionVO, Model model, HttpSession session) {
		LoginVO loginVO2 = (LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2 != null){
			transactionVO.setCustomerId(loginVO2.getUsername());
			int amountt= (int) transactionVO.getAmount();
			if(amountt >0 ){
			String message = transactionService.transferFund(transactionVO);
			
			model.addAttribute("message", message);
			
			return "customer/fundTransfer";
			}
			else{
				model.addAttribute("error", "enter valid");
				return "customer/fundTransfer";
			}
		}else{
			model.addAttribute("error", "Please Login to proceed Further");
			return "customer/login"; 
		}
	}
	
	@GetMapping("/customer/account/getStatement")
	public String getStatement(Model model, HttpSession session) {
		LoginVO loginVO2 = (LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2 != null){
			String userId = loginVO2.getUsername();
			System.out.println("llllllllllkkllklklklklklklklklklklklklklklklklk" + userId);
			List<TransactionVO>list = transactionService.getAllTransactions(userId);
			System.out.println("wowowowowowowowowowowowowowowowowowowowowowowowowowowoowowo  " + list.size());
			model.addAttribute("list", list);
			
			return "customer/accountStatement";
		}else{
			return "customer/login"; 
		}
	}

	
	@PostMapping("/customer/account/saveAddress")
	public String saveAddress(@ModelAttribute AddressVO addressVO, Model model, HttpSession session) {
		LoginVO loginVO2 = (LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2 != null){
			addressVO.setLogonId(loginVO2.getUsername()	);
			String message = addressService.saveAddress(addressVO);			
			model.addAttribute("message", message);
			return "customer/requestEntry";
		}else{
			model.addAttribute("error", "Please Login to proceed Further");
			return "customer/login"; 
		}
	}
	
	@GetMapping("/customer/account/addressEntry")
	public String addressEntry( Model model, HttpSession session) {
		LoginVO loginVO2 = (LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2 != null){
			return "customer/address";
		}else{
			model.addAttribute("error", "Please Login to proceed Further");
			return "customer/login"; 
		}
	}
	
	@GetMapping("/customer/account/requestEntry")
	public String requestEntry( Model model, HttpSession session) {
		LoginVO loginVO2 = (LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2 != null){
			AddressVO address = addressService.getAddressByUserId(loginVO2.getUsername());
			if(address.getAddressId()>0){
				model.addAttribute("address", address);
				List<RequestTypeVO> requestsType = customerRequestService.findAllRequests();
				model.addAttribute("requestType", requestsType);
				return "customer/requestEntry";
			}else{
				return "customer/address";
			}
		}else{
			model.addAttribute("error", "Please Login to proceed Further");
			return "customer/login"; 
		}
	}
	
	@GetMapping("/customer/account/raiseRequest")
	public String raiseRequest(@RequestParam int requestType, Model model, HttpSession session) {
		LoginVO loginVO2 = (LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2 != null){
			RequestTypeVO requestsType = customerRequestService.findById(requestType);
			if(requestsType.getStatus() == 1){
				String page = "customer"+"/" + requestsType.getName().toLowerCase();
				AddressVO address = addressService.getAddressByUserId(loginVO2.getUsername());
				model.addAttribute("address", address);
				return page;
			}else{
				List<RequestTypeVO> requestsType1 = customerRequestService.findAllRequests();
				model.addAttribute("requestType", requestsType1);
				return "customer/requestEntry";
			}
		}else{
			model.addAttribute("error", "Please Login to proceed Further");
			return "customer/login"; 
		}
	}
	
	
}
