package icbt.oas.backend.util;

public class Constants {
   /**
    * Company activation token expiration
    */
   public static Long ACTIVATION_TOKEN_EXPIRATION_INTERVAL = 86400L;
   public static String ACTIVATION_TOKEN_EXPIRATION = "activation.token.expiration.in.seconds";
   public static String JWT_TOKEN_EXPIRATION = "jwt.token.expiration.in.seconds";
   public static String TOTP_EXPIRATION = "totp.interval.in.seconds";

   public static String PENDING_CONFIRMATION = "Pending Confirmation";
   public static String CONFIRMED = "Confirmed";
   public static String PENDING_DELETE_CONFIRMATION = "Pending Delete Confirmation";
   public static String PATH_PURCHASE_CONFIRMATION = "purchase_confirmation";
   public static String PATH_CONTRACT_DELETION_CONFIRMATION = "contract_deletion_confirmation";

   public static String FRONT_END_URI = "front.end.uri";

   public static String JWT_CLAIM_COMPANY_ID = "company-id";
   public static String JWT_CLAIM_PROJECT_CUSTOMER_ID = "project-customer-id";
   public static String JWT_CLAIM_CONTRACT_REMOVAL_REASON = "removed-reason";
   public static String JWT_CLAIM_ROLE = "role";
   public static String JWT_TOKEN_TYPE = "Bearer ";

   public static String PAYMENT_MODE_BANK_DEPOSIT = "Bank Deposit";
   public static String PAYMENT_MODE_CHEQUE = "Cheque";
   public static String PAYMENT_MODE_ONLINE_TRANSFER = "Online Transfer";

   public static String DOMAIN_HIGHLIGHTER = "[CPD]";
   public static String ACCESS_DENIED = "Access Denied";
   public static String ACCOUNT_SUSPENDED = "Your company account is not activated yet. An email has been sent to the admin containing an activation link";
   public static String ACTIVATION_EMAIL_SENT = "Please check your email to activate the account";
   public static String PASSWORD_CREATION_EMAIL_SENT = "Please check the email and create the password";
   public static String CONFIRMATION_EMAIL_SENT = "Please check the email and confirm the contract";
   public static String CONTRACT_DELETED = "Your contract has been removed";
   public static String PURCHASE_CONFIRMED = "Congratulations! you have confirmed the project unit booking";
   public static String DELETION_CONFIRMATION_EMAIL_SENT = "Please check the email and confirm the contract cancellation";
   public static String ALREADY_ACTIVATED = "Already Activated";
   public static String ALREADY_CONFIRMED = "Already Confirmed";
   public static String CONTACT_ADMIN = "Please contact the system admin";
   public static String AUTH_MESSAGE_EXPIRED = "Auth message is expired";
   public static String COMPANY_ID_EXISTS = "Company id is already in use";
   public static String COMPANY_POSITION_EXISTS = "This position already exists for the company";
   public static String COMPANY_PROJECT_EXISTS = "This project is already there with the same name";
   public static String COMPANY_PROJECT_UNIT_EXISTS = "This project unit is already there with the same name for the " +
           "same project";
   public static String COMPANY_PROJECT_CONTAINS_UNITS = "This project has units";
   public static String COMPANY_PROJECT_UNIT_CONTAINS_CUSTOMER = "This project unit has customer";
   public static String CONFIRMATION_FAILED = "Confirmation failed. Please contact the administrator";
   public static String CUSTOMER_NOT_FOUND = "Customer not found";
   public static String EMAIL_EXISTS = "Email address is already in use";
   public static String EMAIL_SENDING_FAILED = "Oops! Error while sending the ";
   public static String EMPTY_RESPONSE = "No response found";
   public static String ERROR = "Error";
   public static String INCORRECT_AUTH_MESSAGE = "Incorrect auth message";
   public static String INVALID_CREDENTIALS = "Invalid Credentials";
   public static String INVALID_COMPANY_ID = "Invalid company id";
   public static String INVALID_EMAIL = "No account is associated with the email address";
   public static String INVALID_PASSWORD = "You've entered wrong password";
   public static String CUSTOMER_ACCOUNT_SUSPENDED = "Your Account is inactive";
   public static String INVALID_REQUEST = "Invalid Request";
   public static String INVALID_USER_ID = "Invalid user id";
   public static String INVALID_ID = "Invalid id";
   public static String INVALID_OTP = "Invalid OTP";
   public static String INVALID_PROJECT_ID = "Invalid project id";
   public static String INVALID_PROJECT_UNIT_ID = "Invalid project unit id";
   public static String INVALID_CUSTOMER_ID = "Invalid customer id";
   public static String INVALID_PROJECT_CUSTOMER_ID = "Invalid project customer id";
   public static String INVALID_CUSTOMER_PAYMENT_ID = "Invalid customer payment id";
   public static String NO_CUSTOMER_PAYMENT_TO_SHOW = "There are no customer payments available";
   public static String NO_PROJECT_UNIT_TO_SHOW = "There are no project units available";
   public static String NO_PROJECT_UNIT_FOUND = "Project Unit Not Found";
   public static String N0_SCHEDULE_PAYMENT_TO_SHOW = "There are no schedule payments available";
   public static String INVALID_SCHEDULE_PAYMENT_ID = "Invalid schedule payment id";
   public static String OTP_SENT = "OTP is sent to the email";
   public static String OPERATION_FAILED = "Failed to proceed with the request. Please contact the administrator";
   public static String PASSWORD_ALREADY_SET = "Request is failed as the password has been already set";
   public static String PERMISSION_DENIED = "You don't have permission to access this resource";
   public static String PROJECT_UNIT_NOT_FOUND = "Project unit not found";
   public static String ROLE_EXISTS = "This role is already available";
   public static String SIGNUP_FAILED = "Failed to signup the account. Please contact the administrator";
   public static String STATUS = "Status";
   public static String STATUS_FAILED = "Failed";
   public static String STATUS_SUCCESS = "Success";
   public static String SYSTEM_ERROR = "Issue with the system. Please contact the administrator";
   public static String UNABLE_TO_PROCEED_WITH_EMAIL = "Unable to proceed with this email";
   public static String WRONG_OTP = "The entered OTP might be expired ot might not be matched your code. " +
           "Please try again";
   public static String CONSULTANT_CREATED_SUCCESSFULLY = "Consultant Created Successfully";
   public static String CONSULTANT_UPDATED_SUCCESSFULLY = "Consultant updated successfully";
   public static String CONSULTANT_DELETED_SUCCESSFULLY = "Consultant deleted successfully";
   public static String JOB_SEEKER_CREATED_SUCCESSFULLY = "Consultant Created Successfully";
   public static String JOB_SEEKER_UPDATED_SUCCESSFULLY = "Consultant updated successfully";
   public static String JOB_SEEKER_DELETED_SUCCESSFULLY = "Consultant deleted successfully";
   public static String APPOINTMENT_CREATED_SUCCESSFULLY = "Appointment created successfully";
   public static String APPOINTMENT_UPDATED_SUCCESSFULLY = "Appointment updated successfully";
   public static String APPOINTMENT_ACCEPTED_SUCCESSFULLY = "Appointment accepted successfully";
   public static String APPOINTMENT_DECLINED_SUCCESSFULLY = "Appointment declined successfully";
   public static String APPOINTMENT_DELETED_SUCCESSFULLY = "Appointment deleted successfully";
   public static String APPOINTMENT_NOT_FOUND = "Appointment not found";



}
