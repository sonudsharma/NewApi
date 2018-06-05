package com.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.models.Address;
import com.models.Cities;
import com.models.ErrorObject;
import com.models.FileInfo;
import com.models.Hospital;
import com.models.JwtUser;
import com.models.Persons;
import com.models.TokenHistory;
import com.models.UserLogin;
import com.models.UserLoginDetails;
import com.models.UserRegist;
import com.models.UserRegistration;
import com.models.UserRegistrationDetails;
import com.models.Users;
import com.security.JwtGenerator;
import com.security.JwtValidator;
import com.security.PasswordService;
import com.service.OtpService;
import com.service.QuickbloxService;
import com.service.UserService;
import com.util.MailHelper;
import com.util.UtilityHelper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

@CrossOrigin(origins = "*")
@RestController
public class UserController {

	private static final Logger LOGGER = Logger.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	@Autowired
	private OtpService otpService;

	@Autowired
	private QuickbloxService quickbloxService;

	PasswordService passwordService = PasswordService.getInstance();

	/*
	 * @Autowired ServletContext context;
	 */

	@RequestMapping(value = "/user/auth/token")
	public UserLogin authenticateToken(@RequestBody Users user) {
		LOGGER.info("Authantication Api Called");
		UserLogin userLogin = new UserLogin();
		Users userFetch = null;
		TokenHistory toch = new TokenHistory();
		try {
			if (StringUtils.isBlank(user.getUsername())
					|| StringUtils.isBlank(user.getPassword()) && StringUtils.isBlank(user.getPin())) {
				LOGGER.info("Username and Password or Pin Required Success=2");
				userLogin.setSuccess(2);
				userLogin.setMsg("Username and Password or Pin Required");
				userLogin.setData(new UserLoginDetails());

			} else {

				userFetch = userService.fetchUser(user.getUsername(), passwordService.encrypt(user.getPassword(), 1),
						passwordService.encrypt(user.getPin()), user.getType());

				if (userFetch.getType() == user.getType()) {
					userLogin.setMsg("You are not Authorized to login");
				} else if (userFetch.getIsOnline().equalsIgnoreCase("Yes")) {
					userLogin.setMsg("You are Logged In to Another Device");
				} else if (userFetch != null) {
					userFetch.setLastLoginDate(new Date());
					userFetch.setIsOnline("Yes");
					// user.setLastLoginDate(new Date());
					userService.updateProfile(userFetch);
					userLogin.setMsg("Login Successfully");
					userLogin.setSuccess(1);

					JwtUser jwtUser = new JwtUser();

					jwtUser.setUserName(userFetch.getUsername());
					jwtUser.setId(userFetch.getId());
					UserLoginDetails userDetails = new UserLoginDetails();
					userDetails.setId(userFetch.getId());
					userDetails.setEmail(userFetch.getUserEmail());
					userDetails.setAge(userFetch.getPerson().getAge());
					userDetails.setFirstname(userFetch.getPerson().getFirstName());

					if (userFetch.getType().equalsIgnoreCase("Doctor")) {
						userDetails.setSpecialist(userFetch.getPerson().getSpeciality());
					}

					// userDetails.setFull_name(userFetch.getPerson().getFullName());
					userDetails.setLastname(userFetch.getPerson().getLastName());
					userDetails.setDob(userFetch.getPerson().getDob());
					userDetails.setGender(userFetch.getPerson().getGender());
					userDetails.setAdharno(userFetch.getPerson().getAdharnumber());
					userDetails.setHeight(userFetch.getPerson().getHeight());
					userDetails.setWeight(userFetch.getPerson().getWeight());
					userDetails.setHospitalID(userFetch.getPerson().getHospital().getId());

					if (userFetch.getPerson().getAddress() != null) {
						userDetails.setAddress(userFetch.getPerson().getAddress().getFulladdress());
						userDetails.setPincode(userFetch.getPerson().getAddress().getPostalcode());
						if (userFetch.getPerson().getAddress().getCity() != null) {
							userDetails.setCity(userFetch.getPerson().getAddress().getCity().getName());
							userDetails.setState(userFetch.getPerson().getAddress().getCity().getState().getName());
						}
					} else {
						userDetails.setAddress("");
						userDetails.setPincode("");
						userDetails.setCity("");
						userDetails.setState("");
					}
					userDetails.setUserType(userFetch.getType());
					String token = JwtGenerator.generate(jwtUser);
					Claims body = Jwts.parser().setSigningKey("raystechserv").parseClaimsJws(token).getBody();
					Date expiration = body.getExpiration();
					userDetails.setToken(token);
					userService.saveToken(userFetch.getId(), token, expiration);
					userLogin.setData(userDetails);

				} /*
					 * else { LOGGER.info("Invalid Username and Password Success=3");
					 * userLogin.setSuccess(3); userLogin.setMsg("Invalid Username and Password");
					 * userLogin.setData(new UserLoginDetails()); }
					 */

			}
		} catch (Exception e) {
			LOGGER.error(e);
			LOGGER.info("Invalid Username and Password Success=3");
			userLogin.setSuccess(3);
			userLogin.setMsg("Invalid Username and Password");
			userLogin.setData(new UserLoginDetails());
		}

		return userLogin;
	}

	@CrossOrigin
	@RequestMapping(value = "/user/updateProfile")
	public Object updateProfile(@RequestBody Map<String, String> userMap) {
		ModelMap model = new ModelMap();
		ErrorObject error = new ErrorObject("Not Found", 404);
		Boolean isValidate = true;
		try {
			Users user = userService.get(Integer.parseInt(userMap.get("id")));
			Persons person = user.getPerson();
			Address address = person.getAddress();

			if (address == null) {
				address = new Address();
			}

			person.setAddress(address);

			String dob = userMap.get("dob");
			String age = userMap.get("age");
			String gender = userMap.get("gender");

			String fulladdress = userMap.get("fulladdress");
			String postalcode = userMap.get("postalcode");
			String email = userMap.get("email");

			Integer cityId = (userMap.get("city") != null && !userMap.get("city").isEmpty()
					? Integer.parseInt(userMap.get("city"))
					: 0);

			if (user.getType().equalsIgnoreCase("Patient")) {

				String adharnumber = userMap.get("adharnumber");
				String height = userMap.get("height");
				String weight = userMap.get("weight");

				if (StringUtils.isBlank(adharnumber) || !StringUtils.isNumeric(adharnumber)
						|| (StringUtils.length(adharnumber) < 12)) {
					error.setMsg("AdharNumber can not be empty and it must be at least 12 digit numeric value");
					error.setErrorCode(406);
					isValidate = false;
				}

				if (!NumberUtils.isNumber(weight)) {
					error.setMsg("Weight Enter in Numeric value");
					error.setErrorCode(406);
					isValidate = false;
				}

				if (!NumberUtils.isNumber(height)) {
					error.setMsg("Height Enter in Numeric value");
					error.setErrorCode(406);
					isValidate = false;
				}
				person.setAdharnumber(adharnumber);
				person.setWeight(weight);
				person.setHeight(height);
			}

			if ((user.getType().equalsIgnoreCase("Doctor"))) {
				String firstname = userMap.get("firstname");
				String lastname = userMap.get("lastname");
				String liciencenumber = userMap.get("licencenumber");
				String tagline = userMap.get("tagline");
				String phone = userMap.get("mobilenumber");

				person.setPhone(phone);
				person.setFirstName(firstname);
				person.setLastName(lastname);
				person.setLiciencenumber(liciencenumber);
				person.setLiciencenumber(liciencenumber);
				person.setTagline(tagline);
			}

			if (isValidate == false) {
				return error;
			}
			user.setType(user.getType());
			// audit fields
			user.setRecordStatusFlg(user.getRecordStatusFlg());
			user.setRecordStatusDate(user.getRecordStatusDate());
			user.setLastChangeDate(new Date());
			user.setLastChangeUserId(user.getId());

			// common fields
			user.setUserEmail(email);
			person.setDob(UtilityHelper.convertTimestampToDate(dob));
			person.setAge(Integer.parseInt(age));
			person.setGender(gender);

			Cities city = userService.getCity(cityId);
			address.setCity(city);
			address.setFulladdress(fulladdress);
			address.setPostalcode(postalcode);

			user.setPerson(person);

			return userService.updateProfile(user);

		} catch (Exception e) {
			LOGGER.error(e);
			model.put("msg", "updation Failed");
		}

		return model;

	}

	@RequestMapping(value = "/user/resetPassword")
	public Map<String, String> resetPassword(@RequestBody Map<String, Object> userMap) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			userMap.put("password", passwordService.encrypt(userMap.get("password").toString(), 1));
			return userService.resetPassword(userMap);
		} catch (Exception e) {
			LOGGER.error(e);
			result.put("msg", "updation Failed");
		}
		return result;
	}

	@RequestMapping(value = "/user/token/validate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ModelMap> validateToken(@RequestBody Users user) {
		ModelMap model = new ModelMap();
		JwtValidator validator = new JwtValidator();
		JwtUser jwtUser = validator.validate(user.getToken());
		if (jwtUser == null) {
			model.put("Is UnAuthenticated", "Token Expired");
		} else {
			model.put("Is Authenticated", jwtUser);
		}
		return ResponseEntity.ok().body(model);
	}

	@RequestMapping("/hospital/{id}")
	public Object getHospital(HttpServletRequest request, @PathVariable("id") Long id) {
		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		map.put("success", "1");
		if (!"true".equals(isVerify)) {
			return map;
		}
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Hospital hospital = userService.getHospital(id);
		result.put("success", "1");
		result.put("msg", "Hospital Details");
		result.put("data", hospital);
		return result;
	}

	@RequestMapping("/person/{id}")
	public Object getPerson(HttpServletRequest request, @PathVariable("id") Integer id) {
		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			return map;
		}
		Users person = userService.getPerson(id);
		return person;
	}

	@RequestMapping("/user/doctorList")
	public Object getDoctorList(HttpServletRequest request) {

		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			map = new LinkedHashMap<String, String>();
			map.put("success", "2");
			map.put("msg", isVerify);
			return map;
		}

		List<Map> userResult = userService.getDoctorList("Doctor");
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("data", userResult);
		result.put("msg", "Doctor List");
		result.put("success", "1");
		return result;
	}

	@RequestMapping("/user/patientList")
	public Object getPatientList(HttpServletRequest request) {

		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			map = new HashMap<String, String>();
			map.put("success", "2");
			map.put("msg", isVerify);
			return map;
		}

		List<Map> userResult = userService.getDoctorList("Patient");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", "1");
		result.put("msg", "Patient List");
		result.put("data", userResult);
		return result;
	}

	/*---Delete a user by id---*/
	@RequestMapping("/user/delete/{id}")
	public Map<String, String> delete(HttpServletRequest request, @PathVariable("id") Integer UserId) {

		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			return map;
		}

		userService.delete(UserId);
		map = new LinkedHashMap<String, String>();
		map.put("success", "1");
		map.put("msg", "User has been deleted successfully.");
		return map;
	}

	private Map<String, String> verifyToken(HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		String token = request.getHeader("token");
		if (StringUtils.isEmpty(token)) {
			result.put("IsAuthenticated", "Token Not Found");
			return result;
		}
		JwtValidator validator = new JwtValidator();
		JwtUser jwtUser = validator.validate(token);
		if (jwtUser == null) {
			result.put("IsAuthenticated", "Token Expired");
		} else {
			result.put("IsAuthenticated", "true");
		}
		return result;
	}

	@RequestMapping(value = "/user/register")
	public Object registerUser(@RequestBody UserRegistration userRegistration) {
		UserRegist userRegist = new UserRegist();
		ErrorObject error = new ErrorObject("Not Found", 404);
		Boolean isValidate = true;
		if (StringUtils.isBlank(userRegistration.getIspatient())) {
			error.setMsg("isPatient should be either 0 or 1");
			error.setErrorCode(406);
			isValidate = false;
		}
		if (StringUtils.isBlank(userRegistration.getIsdoctor())) {
			error.setMsg("isDoctor should be either 0 or 1");
			error.setErrorCode(406);
			isValidate = false;
		}
		if (StringUtils.isBlank(userRegistration.getPassword())) {
			error.setMsg("password can not be empty");
			error.setErrorCode(406);
			isValidate = false;
		}
		if (StringUtils.isBlank(userRegistration.getFirstname())) {
			error.setMsg("Firstname can not be empty");
			error.setErrorCode(406);
			isValidate = false;
		}
		if (StringUtils.isBlank(userRegistration.getLastname())) {
			error.setMsg("Lastname can not be empty");
			error.setErrorCode(406);
			isValidate = false;
		}
		if (StringUtils.isBlank(userRegistration.getEmail())) {
			error.setMsg("Email can not be empty");
			error.setErrorCode(406);
			isValidate = false;
		}
		if (StringUtils.isBlank(userRegistration.getPhone()) || !StringUtils.isNumeric(userRegistration.getPhone())
				|| (StringUtils.length(userRegistration.getPhone()) < 10)) {
			error.setMsg("Phone can not be empty and it must be at least 10 digit numeric value");
			error.setErrorCode(406);
			isValidate = false;
		}
		if (StringUtils.isBlank(userRegistration.getIsdoctor())) {
			error.setMsg("IsDoctor can not be empty");
			error.setErrorCode(406);
			isValidate = false;
		}
		if (StringUtils.isBlank(userRegistration.getIspatient())) {
			error.setMsg("IsPatient can not be empty");
			error.setErrorCode(406);
			isValidate = false;
		}
		if ("0".equals(userRegistration.getIspatient()) && "0".equals(userRegistration.getIsdoctor())) {
			error.setMsg("IsPatient and isDoctor can not be empty");
			error.setErrorCode(406);
			isValidate = false;
		}
		if (userService.fetchUserByEmail(userRegistration.getEmail()) != null) {
			error.setMsg("User already exists.");
			isValidate = false;
		}

		if (isValidate == false) {
			return error;
		} else {
			try {
				Users users = new Users();
				Persons persons = new Persons();
				users.setCreateUserId(users.getId());
				users.setCreatedDate(new Date());
				persons.setFirstName(userRegistration.getFirstname());
				persons.setLastName(userRegistration.getLastname());
				persons.setPhone(userRegistration.getPhone());
				users.setPerson(persons);

				users.setPassword(passwordService.encrypt(userRegistration.getPassword(), 1));
				users.setUsername(userRegistration.getEmail());
				users.setUserEmail(userRegistration.getEmail());
				users.setRecordStatusDate(userRegistration.getRecordStatusDate());

				if (StringUtils.isNotBlank(userRegistration.getRecordStatusFlg())
						&& "Active".equals(userRegistration.getRecordStatusFlg())) {
					users.setRecordStatusFlg("Active");
				} else if (StringUtils.isNotBlank(userRegistration.getRecordStatusFlg())
						&& "Inactive".equals(userRegistration.getRecordStatusFlg())) {
					users.setRecordStatusFlg("Inactive");
				}

				if (StringUtils.isNotBlank(userRegistration.getIsdoctor())
						&& "1".equals(userRegistration.getIsdoctor())) {
					users.setType("Doctor");
				} else if (StringUtils.isNotBlank(userRegistration.getIspatient())
						&& "1".equals(userRegistration.getIspatient())) {
					users.setType("Patient");
				}
				/*
				 * if(users.getPerson().getHospital() == null){ hospitaObj =
				 * service.getHospital(1); //users.getperson.setHospo()hospitalobj) }
				 */
				persons.setHospital(new Hospital(1));
				Integer userId = userService.userRegistration(users);
				users.setCreateUserId(users.getId());
				userService.updateProfile(users);
				Integer otp = otpService.generateOTP(userRegistration.getEmail());

				String token = quickbloxService.loginAndGetToken();
				String id = quickbloxService.registerNewUser(token, userRegistration.getEmail());

				if (id != null) {

					try {
						boolean mail = MailHelper.sendOtpMail(userRegistration.getEmail(), otp);

						if (mail) {
							userRegist.setMsg("Registration Successfully");
							userRegist.setSuccess(1);

							UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails();
							userRegistrationDetails.setId(userId);
							userRegistrationDetails.setEmail(users.getUserEmail());
							userRegistrationDetails.setFull_name(
									userRegistration.getFirstname() + " " + userRegistration.getLastname());

							userRegist.setData(userRegistrationDetails);
						} else {
							userRegist.setMsg("Error in sending email");
							userRegist.setSuccess(2);
							userRegist.setData(new UserRegistrationDetails());
						}
					} catch (Exception e) {
						userRegist.setMsg("Error in sending email");
						userRegist.setSuccess(2);
						userRegist.setData(new UserRegistrationDetails());
					}
				} else {
					userRegist.setMsg("Quickblox registration cound not be done because of duplicate email");
					userRegist.setSuccess(2);
					userRegist.setData(new UserRegistrationDetails());
				}

			} catch (Exception e) {
				LOGGER.error("Exception in User Registration " + e.getMessage());

				userRegist.setSuccess(2);
				userRegist.setMsg("Invalid Data");
				userRegist.setData(new UserRegistrationDetails());
			}
		}
		return userRegist;
	}

	@RequestMapping(value = "/user/sendOtp")
	public Object forgotPassword(@RequestBody UserRegistration userRegistration) {
		Map<String, Object> model = new LinkedHashMap<String, Object>();

		try {

			if (userService.fetchUserByEmail(userRegistration.getEmail()) != null) {
				Integer otp = otpService.generateOTP(userRegistration.getEmail());
				boolean mail = MailHelper.sendOtpMail(userRegistration.getEmail(), otp);
				model.put("success", "1");
				model.put("msg", "Mail sent Successfully");
				model.put("OTP", otp);
			} else {
				model.put("msg", "Email Doesnot Exist");
			}
		} catch (Exception e) {
			LOGGER.error(e);
			e.printStackTrace();
			model.put("msg", "OTP send Failed");
		}
		return model;
		// return user;

	}

	@RequestMapping(value = "/user/validateOtp")
	public Map<String, String> validateOtp(@RequestBody Map<String, Object> userMap) {
		Map<String, String> result = new HashMap<String, String>();
		String msg = "";
		final String SUCCESS = "Entered Otp is valid";
		final String FAIL = "Entered Otp is NOT valid. Please Retry!";
		String username = (String) userMap.get("username");
		Integer otp = (Integer) userMap.get("otp");
		if (otp >= 0) {
			int serverOtp = otpService.getOtp(username);
			if (serverOtp > 0) {
				if (otp == serverOtp) {
					otpService.clearOTP(username);
					msg = "Entered Otp is valid";
				} else {
					msg = SUCCESS;
				}
			} else {
				msg = FAIL;
			}
		} else {
			msg = FAIL;
		}
		result.put("msg", msg);
		return result;
	}

	@RequestMapping(value = "/fileupload", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
	public Map<String, String> upload(HttpServletRequest request, @RequestParam("file") MultipartFile inputFile) {
		FileInfo fileInfo = new FileInfo();
		Map<String, String> result = new HashMap<String, String>();
		if (!inputFile.isEmpty()) {
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("rays-tech.com", "ssharma", "rays#789");
			try {
				String originalFilename = inputFile.getOriginalFilename();
				InputStream originalFilename1 = inputFile.getInputStream();
				String path = "smb://10.10.10.104/Python project/"+originalFilename;
				SmbFile sFile = new SmbFile(path, auth);
				SmbFileOutputStream sfos = new SmbFileOutputStream(sFile);
				InputStream fis = originalFilename1;
				try {

					final byte[] b = new byte[16 * 1024];
					int read = 0;
					while ((read = fis.read(b, 0, b.length)) > 0) {
						sfos.write(b, 0, read);
					}
				} finally {
					fis.close();
					sfos.close();
				}
				Integer userId = Integer.valueOf(request.getParameter("userid"));
				fileInfo.setUserId(userId);
				fileInfo.setFileName(inputFile.getOriginalFilename());
				fileInfo.setPath(sFile.getPath());
				fileInfo.setSize(inputFile.getSize());
				// fileInfo.setFileName(destinationFile.getPath());
				userService.getupload(fileInfo);
				result.put("success", "1");
				result.put("msg", "Document uploaded Successfully");
			

				System.out.println("Done");
			} catch (Exception e) {
				e.printStackTrace();
				result.put("success", "2");
				result.put("msg", "Something went wrong!!");
			}
		} else {
			result.put("success", "2");
			result.put("msg", "Something went wrong!!");
		}
		return result;
	}

	@RequestMapping("/hospital")
	public Object list() {
		List<Hospital> hospital = userService.list();
		return hospital;
	}

	@RequestMapping("/specialist")
	public Object getSpecialist(HttpServletRequest request) {
		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			return map;
		}
		List<Map> userResult = userService.getSpecialist("specialist");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", "1");
		result.put("msg", "specialist List");
		result.put("data", userResult);
		return result;
	}

	@RequestMapping("/mediaTypeList")
	public Object getMedialist(HttpServletRequest request) {
		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			return map;
		}

		List<Map> userResult = userService.getMedialist("MediaType");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", "1");
		result.put("msg", "Media List");
		result.put("data", userResult);
		return result;
	}

	@RequestMapping("/mediatype/{category}")
	public Object getMediatype(HttpServletRequest request, @PathVariable("category") String category) {
		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			return map;
		}
		List<Map> userResult = userService.getMedia(category);
		return userResult;
	}

	@RequestMapping("/stateList")
	public Object getStatelist(HttpServletRequest request) {
		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			return map;
		}
		List<Map> userResult = userService.getStatelist("State");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", "1");
		result.put("msg", "State List");
		result.put("data", userResult);
		return result;
	}

	@RequestMapping("/cityList")
	public Object getCitylist(HttpServletRequest request) {
		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			return map;
		}
		List<Map> userResult = userService.getAllCitylist("City");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", "1");
		result.put("msg", "City List");
		result.put("data", userResult);
		return result;
	}

	@RequestMapping("/city/{id}")
	public Object getCityByStateId(HttpServletRequest request, @PathVariable("id") Integer id) {
		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			return map;
		}
		List<Map> userResult = userService.getCityByStateId(id);
		return userResult;
	}

	@RequestMapping("/state/{id}")
	public Object getStateByCountryId(HttpServletRequest request, @PathVariable("id") Integer id) {
		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			return map;
		}
		List<Map> userResult = userService.getStateByCountryId(id);
		return userResult;
	}

	@RequestMapping("/personByName/{name}")
	public Object getPersonByName(HttpServletRequest request, @PathVariable("name") String name) {
		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			return map;
		}
		List<Map> userResult = userService.getPerson(name);
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("data", userResult);
		result.put("msg", "Data found.");
		result.put("success", "1");
		return result;
	}

	@RequestMapping("/user/logout/{id}")
	public ModelMap logout(@PathVariable("id") Integer id) {
		LOGGER.info("Call logout [ userId : " + id);
		Users user = null;
		ModelMap model = new ModelMap();
		try {
			user = userService.getPerson(id);

			if (user != null) {
				// required to logout from Quickblox also
				user.setIsOnline("No");
				model = (ModelMap) userService.updateProfile(user);
				model.put("msg", "User has successfully logout.");
				model.put("Success", "1");

				quickbloxService.logoutFromChat(Integer.parseInt(user.getExternalid()));
			} else {
				LOGGER.info("Passing wrong user id");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.info("ERROR : " + ex.getMessage());
		}
		return model;
	}

	@RequestMapping(value = "/setPin")
	public Object setPin(HttpServletRequest request, @RequestBody Map<String, String> userMap) {
		LOGGER.info("Set pin Api Called");
		ModelMap model = new ModelMap();
		ErrorObject error = new ErrorObject("Not Found", 404);
		// Boolean isValidate = true;
		/*
		 * Map<String, String> map = verifyToken(request); String isVerify =
		 * map.get("IsAuthenticated"); if (!"true".equals(isVerify)) { return map; }
		 */
		try {
			Users user = userService.get(Integer.parseInt(userMap.get("id")));
			String pin = passwordService.encrypt(userMap.get("pin").toString());
			user.setPin(pin);
			userService.updateProfile(user);
			model.put("success", "1");
			model.put("msg", "pin set successfully");
		} catch (Exception e) {
			LOGGER.error(e);
			model.put("msg", "pin set Fail");
		}
		return model;

	}

	@RequestMapping("/user/patientListReport")
	public Object getPatientListReport(HttpServletRequest request, @RequestBody Map<String, String> userMap) {
		LOGGER.info("patientListReport List Api Called");
		Map<String, String> map = verifyToken(request);
		String isVerify = map.get("IsAuthenticated");
		if (!"true".equals(isVerify)) {
			map = new HashMap<String, String>();
			map.put("success", "2");
			map.put("msg", isVerify);
			return map;
		}
		List<Map> userResult = userService.getDoctorListReport(userMap.get("userType"),
				userMap.get("fromDate") == null || userMap.get("fromDate").equals("") ? "" : userMap.get("fromDate"),
				userMap.get("toDate") == null || userMap.get("toDate").equals("") ? "" : userMap.get("toDate"));

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", "1");
		result.put("msg", "Patient List");
		result.put("data", userResult);
		return result;
	}

	public static void main(String[] args) {

		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("rays-tech.com", "ssharma", "rays#789"); // Authentication

		try {
			File fileSource = new File("C:/tmp/New.txt");
			String path = "smb://10.10.10.104/Python project/New.txt";
			SmbFile sFile = new SmbFile(path, auth);
			SmbFileOutputStream sfos = new SmbFileOutputStream(sFile);
			FileInputStream fis = new FileInputStream(fileSource);
			try {

				final byte[] b = new byte[16 * 1024];
				int read = 0;
				while ((read = fis.read(b, 0, b.length)) > 0) {
					sfos.write(b, 0, read);
				}
			} finally {
				fis.close();
				sfos.close();
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
}
